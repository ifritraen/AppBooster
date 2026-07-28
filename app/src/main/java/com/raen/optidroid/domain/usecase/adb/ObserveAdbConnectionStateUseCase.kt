package com.raen.optidroid.domain.usecase.adb
import com.raen.optidroid.domain.repository.AdbConnectionState
import com.raen.optidroid.domain.repository.AdbRepository
import kotlinx.coroutines.flow.StateFlow

/**
 * Exposes the current ADB connection state as a stream.
 *
 * Business purpose:
 * - Prevents presentation layer from depending directly on repository flows.
 * - Keeps the ViewModel API consistent (use-cases only).
 *
 * @property repository Repository providing ADB connection signals.
 */
class ObserveAdbConnectionStateUseCase(
    private val repository: AdbRepository
) {
    /**
     * @return Hot [StateFlow] emitting the latest [AdbConnectionState].
     */
    operator fun invoke(): StateFlow<AdbConnectionState> = repository.connectionState
}
