package com.raen.optidroid.domain.usecase.analysis
import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.repository.AdbRepository

/**
 * Cancels the currently running analysis process.
 *
 * @param repository The repository managing the analysis.
 */
class CancelAnalysisUseCase(
    private val repository: AdbRepository
) {
    suspend operator fun invoke(): Resource<Unit> {
        return repository.cancelAnalysis()
    }
}
