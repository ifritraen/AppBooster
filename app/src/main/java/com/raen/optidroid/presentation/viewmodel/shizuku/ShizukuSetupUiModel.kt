package com.raen.optidroid.presentation.viewmodel.shizuku

import com.raen.optidroid.domain.model.shizuku.ShizukuState

/**
 * Immutable UI state for the Shizuku setup screen.
 *
 * @property shizukuState Current runtime state of the Shizuku service.
 * @property isCheckingState Whether a background status check is in progress.
 */
data class ShizukuSetupUiModel(
    val shizukuState: ShizukuState = ShizukuState.NotRunning,
    val isCheckingState: Boolean = false
)
