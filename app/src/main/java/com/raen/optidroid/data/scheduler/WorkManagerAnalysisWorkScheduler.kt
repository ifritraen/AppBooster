package com.raen.optidroid.data.scheduler

import android.content.Context
import com.raen.optidroid.domain.model.settings.AppOptimizationType
import com.raen.optidroid.domain.scheduler.AnalysisWorkScheduler
import com.raen.optidroid.presentation.worker.AnalysisWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * WorkManager-backed implementation of [AnalysisWorkScheduler].
 *
 * Business purpose:
 * - Encapsulates WorkManager API calls in the data layer.
 * - Keeps ViewModels and domain orchestration code free from WorkManager details.
 */
class WorkManagerAnalysisWorkScheduler @Inject constructor(
    @param:ApplicationContext private val context: Context
) : AnalysisWorkScheduler {

    override fun enqueue(mode: AppOptimizationType) {
        AnalysisWorker.enqueue(context, mode)
    }

    override fun cancel() {
        AnalysisWorker.cancel(context)
    }
}
