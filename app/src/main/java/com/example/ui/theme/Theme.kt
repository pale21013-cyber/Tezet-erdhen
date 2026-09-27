package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class ThemeSetting(val code: String) {
    SYSTEM("system"),
    LIGHT("light"),
    DARK("dark");

    companion object {
        fun fromCode(code: String): ThemeSetting =
            entries.find { it.code.equals(code, ignoreCase = true) } ?: SYSTEM
    }
}

private val DarkColorScheme = darkColorScheme(
    primary = RoseLight,
    onPrimary = Color(0xFF4C0519),
    primaryContainer = RoseContainerDark,
    onPrimaryContainer = OnRoseContainerDark,
    secondary = FollicularPurple,
    onSecondary = Color.White,
    secondaryContainer = FollicularSoftDark,
    onSecondaryContainer = Color(0xFFF3E8FF),
    tertiary = OvulationTeal,
    onTertiary = Color.White,
    tertiaryContainer = OvulationSoftDark,
    onTertiaryContainer = Color(0xFFCCFBF1),
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = DarkTextPrimary,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkBorder,
    outlineVariant = DarkBorderSubtle
)

private val LightColorScheme = lightColorScheme(
    primary = RosePrimary,
    onPrimary = Color.White,
    primaryContainer = RoseContainerLight,
    onPrimaryContainer = OnRoseContainerLight,
    secondary = FollicularPurple,
    onSecondary = Color.White,
    secondaryContainer = FollicularSoftLight,
    onSecondaryContainer = Color(0xFF581C87),
    tertiary = OvulationTeal,
    onTertiary = Color.White,
    tertiaryContainer = OvulationSoftLight,
    onTertiaryContainer = Color(0xFF115E59),
    background = LightBackground,
    surface = LightSurface,
    onBackground = LightTextPrimary,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceElevated,
    onSurfaceVariant = LightTextSecondary,
    outline = LightBorder,
    outlineVariant = LightBorderSubtle
)

@Composable
fun AuraTheme(
    themeSetting: ThemeSetting = ThemeSetting.SYSTEM,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (themeSetting) {
        ThemeSetting.LIGHT -> false
        ThemeSetting.DARK -> true
        ThemeSetting.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
