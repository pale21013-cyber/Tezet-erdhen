package com.example.updater

import android.app.Activity
import android.content.Context
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class UpdateManager(context: Context) {

    private val gitHubUpdateManager = GitHubUpdateManager(context)

    val updateStatus: StateFlow<UpdateStatus>
        get() = gitHubUpdateManager.updateStatus

    val pendingApkFile: File?
        get() = gitHubUpdateManager.pendingApkFile

    val currentVersionName: String
        get() = gitHubUpdateManager.currentVersionName

    val currentVersionCode: Int
        get() = gitHubUpdateManager.currentVersionCode

    var targetRepository: String
        get() = gitHubUpdateManager.targetRepository
        set(value) { gitHubUpdateManager.targetRepository = value }

    suspend fun checkForUpdates(repo: String = gitHubUpdateManager.targetRepository): UpdateInfo {
        return gitHubUpdateManager.checkForUpdates(repo)
    }

    suspend fun downloadAndInstallApk(apkUrl: String, activity: Activity? = null) {
        gitHubUpdateManager.downloadAndInstallApk(apkUrl, activity)
    }

    fun launchSystemInstallerAndStopApp(apkFile: File, activity: Activity? = null) {
        gitHubUpdateManager.launchSystemInstallerAndStopApp(apkFile, activity)
    }

    fun resumePendingInstallIfPermitted(activity: Activity? = null) {
        gitHubUpdateManager.resumePendingInstallIfPermitted(activity)
    }

    fun resetStatus() {
        gitHubUpdateManager.resetStatus()
    }
}
