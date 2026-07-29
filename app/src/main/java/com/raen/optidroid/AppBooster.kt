package com.raen.optidroid

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.raen.optidroid.presentation.viewmodel.base.AppBoosterStringProvider
import com.topjohnwu.superuser.Shell
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Application class that configures Hilt dependency injection and WorkManager
 * with HiltWorkerFactory so that Workers can receive injected dependencies.
 */
@HiltAndroidApp
class AppBooster : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        // Configure libsu for root shell access — shows grant dialog on first use
        Shell.enableVerboseLogging = BuildConfig.DEBUG
        Shell.setDefaultBuilder(
            Shell.Builder.create()
                .setFlags(Shell.FLAG_REDIRECT_STDERR or Shell.FLAG_MOUNT_MASTER)
                .setTimeout(10)
        )
        AppBoosterStringProvider.init(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
