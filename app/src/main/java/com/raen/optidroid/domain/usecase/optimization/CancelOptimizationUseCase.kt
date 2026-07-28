package com.raen.optidroid.domain.usecase.optimization
import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.repository.AdbRepository

/**
 * Cancels the currently running optimization process.
 *
 * @param repository The repository managing the optimization.
 */
class CancelOptimizationUseCase(
    private val repository: AdbRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.cancelOptimization()
    }
}
