package com.example.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object CyclePhaseWorkerScheduler {

    private const val WORK_NAME_PERIODIC = "aura_cycle_phase_periodic_worker"
    private const val WORK_NAME_ONETIME = "aura_cycle_phase_onetime_worker"

    fun schedulePeriodicWorker(context: Context) {
        try {
            val workRequest = PeriodicWorkRequestBuilder<CyclePhaseWorker>(
                12, TimeUnit.HOURS
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME_PERIODIC,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun triggerImmediateCheck(context: Context) {
        try {
            val oneTimeWork = OneTimeWorkRequestBuilder<CyclePhaseWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                WORK_NAME_ONETIME,
                ExistingWorkPolicy.REPLACE,
                oneTimeWork
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
