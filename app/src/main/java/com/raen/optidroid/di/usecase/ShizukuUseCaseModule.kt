package com.raen.optidroid.di.usecase

import com.raen.optidroid.domain.client.ShizukuShellClient
import com.raen.optidroid.domain.usecase.shizuku.ObserveShizukuStateUseCase
import com.raen.optidroid.domain.usecase.shizuku.OpenShizukuAppUseCase
import com.raen.optidroid.domain.usecase.shizuku.OpenShizukuInstallPageUseCase
import com.raen.optidroid.domain.usecase.shizuku.RefreshShizukuStateUseCase
import com.raen.optidroid.domain.usecase.shizuku.RequestShizukuPermissionUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides Shizuku setup use cases. */
@Module
@InstallIn(SingletonComponent::class)
object ShizukuUseCaseModule {

    @Provides
    @Singleton
    fun provideObserveShizukuStateUseCase(shizukuClient: ShizukuShellClient): ObserveShizukuStateUseCase =
        ObserveShizukuStateUseCase(shizukuClient)

    @Provides
    @Singleton
    fun provideRefreshShizukuStateUseCase(shizukuClient: ShizukuShellClient): RefreshShizukuStateUseCase =
        RefreshShizukuStateUseCase(shizukuClient)

    @Provides
    @Singleton
    fun provideRequestShizukuPermissionUseCase(shizukuClient: ShizukuShellClient): RequestShizukuPermissionUseCase =
        RequestShizukuPermissionUseCase(shizukuClient)

    @Provides
    @Singleton
    fun provideOpenShizukuInstallPageUseCase(shizukuClient: ShizukuShellClient): OpenShizukuInstallPageUseCase =
        OpenShizukuInstallPageUseCase(shizukuClient)

    @Provides
    @Singleton
    fun provideOpenShizukuAppUseCase(shizukuClient: ShizukuShellClient): OpenShizukuAppUseCase =
        OpenShizukuAppUseCase(shizukuClient)
}

