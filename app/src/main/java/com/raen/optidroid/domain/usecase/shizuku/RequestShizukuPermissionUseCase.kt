package com.raen.optidroid.domain.usecase.shizuku

import com.raen.optidroid.domain.client.ShizukuShellClient

/**
 * Requests Shizuku runtime permission from the user.
 *
 * @property shizukuClient Client capable of triggering permission requests.
 */
class RequestShizukuPermissionUseCase(
    private val shizukuClient: ShizukuShellClient
) {
    /**
     * Launches the Shizuku permission request flow.
     */
    suspend operator fun invoke() = shizukuClient.requestPermission()
}

