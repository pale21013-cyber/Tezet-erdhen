package com.example.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.MainActivity
import com.example.R
import com.example.data.AppDatabase
import com.example.data.entities.DailyLogEntity
import com.example.data.entities.DailyLogTagEntity
import com.example.localization.AppLanguage
import com.example.localization.getAppStrings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AuraCycleWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_QUICK_LOG) {
            val moodType = intent.getStringExtra(EXTRA_MOOD_TYPE) ?: "Happy"
            handleQuickLog(context, moodType)
        }
    }

    private fun handleQuickLog(context: Context, moodType: String) {
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = AppDatabase.getInstance(context)
                val todayStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                val existingLog = db.dailyLogDao().getLogForDateSync(todayStr)
                val activeCycle = db.cycleDao().getActiveCycleSync()

                val newLog = existingLog?.copy(isLogged = 1) ?: DailyLogEntity(
                    logDate = todayStr,
                    cycleId = activeCycle?.cycleId,
                    isLogged = 1,
                    sleepQuality = 4,
                    activityLevel = 1
                )
                db.dailyLogDao().insertOrUpdate(newLog)

                // Match tag by mood name
                val allTags = db.tagDao().getAllTagsSync()
                val targetTag = allTags.find { it.tagName.equals(moodType, ignoreCase = true) }
                if (targetTag != null) {
                    db.tagDao().insertLogTag(DailyLogTagEntity(todayStr, targetTag.tagId))
                }

                // Refresh widget UI with feedback
                val manager = AppWidgetManager.getInstance(context)
                val ids = manager.getAppWidgetIds(ComponentName(context, AuraCycleWidgetProvider::class.java))
                for (id in ids) {
                    updateAppWidget(context, manager, id, lastLoggedMood = moodType)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val ACTION_QUICK_LOG = "com.example.ACTION_QUICK_LOG_WIDGET"
        const val EXTRA_MOOD_TYPE = "extra_mood_type"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            lastLoggedMood: String? = null
        ) {
            val views = RemoteViews(context.packageName, R.layout.widget_aura_cycle)

            val secPrefs = context.getSharedPreferences("aura_security_prefs", Context.MODE_PRIVATE)
            val langCode = secPrefs.getString("app_language", AppLanguage.GERMAN.code) ?: AppLanguage.GERMAN.code
            val strings = getAppStrings(AppLanguage.fromCode(langCode))

            if (lastLoggedMood != null) {
                val localized = com.example.localization.getLocalizedTagName(lastLoggedMood, AppLanguage.fromCode(langCode))
                views.setTextViewText(R.id.widget_title_text, "✓ $localized")
                views.setTextViewText(R.id.widget_sub_text, strings.saveSuccessMsg)
            } else {
                views.setTextViewText(R.id.widget_title_text, strings.appName)
                views.setTextViewText(R.id.widget_sub_text, strings.fastActionsTitle)
            }

            // Main click opens app
            val openAppIntent = Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            val mainPendingIntent = PendingIntent.getActivity(
                context, 0, openAppIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            views.setOnClickPendingIntent(R.id.widget_root, mainPendingIntent)

            // Setup 4 quick action buttons for person's mode/mood
            views.setOnClickPendingIntent(R.id.widget_btn_happy, createQuickLogIntent(context, "Happy", 101))
            views.setOnClickPendingIntent(R.id.widget_btn_sensitive, createQuickLogIntent(context, "Sensitive", 102))
            views.setOnClickPendingIntent(R.id.widget_btn_tired, createQuickLogIntent(context, "Calm", 103))
            views.setOnClickPendingIntent(R.id.widget_btn_flow, createOpenFlowIntent(context, 104))

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun createQuickLogIntent(context: Context, mood: String, requestCode: Int): PendingIntent {
            val intent = Intent(context, AuraCycleWidgetProvider::class.java).apply {
                action = ACTION_QUICK_LOG
                putExtra(EXTRA_MOOD_TYPE, mood)
            }
            return PendingIntent.getBroadcast(
                context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }

        private fun createOpenFlowIntent(context: Context, requestCode: Int): PendingIntent {
            val intent = Intent(context, MainActivity::class.java).apply {
                action = Intent.ACTION_VIEW
                putExtra("action_route", "quick_flow")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            return PendingIntent.getActivity(
                context, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}
