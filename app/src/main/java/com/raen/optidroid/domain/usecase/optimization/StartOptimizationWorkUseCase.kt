package com.raen.optidroid.domain.usecase.optimization
import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.model.settings.AppOptimizationType
import com.raen.optidroid.domain.scheduler.OptimizationWorkScheduler

/**
 * Enqueues the foreground optimization WorkManager job.
 *
 * Business purpose:
 * - Centralizes work scheduling behind a testable abstraction.
 * - Keeps ViewModels free of WorkManager details.
 *
 * @property scheduler Scheduler responsible for enqueuing and canceling work.
 */
class StartOptimizationWorkUseCase(
    private val scheduler: OptimizationWorkScheduler
) {

    /**
     * Enqueues optimization as unique work.
     *
     * @param mode Optimization mode to execute.
     * @param forceOptimize When true, compiles every installed package
     *        regardless of its current compilation status.
     * @return [Resource.Success] when the request is enqueued.
     */
    operator fun invoke(
        mode: AppOptimizationType,
        forceOptimize: Boolean = false
    ): Resource<Unit> {
        scheduler.enqueue(mode, forceOptimize)
        return Resource.Success(Unit)
    }
}
