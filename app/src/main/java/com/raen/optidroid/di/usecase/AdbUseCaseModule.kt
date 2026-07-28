package com.raen.optidroid.di.usecase

import com.raen.optidroid.domain.repository.AdbRepository
import com.raen.optidroid.domain.usecase.adb.ConnectAdbUseCase
import com.raen.optidroid.domain.usecase.adb.EnsureAdbConnectedUseCase
import com.raen.optidroid.domain.usecase.adb.ObserveAdbConnectionStateUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides ADB-related use cases. */
@Module
@InstallIn(SingletonComponent::class)
object AdbUseCaseModule {

    @Provides
    @Singleton
    fun provideConnectAdbUseCase(adbRepository: AdbRepository): ConnectAdbUseCase =
        ConnectAdbUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideEnsureAdbConnectedUseCase(adbRepository: AdbRepository): EnsureAdbConnectedUseCase =
        EnsureAdbConnectedUseCase(adbRepository)

    @Provides
    @Singleton
    fun provideObserveAdbConnectionStateUseCase(adbRepository: AdbRepository): ObserveAdbConnectionStateUseCase =
        ObserveAdbConnectionStateUseCase(adbRepository)
}

