package com.example.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.data.AppDatabase
import com.example.data.CycleRepository
import com.example.ml.CycleFeaturePipeline
import com.example.ml.LocalPredictorEngine
import com.example.notifications.NotificationHelper
import com.example.security.SecurityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CyclePhaseWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val context = applicationContext
            val database = AppDatabase.getInstance(context)
            val repository = CycleRepository(database)
            val securityManager = SecurityManager(context)

            val language = securityManager.appLanguage.value

            val cycles = repository.getAllCyclesSync()
            val logs = repository.getAllLogsSync()
            val tags = repository.getAllTagsSync()
            val logTags = repository.getAllDailyLogTagsSync()

            val featurePipeline = CycleFeaturePipeline()
            val predictorEngine = LocalPredictorEngine(featurePipeline)

            val vectors = featurePipeline.constructSlidingWindowVectors(
                cycles = cycles,
                logs = logs,
                allTags = tags,
                logTags = logTags,
                windowDays = 90
            )

            val (stats, _) = predictorEngine.runInference(
                cycles = cycles,
                featureVectors = vectors
            )

            val currentPhase = stats.currentPhase

            val prefs = context.getSharedPreferences("aura_phase_worker_prefs", Context.MODE_PRIVATE)
            val lastNotifiedPhaseName = prefs.getString("last_notified_phase", null)

            if (lastNotifiedPhaseName == null) {
                // Initial setup run - record current phase without notifying immediately to avoid spam on first launch
                prefs.edit().putString("last_notified_phase", currentPhase.name).apply()
            } else if (lastNotifiedPhaseName != currentPhase.name) {
                // User entered a NEW cycle phase!
                prefs.edit().putString("last_notified_phase", currentPhase.name).apply()
                NotificationHelper.showPhaseTransitionNotification(
                    context = context,
                    newPhase = currentPhase,
                    language = language
                )
            }

            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
