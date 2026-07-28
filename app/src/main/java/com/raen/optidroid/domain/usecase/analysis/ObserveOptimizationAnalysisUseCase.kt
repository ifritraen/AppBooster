package com.raen.optidroid.domain.usecase.analysis
import com.raen.optidroid.domain.model.common.OptimizationAnalysis
import com.raen.optidroid.domain.repository.AdbRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * Exposes the current optimization analysis state as a stream.
 *
 * Business purpose:
 * - Keeps analysis observation in the domain layer.
 * - Allows multiple presentation entry points (Dashboard, notification, etc.)
 *   to observe analysis without coupling to the repository.
 *
 * @property repository Repository providing optimization analysis state.
 */
class ObserveOptimizationAnalysisUseCase(
    private val repository: AdbRepository
) {

    /**
     * @return Hot [StateFlow] emitting the latest [OptimizationAnalysis].
     */
    operator fun invoke(): StateFlow<OptimizationAnalysis> = repository.optimizationAnalysis
}
