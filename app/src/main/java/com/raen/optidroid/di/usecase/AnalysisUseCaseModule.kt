package com.raen.optidroid.di.usecase

import com.raen.optidroid.domain.repository.AdbRepository
import com.raen.optidroid.domain.scheduler.AnalysisWorkScheduler
import com.raen.optidroid.domain.usecase.adb.ConnectAdbUseCase
import com.raen.optidroid.domain.usecase.analysis.CancelAnalysisUseCase
import com.raen.optidroid.domain.usecase.analysis.CancelAnalysisWorkUseCase
import com.raen.optidroid.domain.usecase.analysis.ObserveOptimizationAnalysisUseCase
import com.raen.optidroid.domain.usecase.analysis.RunAnalysisUseCase
import com.raen.optidroid.domain.usecase.analysis.StartAnalysisUseCase
import com.raen.optidroid.domain.usecase.analysis.StartAnalysisWorkUseCase
import com.raen.optidroid.domain.usecase.analysis.StopAnalysisUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides analysis-related use cases. */
@Module
@InstallIn(SingletonComponent::class)
object AnalysisUseCaseModule {

    @Provides
    @Singleton
    fun provideCancelAnalysisUseCase(adbRepository: AdbRepository): CancelAnalysisUseCase =
        CancelAnalysisUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideCancelAnalysisWorkUseCase(scheduler: AnalysisWorkScheduler): CancelAnalysisWorkUseCase =
        CancelAnalysisWorkUseCase(scheduler)

    @Provides
    @Singleton
    fun provideObserveOptimizationAnalysisUseCase(adbRepository: AdbRepository): ObserveOptimizationAnalysisUseCase =
        ObserveOptimizationAnalysisUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideRunAnalysisUseCase(adbRepository: AdbRepository): RunAnalysisUseCase =
        RunAnalysisUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideStartAnalysisWorkUseCase(scheduler: AnalysisWorkScheduler): StartAnalysisWorkUseCase =
        StartAnalysisWorkUseCase(scheduler)

    @Provides
    @Singleton
    fun provideStartAnalysisUseCase(
        connectAdbUseCase: ConnectAdbUseCase,
        startAnalysisWorkUseCase: StartAnalysisWorkUseCase
    ): StartAnalysisUseCase = StartAnalysisUseCase(connectAdbUseCase, startAnalysisWorkUseCase)

    @Provides
    @Singleton
    fun provideStopAnalysisUseCase(
        cancelAnalysisWorkUseCase: CancelAnalysisWorkUseCase,
        cancelAnalysisUseCase: CancelAnalysisUseCase
    ): StopAnalysisUseCase = StopAnalysisUseCase(cancelAnalysisWorkUseCase, cancelAnalysisUseCase)
}

