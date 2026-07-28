package com.raen.optidroid.domain.usecase.optimization
import com.raen.optidroid.domain.model.common.OptimizationLogEntry
import com.raen.optidroid.domain.repository.AdbRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * Exposes the structured activity log entries as a stream.
 *
 * Business purpose:
 * - Keeps ViewModels independent of repository implementation details.
 * - Enables consistent activity feed across screens.
 *
 * @property repository Repository providing log entry stream.
 */
class ObserveOptimizationLogEntriesUseCase(
    private val repository: AdbRepository
) {

    /**
     * @return Hot [StateFlow] emitting the latest list of [OptimizationLogEntry].
     */
    operator fun invoke(): StateFlow<List<OptimizationLogEntry>> = repository.logEntries
}
