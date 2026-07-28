package com.raen.optidroid.domain.usecase.optimization
import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.scheduler.OptimizationWorkScheduler

/**
 * Cancels any running optimization work.
 *
 * Business purpose:
 * - Keeps WorkManager cancellation details out of ViewModels.
 * - Allows stop to be triggered consistently from UI and notifications.
 *
 * @property scheduler Scheduler responsible for canceling optimization work.
 */
class CancelOptimizationWorkUseCase(
    private val scheduler: OptimizationWorkScheduler
) {

    /**
     * @return [Resource.Success] after issuing cancellation.
     */
    operator fun invoke(): Resource<Unit> {
        scheduler.cancel()
        return Resource.Success(Unit)
    }
}
