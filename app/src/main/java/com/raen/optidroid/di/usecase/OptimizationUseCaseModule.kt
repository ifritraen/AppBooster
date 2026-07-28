package com.raen.optidroid.di.usecase

import com.raen.optidroid.domain.repository.AdbRepository
import com.raen.optidroid.domain.scheduler.OptimizationWorkScheduler
import com.raen.optidroid.domain.usecase.adb.ConnectAdbUseCase
import com.raen.optidroid.domain.usecase.optimization.CancelOptimizationUseCase
import com.raen.optidroid.domain.usecase.optimization.CancelOptimizationWorkUseCase
import com.raen.optidroid.domain.usecase.optimization.DismissOptimizationResultUseCase
import com.raen.optidroid.domain.usecase.optimization.ObserveCommandOutputUseCase
import com.raen.optidroid.domain.usecase.optimization.ObserveOptimizationLogEntriesUseCase
import com.raen.optidroid.domain.usecase.optimization.ObserveOptimizationProgressUseCase
import com.raen.optidroid.domain.usecase.optimization.OptimizeAppUseCase
import com.raen.optidroid.domain.usecase.optimization.StartOptimizationUseCase
import com.raen.optidroid.domain.usecase.optimization.StartOptimizationWorkUseCase
import com.raen.optidroid.domain.usecase.optimization.StopOptimizationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides optimization-related use cases. */
@Module
@InstallIn(SingletonComponent::class)
object OptimizationUseCaseModule {

    @Provides
    @Singleton
    fun provideCancelOptimizationUseCase(adbRepository: AdbRepository): CancelOptimizationUseCase =
        CancelOptimizationUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideCancelOptimizationWorkUseCase(scheduler: OptimizationWorkScheduler): CancelOptimizationWorkUseCase =
        CancelOptimizationWorkUseCase(scheduler)

    @Provides
    @Singleton
    fun provideDismissOptimizationResultUseCase(adbRepository: AdbRepository): DismissOptimizationResultUseCase =
        DismissOptimizationResultUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideObserveCommandOutputUseCase(adbRepository: AdbRepository): ObserveCommandOutputUseCase =
        ObserveCommandOutputUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideObserveOptimizationLogEntriesUseCase(adbRepository: AdbRepository): ObserveOptimizationLogEntriesUseCase =
        ObserveOptimizationLogEntriesUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideObserveOptimizationProgressUseCase(adbRepository: AdbRepository): ObserveOptimizationProgressUseCase =
        ObserveOptimizationProgressUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideOptimizeAppUseCase(adbRepository: AdbRepository): OptimizeAppUseCase =
        OptimizeAppUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideStartOptimizationWorkUseCase(scheduler: OptimizationWorkScheduler): StartOptimizationWorkUseCase =
        StartOptimizationWorkUseCase(scheduler)

    @Provides
    @Singleton
    fun provideStartOptimizationUseCase(
        connectAdbUseCase: ConnectAdbUseCase,
        startOptimizationWorkUseCase: StartOptimizationWorkUseCase
    ): StartOptimizationUseCase = StartOptimizationUseCase(connectAdbUseCase, startOptimizationWorkUseCase)

    @Provides
    @Singleton
    fun provideStopOptimizationUseCase(
        cancelOptimizationWorkUseCase: CancelOptimizationWorkUseCase,
        cancelOptimizationUseCase: CancelOptimizationUseCase
    ): StopOptimizationUseCase =
        StopOptimizationUseCase(cancelOptimizationWorkUseCase, cancelOptimizationUseCase)
}

