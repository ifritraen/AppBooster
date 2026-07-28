package com.raen.optidroid.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
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
        if (intent.action == Intent.ACTION_BOOT_COMPLETED || intent.action == Intent.ACTION_USER_PRESENT) {
            val pendingResult = goAsync()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val enabled = (settingsRepository.observeAutoOptimizationEnabled().first() as? Resource.Success)?.data ?: false
                    if (enabled) {
                        val delayMinutes = (settingsRepository.observeUnlockDelayMinutes().first() as? Resource.Success)?.data ?: 0
                        val modeRes = settingsRepository.observeAppOptimizationType().first()
                        val mode = (modeRes as? Resource.Success)?.data ?: AppOptimizationType.SPEED_PROFILE

                        val request = OneTimeWorkRequestBuilder<OptimizationWorker>()
                            .setInputData(
                                androidx.work.workDataOf(
                                    OptimizationWorker.KEY_OPTIMIZATION_MODE to mode.value
                                )
                            )
                            .setInitialDelay(delayMinutes.toLong(), TimeUnit.MINUTES)
                            .addTag(OptimizationWorker.TAG)
                            .build()

                        WorkManager.getInstance(context.applicationContext)
                            .enqueue(request)
                    }
                } finally {
                    pendingResult.finish()
                }
            }
        }
    }
}
