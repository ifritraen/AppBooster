package com.raen.optidroid.presentation.viewmodel.settings

import com.raen.optidroid.domain.model.settings.AppOptimizationType
import com.raen.optidroid.domain.model.shizuku.ShizukuState

data class SettingsUiState(
    val appOptimizationType: AppOptimizationType = AppOptimizationType.SPEED_PROFILE,
    val appVersionName: String = "",
    val appVersionCode: String = "",
    val shizukuState: ShizukuState = ShizukuState.NotRunning,
    val autoOptimizationEnabled: Boolean = false,
    val unlockDelayMinutes: Int = 0,
    val periodicScheduleHours: Int = 1,
    val minUnlockIntervalHours: Int = 0,
    val optimizePrivateSpaceEnabled: Boolean = true,
    val optimizationProgress: com.raen.optidroid.domain.model.common.OptimizationProgress = com.raen.optidroid.domain.model.common.OptimizationProgress()
)
