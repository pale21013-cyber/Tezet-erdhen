package com.example.shortcuts

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.graphics.drawable.IconCompat
import com.example.MainActivity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings

class AppShortcutHelper(private val context: Context) {
    private val prefs = context.getSharedPreferences("aura_shortcut_frequencies", Context.MODE_PRIVATE)
    private val secPrefs = context.getSharedPreferences("aura_security_prefs", Context.MODE_PRIVATE)

    fun recordActionUsage(actionKey: String) {
        val count = prefs.getInt(actionKey, 0) + 1
        prefs.edit().putInt(actionKey, count).apply()
        updateDynamicShortcuts()
    }

    fun updateDynamicShortcuts(currentPhase: String = "Cycle Day 14") {
        if (!ShortcutManagerCompat.isRateLimitingActive(context)) {
            val langCode = secPrefs.getString("app_language", AppLanguage.GERMAN.code) ?: AppLanguage.GERMAN.code
            val language = AppLanguage.fromCode(langCode)
            val strings = getAppStrings(language)

            val happyCount = prefs.getInt("mood_happy", 12)
            val calmCount = prefs.getInt("mood_calm", 10)
            val sensitiveCount = prefs.getInt("mood_sensitive", 9)
            val energeticCount = prefs.getInt("mood_energetic", 8)
            val flowCount = prefs.getInt("quick_flow", 7)
            val cycleCount = prefs.getInt("cycle_status", 5)

            val actions = listOf(
                ShortcutData(
                    id = "shortcut_mood_happy",
                    shortLabel = "🌸 ${strings.fastMoodHappy}",
                    longLabel = "${strings.fastActionsTitle}: ${strings.fastMoodHappy}",
                    route = "mood_happy",
                    usageCount = happyCount
                ),
                ShortcutData(
                    id = "shortcut_mood_calm",
                    shortLabel = "🧘 ${strings.fastMoodCalm}",
                    longLabel = "${strings.fastActionsTitle}: ${strings.fastMoodCalm}",
                    route = "mood_calm",
                    usageCount = calmCount
                ),
                ShortcutData(
                    id = "shortcut_mood_sensitive",
                    shortLabel = "🥺 ${strings.fastMoodSensitive}",
                    longLabel = "${strings.fastActionsTitle}: ${strings.fastMoodSensitive}",
                    route = "mood_sensitive",
                    usageCount = sensitiveCount
                ),
                ShortcutData(
                    id = "shortcut_mood_energetic",
                    shortLabel = "⚡ ${strings.fastMoodEnergetic}",
                    longLabel = "${strings.fastActionsTitle}: ${strings.fastMoodEnergetic}",
                    route = "mood_energetic",
                    usageCount = energeticCount
                ),
                ShortcutData(
                    id = "shortcut_flow",
                    shortLabel = "💧 ${strings.tabToday}",
                    longLabel = strings.flowTitle,
                    route = "quick_flow",
                    usageCount = flowCount
                ),
                ShortcutData(
                    id = "shortcut_cycle",
                    shortLabel = currentPhase.take(15),
                    longLabel = currentPhase,
                    route = "cycle_status",
                    usageCount = cycleCount
                )
            ).sortedByDescending { it.usageCount }

            val shortcutInfos = actions.take(4).mapIndexed { index, item ->
                val intent = Intent(context, MainActivity::class.java).apply {
                    action = Intent.ACTION_VIEW
                    putExtra("action_route", item.route)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                }

                ShortcutInfoCompat.Builder(context, item.id)
                    .setShortLabel(item.shortLabel)
                    .setLongLabel(item.longLabel)
                    .setIcon(IconCompat.createWithResource(context, com.example.R.mipmap.ic_launcher))
                    .setIntent(intent)
                    .setRank(index)
                    .build()
            }

            try {
                ShortcutManagerCompat.setDynamicShortcuts(context, shortcutInfos)
            } catch (e: Exception) {
                // Ignore if device platform restricts shortcuts
            }
        }
    }

    private data class ShortcutData(
        val id: String,
        val shortLabel: String,
        val longLabel: String,
        val route: String,
        val usageCount: Int
    )
}
