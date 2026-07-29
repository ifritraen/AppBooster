package com.raen.optidroid.domain.repository

import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.model.settings.AppOptimizationType
import kotlinx.coroutines.flow.Flow

/**
 * Provides access to user-facing runtime configuration.
 *
 * Business purpose:
 * - Persist and observe the selected optimization mode.
 *
 * Note: Legacy ADB host/port/pairing-code configuration has been removed.
 */
interface SettingsRepository {

    /**
     * Observes the currently selected optimization mode so the UI can react
     * to changes, wrapping every emission into [Resource].
     *
     * @return A cold [Flow] emitting [Resource] instances representing the
     * active [AppOptimizationType].
     */
    fun observeAppOptimizationType(): Flow<Resource<AppOptimizationType>>

    /**
     * Persists the chosen optimization mode to durable storage so it is
     * restored on next launch.
     *
     * @param type The [AppOptimizationType] selected by the user.
     * @return A [Resource] describing the success or error of the operation.
     */
    suspend fun setAppOptimizationType(
        type: AppOptimizationType
    ): Resource<Unit>

    fun observeAutoOptimizationEnabled(): Flow<Resource<Boolean>>
    suspend fun setAutoOptimizationEnabled(enabled: Boolean): Resource<Unit>

    fun observeUnlockDelayMinutes(): Flow<Resource<Int>>
    suspend fun setUnlockDelayMinutes(minutes: Int): Resource<Unit>

    fun observePeriodicScheduleHours(): Flow<Resource<Int>>
    suspend fun setPeriodicScheduleHours(hours: Int): Resource<Unit>

    fun observeMinUnlockIntervalHours(): Flow<Resource<Int>>
    suspend fun setMinUnlockIntervalHours(hours: Int): Resource<Unit>

    fun observeLastUnlockTimestamp(): Flow<Resource<Long>>
    suspend fun setLastUnlockTimestamp(timestamp: Long): Resource<Unit>

    fun observeOptimizePrivateSpace(): Flow<Resource<Boolean>>
    suspend fun setOptimizePrivateSpace(enabled: Boolean): Resource<Unit>
}
