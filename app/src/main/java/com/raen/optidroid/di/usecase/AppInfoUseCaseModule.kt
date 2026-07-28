package com.raen.optidroid.di.usecase

import com.raen.optidroid.domain.repository.AppInfoRepository
import com.raen.optidroid.domain.usecase.appinfo.GetAppInfoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provides app-info related use cases. */
@Module
@InstallIn(SingletonComponent::class)
object AppInfoUseCaseModule {

    @Provides
    @Singleton
    fun provideGetAppInfoUseCase(appInfoRepository: AppInfoRepository): GetAppInfoUseCase =
        GetAppInfoUseCase(appInfoRepository)
}

