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
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

data class UpdateInfo(
    val hasUpdate: Boolean,
    val latestVersionName: String,
    val latestVersionCode: Int,
    val releaseNotes: String,
    val downloadUrl: String
)

sealed class UpdateStatus {
    object Idle : UpdateStatus()
    object Checking : UpdateStatus()
    data class UpdateAvailable(val info: UpdateInfo) : UpdateStatus()
    object UpToDate : UpdateStatus()
    data class Downloading(val progressPercent: Int) : UpdateStatus()
    data class ReadyToInstall(val apkFile: File) : UpdateStatus()
    data class Error(val message: String) : UpdateStatus()
}

class UpdateManager(private val context: Context) {

    private val _updateStatus = MutableStateFlow<UpdateStatus>(UpdateStatus.Idle)
    val updateStatus: StateFlow<UpdateStatus> = _updateStatus.asStateFlow()

    var pendingApkFile: File? = null
        private set

    val currentVersionName: String = BuildConfig.VERSION_NAME
    val currentVersionCode: Int = BuildConfig.VERSION_CODE

    companion object {
        const val GITHUB_REPO = "pale21013-cyber/Tezet-erdhen"
        const val MANIFEST_URL = "https://raw.githubusercontent.com/pale21013-cyber/Tezet-erdhen/main/update_manifest.json"
        const val API_LATEST_RELEASE = "https://api.github.com/repos/pale21013-cyber/Tezet-erdhen/releases/latest"
        const val FALLBACK_APK_URL = "https://github.com/pale21013-cyber/Tezet-erdhen/releases/latest/download/AuraCycle-latest.apk"
    }

    suspend fun checkForUpdates(): UpdateInfo = withContext(Dispatchers.IO) {
        _updateStatus.value = UpdateStatus.Checking
        try {
            var manifestParsed = false
            var remoteVersionCode = currentVersionCode
            var remoteVersionName = currentVersionName
            var notes = ""
            var apkDownloadUrl = FALLBACK_APK_URL

            // 1. Try manifest
            try {
                val conn = openConnectionWithRedirects(MANIFEST_URL)
                if (conn.responseCode in 200..299) {
                    val content = conn.inputStream.bufferedReader().use { it.readText() }
                    conn.disconnect()
                    val json = org.json.JSONObject(content)
                    remoteVersionCode = json.optInt("versionCode", currentVersionCode)
                    remoteVersionName = json.optString("versionName", currentVersionName)
                    notes = json.optString("releaseNotes", "")
                    apkDownloadUrl = json.optString("downloadUrl", FALLBACK_APK_URL)
                    manifestParsed = true
                } else {
                    conn.disconnect()
                }
            } catch (e: Exception) {
                // Ignore manifest error, fallback to GitHub release API
            }

            if (!manifestParsed) {
                // 2. Try GitHub Release API
                try {
                    val conn = openConnectionWithRedirects(API_LATEST_RELEASE)
                    if (conn.responseCode in 200..299) {
                        val content = conn.inputStream.bufferedReader().use { it.readText() }
                        conn.disconnect()
                        val json = org.json.JSONObject(content)
                        val tagName = json.optString("tag_name", "").removePrefix("v")
                        remoteVersionName = tagName.ifEmpty { currentVersionName }
                        notes = json.optString("body", "Neueste Version via GitHub Release.")

                        val assets = json.optJSONArray("assets")
                        if (assets != null) {
                            for (i in 0 until assets.length()) {
                                val asset = assets.getJSONObject(i)
                                val name = asset.optString("name", "")
                                if (name.endsWith(".apk")) {
                                    apkDownloadUrl = asset.optString("browser_download_url", apkDownloadUrl)
                                    break
                                }
                            }
                        }
                        manifestParsed = true
                    } else {
                        conn.disconnect()
                    }
                } catch (e: Exception) {
                    // Ignore
                }
            }

            val hasUpdate = remoteVersionCode > currentVersionCode || (manifestParsed && remoteVersionName != currentVersionName)

            if (hasUpdate) {
                val info = UpdateInfo(
                    hasUpdate = true,
                    latestVersionName = remoteVersionName,
                    latestVersionCode = remoteVersionCode,
                    releaseNotes = notes.ifEmpty { "🌸 Neues GitHub Release auf pale21013-cyber/Tezet-erdhen verfügbar!" },
                    downloadUrl = apkDownloadUrl
                )
                _updateStatus.value = UpdateStatus.UpdateAvailable(info)
                info
            } else {
                _updateStatus.value = UpdateStatus.UpToDate
                UpdateInfo(false, currentVersionName, currentVersionCode, "", "")
            }
        } catch (e: Exception) {
            _updateStatus.value = UpdateStatus.Error(e.localizedMessage ?: "Update-Prüfung fehlgeschlagen")
            UpdateInfo(false, currentVersionName, currentVersionCode, "", "")
        }
    }

    private fun openConnectionWithRedirects(initialUrl: String, maxRedirects: Int = 10): HttpURLConnection {
        var urlStr = initialUrl
        var redirects = 0
        while (redirects < maxRedirects) {
            val url = URL(urlStr)
            val conn = url.openConnection() as HttpURLConnection
            conn.connectTimeout = 12000
            conn.readTimeout = 12000
            conn.instanceFollowRedirects = false
            conn.setRequestProperty("User-Agent", "AuraCycle-Android")
            conn.setRequestProperty("Accept", "*/*")

            val status = conn.responseCode
            if (status in 301..308) {
                val newUrl = conn.getHeaderField("Location")
                conn.disconnect()
                if (!newUrl.isNullOrEmpty()) {
                    urlStr = if (newUrl.startsWith("http")) newUrl else URL(url, newUrl).toString()
                    redirects++
                    continue
                }
            }
            return conn
        }
        throw java.io.IOException("Zu viele Umleitungen ($maxRedirects)")
    }

    suspend fun downloadAndInstallApk(
        apkUrl: String,
        activity: Activity? = null
    ) = withContext(Dispatchers.IO) {
        _updateStatus.value = UpdateStatus.Downloading(0)
        try {
            val updatesDir = File(context.getExternalCacheDir() ?: context.cacheDir, "updates")
            if (!updatesDir.exists()) {
                updatesDir.mkdirs()
            }
            val apkFile = File(updatesDir, "AuraCycle_update.apk")
            if (apkFile.exists()) {
                apkFile.delete()
            }

            var downloadedBytes = 0L
            val conn = openConnectionWithRedirects(apkUrl)

            if (conn.responseCode in 200..299) {
                val serverLength = conn.contentLengthLong
                val totalBytes = if (serverLength > 0) serverLength else (15 * 1024 * 1024L)

                conn.inputStream.use { input ->
                    FileOutputStream(apkFile).use { output ->
                        val buffer = ByteArray(16384)
                        var read: Int
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
                conn.disconnect()
            } else {
                val code = conn.responseCode
                conn.disconnect()
                throw java.io.IOException("HTTP-Fehler $code beim Herunterladen der APK.")
            }

            if (!apkFile.exists() || apkFile.length() < 100_000) {
                apkFile.delete()
                throw java.io.IOException("Unvollständige APK-Datei empfangen (${apkFile.length()} Bytes). Bitte erneut versuchen.")
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
            pendingApkFile = null
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
