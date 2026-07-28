package com.raen.optidroid.domain.usecase.optimization

import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.model.settings.AppOptimizationType
import com.raen.optidroid.domain.usecase.adb.ConnectAdbUseCase

/**
 * Starts an optimization run by ensuring connectivity and enqueuing foreground work.
 *
 * Business purpose:
 * - Provides a single entry point for starting optimization.
 * - Centralizes connectivity preconditions and scheduling.
 *
 * @property connectAdbUseCase Use case that establishes ADB/Shizuku connectivity.
 * @property startOptimizationWorkUseCase Use case that schedules optimization work.
 */
class StartOptimizationUseCase(
    private val connectAdbUseCase: ConnectAdbUseCase,
    private val startOptimizationWorkUseCase: StartOptimizationWorkUseCase
) {

    /**
     * Starts optimization for the given mode.
     *
     * @param mode Optimization mode to execute.
     * @param forceOptimize When true, compiles every installed package
     *        regardless of its current compilation status.
     * @return [Resource.Success] when work is scheduled, [Resource.Error] otherwise.
     */
    suspend operator fun invoke(
        mode: AppOptimizationType,
        forceOptimize: Boolean = false
    ): Resource<Unit> {
        return when (val connection = connectAdbUseCase()) {
            is Resource.Success -> startOptimizationWorkUseCase(mode, forceOptimize)
            is Resource.Error -> connection
        }
    }
}
