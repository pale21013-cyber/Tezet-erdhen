package com.example.security

import android.content.Context
import android.content.SharedPreferences
import com.example.localization.AppLanguage
import com.example.ui.theme.ThemeSetting
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SecurityManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("aura_security_prefs", Context.MODE_PRIVATE)

    private val _isLocked = MutableStateFlow(prefs.getBoolean(KEY_BIOMETRIC_ENABLED, true))
    val isLocked: StateFlow<Boolean> = _isLocked.asStateFlow()

    private val _isBiometricEnabled = MutableStateFlow(prefs.getBoolean(KEY_BIOMETRIC_ENABLED, true))
    val isBiometricEnabled: StateFlow<Boolean> = _isBiometricEnabled.asStateFlow()

    private val _currentPin = MutableStateFlow(prefs.getString(KEY_PIN, "1234") ?: "1234")
    val currentPin: StateFlow<String> = _currentPin.asStateFlow()

    // Default to German as requested
    private val initialLanguageCode = prefs.getString(KEY_APP_LANGUAGE, AppLanguage.GERMAN.code) ?: AppLanguage.GERMAN.code
    private val _appLanguage = MutableStateFlow(AppLanguage.fromCode(initialLanguageCode))
    val appLanguage: StateFlow<AppLanguage> = _appLanguage.asStateFlow()

    // Theme setting (System / Light / Dark)
    private val initialThemeCode = prefs.getString(KEY_THEME_SETTING, ThemeSetting.SYSTEM.code) ?: ThemeSetting.SYSTEM.code
    private val _themeSetting = MutableStateFlow(ThemeSetting.fromCode(initialThemeCode))
    val themeSetting: StateFlow<ThemeSetting> = _themeSetting.asStateFlow()

    fun setThemeSetting(setting: ThemeSetting) {
        prefs.edit().putString(KEY_THEME_SETTING, setting.code).apply()
        _themeSetting.value = setting
    }

    fun setLanguage(language: AppLanguage) {
        prefs.edit().putString(KEY_APP_LANGUAGE, language.code).apply()
        _appLanguage.value = language
    }

    fun unlockWithBiometric(): Boolean {
        _isLocked.value = false
        return true
    }

    fun verifyPin(enteredPin: String): Boolean {
        if (enteredPin == _currentPin.value) {
            _isLocked.value = false
            return true
        }
        return false
    }

    fun setBiometricEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_BIOMETRIC_ENABLED, enabled).apply()
        _isBiometricEnabled.value = enabled
        if (!enabled) {
            _isLocked.value = false
        }
    }

    fun setPin(newPin: String) {
        prefs.edit().putString(KEY_PIN, newPin).apply()
        _currentPin.value = newPin
    }

    fun lock() {
        if (_isBiometricEnabled.value) {
            _isLocked.value = true
        }
    }

    companion object {
        private const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
        private const val KEY_PIN = "security_pin"
        private const val KEY_APP_LANGUAGE = "app_language"
        private const val KEY_THEME_SETTING = "theme_setting"
    }
}
