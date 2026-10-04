package com.example.security

import android.content.Context
import android.content.SharedPreferences
import com.example.localization.AppLanguage
import com.example.localization.AppPersona
import com.example.ui.theme.ThemeSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SecurityManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("aura_security_prefs", Context.MODE_PRIVATE)

    private val _isOnboardingCompleted = MutableStateFlow(prefs.getBoolean(KEY_ONBOARDING_COMPLETED, false))
    val isOnboardingCompleted: StateFlow<Boolean> = _isOnboardingCompleted.asStateFlow()

    // Default to German as requested
    private val initialLanguageCode = prefs.getString(KEY_APP_LANGUAGE, AppLanguage.GERMAN.code) ?: AppLanguage.GERMAN.code
    private val _appLanguage = MutableStateFlow(AppLanguage.fromCode(initialLanguageCode))
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    // Theme setting (System / Light / Dark)
    private val initialThemeCode = prefs.getString(KEY_THEME_SETTING, ThemeSetting.SYSTEM.code) ?: ThemeSetting.SYSTEM.code
    private val _themeSetting = MutableStateFlow(ThemeSetting.fromCode(initialThemeCode))
    val themeSetting: StateFlow<ThemeSetting> = _themeSetting.asStateFlow()

    // App Persona (Loving, Sarcastic, Logical, Funny)
    private val initialPersonaCode = prefs.getString(KEY_APP_PERSONA, AppPersona.LOVING.code) ?: AppPersona.LOVING.code
    private val _appPersona = MutableStateFlow(AppPersona.fromCode(initialPersonaCode))
    val appPersona: StateFlow<AppPersona> = _appPersona.asStateFlow()

    fun setOnboardingCompleted(completed: Boolean) {
        prefs.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply()
        _isOnboardingCompleted.value = completed
    }

    fun setThemeSetting(setting: ThemeSetting) {
        prefs.edit().putString(KEY_THEME_SETTING, setting.code).apply()
        _themeSetting.value = setting
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_APP_LANGUAGE, language.code).apply()
        _appLanguage.value = language
    }

    fun setPersona(persona: AppPersona) {
        prefs.edit().putString(KEY_APP_PERSONA, persona.code).apply()
        _appPersona.value = persona
    }

    companion object {
        private const val KEY_ONBOARDING_COMPLETED = "onboarding_completed"
        private const val KEY_APP_LANGUAGE = "app_language"
        private const val KEY_THEME_SETTING = "theme_setting"
        private const val KEY_APP_PERSONA = "app_persona"
    }
}

