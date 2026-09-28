package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.CycleRepository
import com.example.data.backup.DataBackupManager
import com.example.notifications.NotificationHelper
import com.example.security.SecurityManager
import com.example.shortcuts.AppShortcutHelper
import com.example.updater.UpdateManager
import com.example.worker.CyclePhaseWorkerScheduler

class AuraApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { CycleRepository(database) }
    val securityManager by lazy { SecurityManager(this) }
    val shortcutHelper by lazy { AppShortcutHelper(this) }
    val updateManager by lazy { UpdateManager(this) }
    val backupManager by lazy { DataBackupManager(repository, this) }

    override fun onCreate() {
        super.onCreate()
        shortcutHelper.updateDynamicShortcuts()
        NotificationHelper.createNotificationChannel(this)
        CyclePhaseWorkerScheduler.schedulePeriodicWorker(this)
    }
}
