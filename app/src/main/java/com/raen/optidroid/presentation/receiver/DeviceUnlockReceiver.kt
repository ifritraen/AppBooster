package com.raen.optidroid.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.model.settings.AppOptimizationType
import com.raen.optidroid.domain.repository.SettingsRepository
import com.raen.optidroid.presentation.worker.OptimizationWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Listens for device boot and unlock actions to trigger auto-optimization after user-configured delay.
 */
@AndroidEntryPoint
class DeviceUnlockReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED && intent.action != Intent.ACTION_USER_PRESENT) return

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val enabled = (settingsRepository.observeAutoOptimizationEnabled().first() as? Resource.Success)?.data ?: false
                if (!enabled) return@launch

                val minIntervalHours = (settingsRepository.observeMinUnlockIntervalHours().first() as? Resource.Success)?.data ?: 0
                val lastUnlockTimestamp = (settingsRepository.observeLastUnlockTimestamp().first() as? Resource.Success)?.data ?: 0L
                val currentTime = System.currentTimeMillis()

                if (minIntervalHours > 0 && lastUnlockTimestamp > 0L) {
                    val elapsedHours = (currentTime - lastUnlockTimestamp) / (1000 * 60 * 60)
                    if (elapsedHours < minIntervalHours) return@launch
                }

                settingsRepository.setLastUnlockTimestamp(currentTime)

                val delayMinutes = (settingsRepository.observeUnlockDelayMinutes().first() as? Resource.Success)?.data ?: 0
                val mode = (settingsRepository.observeAppOptimizationType().first() as? Resource.Success)?.data
                    ?: AppOptimizationType.SPEED_PROFILE

                // setExpedited grants a background-start exemption on Android 12+ so the worker
                // can call setForeground() and show a notification from the background.
                // OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST is the graceful fallback.
                val request = OneTimeWorkRequestBuilder<OptimizationWorker>()
                    .setInputData(workDataOf(OptimizationWorker.KEY_OPTIMIZATION_MODE to mode.value))
                    .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                    .apply { if (delayMinutes > 0) setInitialDelay(delayMinutes.toLong(), TimeUnit.MINUTES) }
                    .addTag(OptimizationWorker.TAG)
                    .build()

                WorkManager.getInstance(context.applicationContext)
                    .enqueueUniqueWork(OptimizationWorker.UNIQUE_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
