package com.example

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppTab
import com.example.ui.AuraApp
import com.example.ui.CycleViewModel
import com.example.ui.CycleViewModelFactory
import com.example.ui.theme.AuraTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: CycleViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as AuraApplication
        val factory = CycleViewModelFactory(
            app.repository,
            app.securityManager,
            app.shortcutHelper,
            app.updateManager,
            app.backupManager
        )
        viewModel = ViewModelProvider(this, factory)[CycleViewModel::class.java]

        handleIntent(intent)

        setContent {
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            AuraTheme(themeSetting = uiState.themeSetting) {
                AuraApp(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        val actionRoute = intent?.getStringExtra("action_route")
        val directMood = intent?.getStringExtra("extra_mood_direct") ?: intent?.getStringExtra("extra_mood_type")

        if (directMood != null) {
            viewModel.logFastMood(directMood)
            viewModel.selectTab(AppTab.TODAY)
            return
        }

        when (actionRoute) {
            "mood_happy" -> {
                viewModel.logFastMood("Happy")
                viewModel.selectTab(AppTab.TODAY)
            }
            "mood_calm" -> {
                viewModel.logFastMood("Calm")
                viewModel.selectTab(AppTab.TODAY)
            }
            "mood_sensitive" -> {
                viewModel.logFastMood("Sensitive")
                viewModel.selectTab(AppTab.TODAY)
            }
            "mood_energetic" -> {
                viewModel.logFastMood("Energetic")
                viewModel.selectTab(AppTab.TODAY)
            }
            "mood_tired" -> {
                viewModel.logFastMood("Tired")
                viewModel.selectTab(AppTab.TODAY)
            }
            "take_pill" -> {
                viewModel.toggleTablet(true)
                viewModel.saveCurrentLog()
                viewModel.selectTab(AppTab.TODAY)
            }
            "baby_chance" -> {
                viewModel.selectTab(AppTab.CALENDAR)
            }
            "calendar" -> {
                viewModel.selectTab(AppTab.CALENDAR)
            }
            "quick_mood" -> {
                viewModel.selectTab(AppTab.TODAY)
            }
            "quick_flow" -> {
                viewModel.selectTab(AppTab.TODAY)
            }
            "quick_symptoms" -> {
                viewModel.selectTab(AppTab.TODAY)
            }
            "cycle_status" -> {
                viewModel.selectTab(AppTab.TODAY)
            }
        }
    }
}
