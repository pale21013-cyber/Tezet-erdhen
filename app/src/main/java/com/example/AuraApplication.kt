package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.CycleRepository
import com.example.security.SecurityManager
import com.example.shortcuts.AppShortcutHelper
import com.example.updater.UpdateManager

class AuraApplication : Application() {

    val database by lazy { AppDatabase.getInstance(this) }
    val repository by lazy { CycleRepository(database) }
    val securityManager by lazy { SecurityManager(this) }
    val shortcutHelper by lazy { AppShortcutHelper(this) }
    val updateManager by lazy { UpdateManager(this) }

    override fun onCreate() {
        super.onCreate()
        shortcutHelper.updateDynamicShortcuts()
    }
}
