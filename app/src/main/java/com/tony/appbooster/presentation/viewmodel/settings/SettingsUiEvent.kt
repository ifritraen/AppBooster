package com.tony.appbooster.presentation.viewmodel.settings

import com.tony.appbooster.domain.model.settings.AppOptimizationType

sealed interface SettingsUiEvent {
    data class OnOptimizationTypeSelected(val type: AppOptimizationType) : SettingsUiEvent
    data class OnAutoOptimizationToggled(val enabled: Boolean) : SettingsUiEvent
    data class OnUnlockDelayChanged(val minutes: Int) : SettingsUiEvent
    data class OnPeriodicScheduleChanged(val hours: Int) : SettingsUiEvent
}
