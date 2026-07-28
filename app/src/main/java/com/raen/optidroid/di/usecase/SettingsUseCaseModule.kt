package com.raen.optidroid.di.usecase

import com.raen.optidroid.domain.repository.SettingsRepository
import com.raen.optidroid.domain.usecase.settings.ObserveAppOptimizationTypeUseCase
import com.raen.optidroid.domain.usecase.settings.SetAppOptimizationTypeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides settings-related use cases. */
@Module
@InstallIn(SingletonComponent::class)
object SettingsUseCaseModule {

    @Provides
    @Singleton
    fun provideObserveAppOptimizationTypeUseCase(settingsRepository: SettingsRepository): ObserveAppOptimizationTypeUseCase =
        ObserveAppOptimizationTypeUseCase(settingsRepository)

    @Provides
    @Singleton
    fun provideSetAppOptimizationTypeUseCase(settingsRepository: SettingsRepository): SetAppOptimizationTypeUseCase =
        SetAppOptimizationTypeUseCase(settingsRepository)
}

