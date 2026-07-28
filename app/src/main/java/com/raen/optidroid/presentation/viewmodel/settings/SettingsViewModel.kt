package com.raen.optidroid.presentation.viewmodel.settings

import androidx.lifecycle.viewModelScope
import com.raen.optidroid.domain.model.common.Resource
import com.raen.optidroid.domain.model.settings.AppOptimizationType
import com.raen.optidroid.domain.repository.SettingsRepository
import com.raen.optidroid.domain.usecase.appinfo.GetAppInfoUseCase
import com.raen.optidroid.domain.usecase.settings.ObserveAppOptimizationTypeUseCase
import com.raen.optidroid.domain.usecase.settings.SetAppOptimizationTypeUseCase
import com.raen.optidroid.domain.usecase.shizuku.ObserveShizukuStateUseCase
import com.raen.optidroid.presentation.navigation.interfaces.NavigationManager
import com.raen.optidroid.presentation.screen.settings.model.AppInfo
import com.raen.optidroid.presentation.viewmodel.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val observeAppOptimizationTypeUseCase: ObserveAppOptimizationTypeUseCase,
    private val setAppOptimizationTypeUseCase: SetAppOptimizationTypeUseCase,
    private val getAppInfoUseCase: GetAppInfoUseCase,
    private val observeShizukuStateUseCase: ObserveShizukuStateUseCase,
    private val settingsRepository: SettingsRepository
) : BaseViewModel<SettingsUiState, SettingsUiEvent, SettingsUiEffect>(navigationManager) {

    override val LOG_TAG: String = "SettingsViewModel"

    init {
        observeOptimizationType()
        observeShizukuState()
        observeAutoOptimizationSettings()
        loadAppInfo()
    }

    private fun observeOptimizationType() {
        viewModelScope.launch(exceptionHandler) {
            observeAppOptimizationTypeUseCase()
                .collectLatest { resource ->
                    if (resource is Resource.Success) {
                        updateUiData(currentUiData().copy(appOptimizationType = resource.data))
                    }
                }
        }
    }

    private fun observeShizukuState() {
        viewModelScope.launch(exceptionHandler) {
            observeShizukuStateUseCase()
                .collectLatest { shizukuState ->
                    updateUiData(currentUiData().copy(shizukuState = shizukuState))
                }
        }
    }

    private fun observeAutoOptimizationSettings() {
        viewModelScope.launch(exceptionHandler) {
            launch {
                settingsRepository.observeAutoOptimizationEnabled().collectLatest { res ->
                    if (res is Resource.Success) updateUiData(currentUiData().copy(autoOptimizationEnabled = res.data))
                }
            }
            launch {
                settingsRepository.observeUnlockDelayMinutes().collectLatest { res ->
                    if (res is Resource.Success) updateUiData(currentUiData().copy(unlockDelayMinutes = res.data))
                }
            }
            launch {
                settingsRepository.observePeriodicScheduleHours().collectLatest { res ->
                    if (res is Resource.Success) updateUiData(currentUiData().copy(periodicScheduleHours = res.data))
                }
            }
        }
    }

    private fun loadAppInfo() {
        executeAsync {
            val result = getAppInfoUseCase()
            if (result is Resource.Success) {
                val appInfo: AppInfo = result.data
                updateUiData(
                    currentUiData().copy(
                        appVersionName = appInfo.versionName,
                        appVersionCode = appInfo.versionCode
                    )
                )
            }
        }
    }

    override fun handleEvent(event: SettingsUiEvent) {
        when (event) {
            is SettingsUiEvent.OnOptimizationTypeSelected -> persistOptimizationType(event.type)
            is SettingsUiEvent.OnAutoOptimizationToggled -> {
                executeAsync {
                    settingsRepository.setAutoOptimizationEnabled(event.enabled)
                }
            }
            is SettingsUiEvent.OnUnlockDelayChanged -> {
                executeAsync {
                    settingsRepository.setUnlockDelayMinutes(event.minutes)
                }
            }
            is SettingsUiEvent.OnPeriodicScheduleChanged -> {
                executeAsync {
                    settingsRepository.setPeriodicScheduleHours(event.hours)
                }
            }
        }
    }

    fun onOptimizationTypeSelected(type: AppOptimizationType) {
        onEvent(SettingsUiEvent.OnOptimizationTypeSelected(type))
    }

    private fun persistOptimizationType(type: AppOptimizationType) {
        executeAsync {
            if (setAppOptimizationTypeUseCase(type) is Resource.Success) {
                updateUiData(currentUiData().copy(appOptimizationType = type))
            }
        }
    }

    private fun currentUiData(): SettingsUiState {
        return uiState.value.data ?: SettingsUiState()
    }
}
