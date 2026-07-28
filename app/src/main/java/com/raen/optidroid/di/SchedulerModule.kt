package com.raen.optidroid.di

import com.raen.optidroid.data.scheduler.WorkManagerAnalysisWorkScheduler
import com.raen.optidroid.data.scheduler.WorkManagerOptimizationWorkScheduler
import com.raen.optidroid.domain.scheduler.AnalysisWorkScheduler
import com.raen.optidroid.domain.scheduler.OptimizationWorkScheduler
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt bindings for work scheduling abstractions.
 *
 * Business purpose:
 * - Keeps WorkManager details out of ViewModels and domain orchestration.
 * - Enables easy substitution with fake schedulers in tests.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class SchedulerModule {

    /**
     * Binds WorkManager-backed analysis scheduling.
     */
    @Binds
    @Singleton
    abstract fun bindAnalysisWorkScheduler(
        impl: WorkManagerAnalysisWorkScheduler
    ): AnalysisWorkScheduler

    /**
     * Binds WorkManager-backed optimization scheduling.
     */
    @Binds
    @Singleton
    abstract fun bindOptimizationWorkScheduler(
        impl: WorkManagerOptimizationWorkScheduler
    ): OptimizationWorkScheduler
}
