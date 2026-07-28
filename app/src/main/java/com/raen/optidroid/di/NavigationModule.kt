package com.raen.optidroid.di

import com.raen.optidroid.presentation.navigation.interfaces.NavigationManager
import com.raen.optidroid.presentation.navigation.NavigationManagerImpl

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    /**
     * Binds the [NavigationManagerImpl] to the [NavigationManager] interface
     * as a singleton.
     */
    @Binds
    @Singleton
    abstract fun bindNavigationManager(
        impl: NavigationManagerImpl
    ): NavigationManager
}