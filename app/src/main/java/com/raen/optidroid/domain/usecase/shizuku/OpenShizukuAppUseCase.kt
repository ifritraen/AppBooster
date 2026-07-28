package com.raen.optidroid.domain.usecase.shizuku

import com.raen.optidroid.domain.client.ShizukuShellClient

/**
 * Opens the Shizuku app so the user can start the service.
 *
 * @property shizukuClient Client that can launch the Shizuku app.
 */
class OpenShizukuAppUseCase(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * Launches the Shizuku application.
     */
    operator fun invoke() = shizukuClient.openShizukuApp()
}

