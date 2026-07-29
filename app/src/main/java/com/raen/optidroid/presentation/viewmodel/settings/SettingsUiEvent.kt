package com.raen.optidroid.presentation.viewmodel.settings

import com.raen.optidroid.domain.model.settings.AppOptimizationType

sealed interface SettingsUiEvent {
    data class OnOptimizationTypeSelected(val type: AppOptimizationType) : SettingsUiEvent
    data class OnAutoOptimizationToggled(val enabled: Boolean) : SettingsUiEvent
    data class OnUnlockDelayChanged(val minutes: Int) : SettingsUiEvent
    data class OnPeriodicScheduleChanged(val hours: Int) : SettingsUiEvent
    data class OnMinUnlockIntervalChanged(val hours: Int) : SettingsUiEvent
    data class OnOptimizePrivateSpaceToggled(val enabled: Boolean) : SettingsUiEvent
}
