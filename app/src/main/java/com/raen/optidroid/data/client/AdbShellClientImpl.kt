package com.raen.optidroid.data.client

import android.util.Log
import com.raen.optidroid.domain.client.AdbShellClient
import com.raen.optidroid.domain.client.ShizukuShellClient
import com.raen.optidroid.domain.model.common.ShellCommandResult
import com.raen.optidroid.domain.model.shizuku.ShizukuState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shizuku-based implementation of [AdbShellClient].
 *
 * Uses the Shizuku service to execute shell commands with elevated (shell UID) privileges.
 * This implementation delegates all operations to [ShizukuShellClient] which handles
 * the Shizuku lifecycle and permission management.
 *
 * **How it works:**
 * Shizuku runs a privileged server process (started via ADB or root) that accepts
 * Binder IPC calls from authorized apps. Commands are executed with the same
 * privileges as ADB shell, enabling system-level operations.
 *
 * **Setup required:**
 * 1. User installs Shizuku app
 * 2. User starts Shizuku service via ADB command or root
 * 3. User grants this app permission through Shizuku's UI
 */
@Singleton
class AdbShellClientImpl @Inject constructor(
    private val shizukuClient: ShizukuShellClient,
    private val rootClient: RootShellClientImpl
) : AdbShellClient {

    override suspend fun isConnected(): Boolean {
        shizukuClient.refreshState()
        if (shizukuClient.isReady()) return true
        return rootClient.isRootAvailable()
    }

    override suspend fun ensureConnected() {
        shizukuClient.refreshState()

        if (shizukuClient.isReady()) return

        if (rootClient.isRootAvailable()) return

        val currentState = shizukuClient.state.first()
        when (currentState) {
            ShizukuState.Ready -> {}
            ShizukuState.NotInstalled -> {
                throw IllegalStateException(
                    "Shizuku is not installed and Root access was not detected."
                )
            }
            ShizukuState.NotRunning -> {
                throw IllegalStateException(
                    "Shizuku service is not running and Root access was not detected."
                )
            }
            ShizukuState.PermissionRequired -> {
                throw IllegalStateException(
                    "Shizuku permission required and Root access was not detected."
                )
            }
            is ShizukuState.Error -> {
                throw IllegalStateException(
                    "Shizuku error: ${currentState.message}"
                )
            }
        }
    }

    override suspend fun executeDetailed(command: String): ShellCommandResult {
        shizukuClient.refreshState()
        if (shizukuClient.isReady()) {
            val result = shizukuClient.execute(command)
            if (!result.isSuccess) {
                Log.e(TAG, "Shizuku command failed with exit code ${result.exitCode}: ${result.error}")
            }
            return ShellCommandResult(
                exitCode = result.exitCode,
                stdout = result.output,
                stderr = result.error
            )
        }

        if (rootClient.isRootAvailable()) {
            return rootClient.executeDetailed(command)
        }

        ensureConnected()
        val result = shizukuClient.execute(command)
        return ShellCommandResult(
            exitCode = result.exitCode,
            stdout = result.output,
            stderr = result.error
        )
    }

    override suspend fun execute(command: String): String {
        return executeDetailed(command).stdout
    }

    override fun stream(command: String): Flow<Result<String>> {
        return if (shizukuClient.isReady()) {
            shizukuClient.executeStreaming(command)
        } else {
            rootClient.stream(command)
        }
    }

    companion object {
        private const val TAG = "AdbShellClientShizuku"
    }
}
