package com.example.updater

import java.io.File

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
