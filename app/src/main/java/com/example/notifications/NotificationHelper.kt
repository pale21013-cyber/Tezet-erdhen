package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.localization.AppLanguage
import com.example.ml.CyclePhase

object NotificationHelper {

    const val CHANNEL_ID_PHASE = "channel_cycle_phase_updates"
    const val NOTIFICATION_ID_PHASE = 2001

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Zyklusphasen & Empfehlungen"
            val descriptionText = "Erinnerungen bei Eintritt in eine neue Zyklusphase mit personalisierten Ernährungs- und Bewegungstipps."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID_PHASE, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showPhaseTransitionNotification(
        context: Context,
        newPhase: CyclePhase,
        language: AppLanguage = AppLanguage.GERMAN
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("action_route", "phase_insights")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val (title, text) = when (language) {
            AppLanguage.GERMAN -> when (newPhase) {
                CyclePhase.MENSTRUAL -> "🩸 Neue Phase: Menstruation" to "Zeit für Ruhe, Wärme & eisenhaltige Nahrung. Schau dir deine Ernährungstipps an!"
                CyclePhase.FOLLICULAR -> "💜 Neue Phase: Follikelphase" to "Steigende Energie & Fokus! Entdecke deine Kohlenhydrat- & Workout-Empfehlungen."
                CyclePhase.OVULATORY -> "🩵 Neue Phase: Eisprung (Ovulation)" to "Höchste Vitalität & Fruchtbarkeit! Prüfe deine Antioxidantien- & Power-Tipps."
                CyclePhase.LUTEAL -> "🟠 Neue Phase: Lutealphase" to "Erweiterter Energiebedarf! Schau dir deine Kalorien- & B6-Tipps zur PMS-Linderung an."
            }
            AppLanguage.ALBANIAN -> when (newPhase) {
                CyclePhase.MENSTRUAL -> "🩸 Fazë e Re: Menstruacionet" to "Koha për qetësi, ngrohtësi dhe ushqim me hekur. Shiko këshillat e ushqimit!"
                CyclePhase.FOLLICULAR -> "💜 Fazë e Re: Faza Follikulare" to "Energji në rritje! Zbuloni rekomandimet për karbohidrate dhe stërvitje."
                CyclePhase.OVULATORY -> "🩵 Fazë e Re: Ovulacioni" to "Vitalitet maksimal! Kontrolloni këshillat për antioksidues dhe stërvitje."
                CyclePhase.LUTEAL -> "🟠 Fazë e Re: Faza Luteale" to "Metabolizëm në rritje! Shiko këshillat për ushqim dhe zbutjen e PMS."
            }
            AppLanguage.ENGLISH -> when (newPhase) {
                CyclePhase.MENSTRUAL -> "🩸 New Phase: Menstrual Phase" to "Time for rest, warmth & iron-rich foods. Check your personalized nutrition tips!"
                CyclePhase.FOLLICULAR -> "💜 New Phase: Follicular Phase" to "Rising energy & focus! Discover your complex carb & strength workout tips."
                CyclePhase.OVULATORY -> "🩵 New Phase: Ovulation Phase" to "Peak vitality & fertility! Check your antioxidant & high-power workout recommendations."
                CyclePhase.LUTEAL -> "🟠 New Phase: Luteal Phase" to "Metabolic rate rising! Check your calorie surplus & Vitamin B6 tips for PMS ease."
            }
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_PHASE)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        try {
            val notificationManager = NotificationManagerCompat.from(context)
            if (notificationManager.areNotificationsEnabled()) {
                notificationManager.notify(NOTIFICATION_ID_PHASE, builder.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
