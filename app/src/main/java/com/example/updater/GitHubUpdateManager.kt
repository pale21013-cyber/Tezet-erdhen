package com.example.updater

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit

class GitHubUpdateManager(private val context: Context) {

    private val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val updateStatus: StateFlow<UpdateStatus> = _updateStatus.asStateFlow()

    var pendingApkFile: File? = null
        private set

    var targetRepository: String = DEFAULT_REPO

    val currentVersionName: String = BuildConfig.VERSION_NAME
    val currentVersionCode: Int = BuildConfig.VERSION_CODE

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()
    }

    companion object {
        const val DEFAULT_REPO = "pale21013-cyber/Tezet-erdhen"
        const val USER_AGENT = "LabCast-Android-OTA"
        const val FALLBACK_APK_URL = "https://github.com/pale21013-cyber/Tezet-erdhen/releases/latest/download/AuraCycle-latest.apk"

        fun extractVersionNumbers(versionStr: String): List<Int> {
            val clean = versionStr
                .replace(Regex("^(v|release-|build-)"), "")
                .split("+")[0]
                .split("-")[0]
            val parts = clean.split(".")
            return parts.mapNotNull { part ->
                part.filter { it.isDigit() }.toIntOrNull()
            }
        }

        fun isNewerVersion(remoteVersion: String, currentVersion: String): Boolean {
            val remote = extractVersionNumbers(remoteVersion)
            val current = extractVersionNumbers(currentVersion)

            val maxLen = maxOf(remote.size, current.size)
            for (i in 0 until maxLen) {
                val r = remote.getOrElse(i) { 0 }
                val c = current.getOrElse(i) { 0 }
                if (r > c) return true
                if (r < c) return false
            }
            return false
        }
    }

    suspend fun checkForUpdates(repo: String = targetRepository): UpdateInfo = withContext(Dispatchers.IO) {
        targetRepository = repo.trim().ifEmpty { DEFAULT_REPO }
        _updateStatus.value = UpdateStatus.Checking

        try {
            var remoteVersionName = currentVersionName
            var remoteVersionCode = currentVersionCode
            var releaseNotes = ""
            var apkDownloadUrl = ""
            var hasDirectApk = false
            var releaseFound = false

            // Query GitHub Releases API
            val apiUrl = "https://api.github.com/repos/$targetRepository/releases/latest"
            val request = Request.Builder()
                .url(apiUrl)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "application/vnd.github.v3+json")
                .build()

            var responseBody: String? = null
            var retries = 0
            val maxRetries = 3

            while (retries < maxRetries && responseBody == null) {
                try {
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        responseBody = response.body?.string()
                    } else if (response.code == 404) {
                        // Fallback to /releases endpoint if no "latest" release tag exists
                        val fallbackReq = Request.Builder()
                            .url("https://api.github.com/repos/$targetRepository/releases")
                            .header("User-Agent", USER_AGENT)
                            .header("Accept", "application/vnd.github.v3+json")
                            .build()
                        val fallbackResp = okHttpClient.newCall(fallbackReq).execute()
                        if (fallbackResp.isSuccessful) {
                            val arrayStr = fallbackResp.body?.string()
                            if (!arrayStr.isNullOrBlank()) {
                                val jsonArr = JSONArray(arrayStr)
                                if (jsonArr.length() > 0) {
                                    responseBody = jsonArr.getJSONObject(0).toString()
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    retries++
                    if (retries >= maxRetries) throw e
                    kotlinx.coroutines.delay(1000)
                }
            }

            if (!responseBody.isNullOrBlank()) {
                val json = JSONObject(responseBody)
                val tagName = json.optString("tag_name", "")
                remoteVersionName = tagName.removePrefix("v").removePrefix("release-")
                releaseNotes = json.optString("body", "🌸 Neues GitHub Release auf $targetRepository verfügbar!")
                releaseFound = true

                // Iterate through release assets to find downloadable .apk
                val assets = json.optJSONArray("assets")
                if (assets != null) {
                    for (i in 0 until assets.length()) {
                        val asset = assets.getJSONObject(i)
                        val name = asset.optString("name", "")
                        if (name.endsWith(".apk", ignoreCase = true)) {
                            apkDownloadUrl = asset.optString("browser_download_url", "")
                            hasDirectApk = true
                            break
                        }
                    }
                }
            }

            if (!hasDirectApk && apkDownloadUrl.isEmpty()) {
                apkDownloadUrl = "https://github.com/$targetRepository/releases/latest/download/AuraCycle-latest.apk"
            }

            val isNewer = isNewerVersion(remoteVersionName, currentVersionName)
            val hasUpdate = isNewer || (releaseFound && remoteVersionName != currentVersionName)

            if (hasUpdate) {
                val info = UpdateInfo(
                    hasUpdate = true,
                    latestVersionName = remoteVersionName.ifEmpty { currentVersionName },
                    latestVersionCode = remoteVersionCode,
                    releaseNotes = releaseNotes.ifEmpty { "🌸 Neues GitHub Release auf $targetRepository verfügbar!" },
                    downloadUrl = apkDownloadUrl
                )
                _updateStatus.value = UpdateStatus.UpdateAvailable(info)
                info
            } else {
                _updateStatus.value = UpdateStatus.UpToDate
                UpdateInfo(false, currentVersionName, currentVersionCode, "", "")
            }
        } catch (e: Exception) {
            val errorMsg = e.localizedMessage ?: "Fehler bei der GitHub OTA Update-Prüfung"
            _updateStatus.value = UpdateStatus.Error(errorMsg)
            UpdateInfo(false, currentVersionName, currentVersionCode, "", "")
        }
    }

    suspend fun downloadAndInstallApk(
        apkUrl: String,
        activity: Activity? = null
    ) = withContext(Dispatchers.IO) {
        _updateStatus.value = UpdateStatus.Downloading(0)
        try {
            val updatesDir = File(context.externalCacheDir ?: context.cacheDir, "updates")
            if (!updatesDir.exists()) {
                updatesDir.mkdirs()
            }
            val apkFile = File(updatesDir, "AuraCycle_OTA_update.apk")
            if (apkFile.exists()) {
                apkFile.delete()
            }

            val request = Request.Builder()
                .url(apkUrl)
                .header("User-Agent", USER_AGENT)
                .header("Accept", "*/*")
                .build()

            val response = okHttpClient.newCall(request).execute()
            if (!response.isSuccessful) {
                throw IOException("HTTP-Fehler ${response.code} beim Herunterladen der APK.")
            }

            val body = response.body ?: throw IOException("Leerer Response Body von GitHub received.")
            val contentLength = body.contentLength()
            val totalBytes = if (contentLength > 0) contentLength else (15 * 1024 * 1024L)

            body.byteStream().use { input ->
                FileOutputStream(apkFile).use { output ->
                    val buffer = ByteArray(16384)
                    var read: Int
                    var downloadedBytes = 0L
                    var lastReportTime = System.currentTimeMillis()

                    while (input.read(buffer).also { read = it } != -1) {
                        output.write(buffer, 0, read)
                        downloadedBytes += read
                        val now = System.currentTimeMillis()
                        if (now - lastReportTime > 100) {
                            lastReportTime = now
                            val progress = ((downloadedBytes * 100) / totalBytes).toInt().coerceIn(0, 99)
                            _updateStatus.value = UpdateStatus.Downloading(progress)
                        }
                    }
                }
            }

            if (!apkFile.exists() || apkFile.length() < 100_000) {
                apkFile.delete()
                throw IOException("Unvollständige APK-Datei empfangen (${apkFile.length()} Bytes). Bitte erneut versuchen.")
            }

            _updateStatus.value = UpdateStatus.Downloading(100)
            pendingApkFile = apkFile
            _updateStatus.value = UpdateStatus.ReadyToInstall(apkFile)

            withContext(Dispatchers.Main) {
                launchSystemInstallerAndStopApp(apkFile, activity)
            }
        } catch (e: Exception) {
            _updateStatus.value = UpdateStatus.Error("Download fehlgeschlagen: ${e.localizedMessage}")
        }
    }

    fun launchSystemInstallerAndStopApp(apkFile: File, activity: Activity? = null) {
        try {
            if (!apkFile.exists() || apkFile.length() < 100_000) {
                _updateStatus.value = UpdateStatus.Error("Ungültige APK-Datei. Bitte erneut herunterladen.")
                return
            }

            pendingApkFile = apkFile

            // Check install unknown apps permission on API 26+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (!context.packageManager.canRequestPackageInstalls()) {
                    val permissionIntent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).apply {
                        data = Uri.parse("package:${context.packageName}")
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    context.startActivity(permissionIntent)
                    _updateStatus.value = UpdateStatus.Error("Bitte 'Unbekannte Apps installieren' in den Einstellungen aktivieren.")
                    return
                }
            }

            val apkUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                apkFile
            )

            val installIntent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(apkUri, "application/vnd.android.package-archive")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            }

            // Grant permissions to all matching installer activities
            val resolvedActivities = context.packageManager.queryIntentActivities(installIntent, PackageManager.MATCH_DEFAULT_ONLY)
            for (res in resolvedActivities) {
                val packageName = res.activityInfo.packageName
                context.grantUriPermission(packageName, apkUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            context.startActivity(installIntent)
            _updateStatus.value = UpdateStatus.ReadyToInstall(apkFile)
        } catch (e: Exception) {
            _updateStatus.value = UpdateStatus.Error("System-Installation fehlgeschlagen: ${e.message}")
        }
    }

    fun resumePendingInstallIfPermitted(activity: Activity? = null) {
        val file = pendingApkFile
        if (file != null && file.exists() && file.length() >= 100_000) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O || context.packageManager.canRequestPackageInstalls()) {
                launchSystemInstallerAndStopApp(file, activity)
            }
        }
    }

    fun resetStatus() {
        pendingApkFile = null
        _updateStatus.value = UpdateStatus.Idle
    }
}
