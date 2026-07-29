package com.raen.optidroid.data.client

import android.util.Log
import com.raen.optidroid.domain.model.common.ShellCommandResult
import com.topjohnwu.superuser.Shell
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Singleton

/**
 * Root shell client backed by libsu (topjohnwu/libsu).
 *
 * libsu handles the root grant dialog, permission caching, and shell lifecycle.
 * Works with Magisk, KernelSU, APatch, and SuperSU.
 *
 * Shell.Builder must be configured once in Application.onCreate() before use.
 */
@Singleton
class RootShellClientImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    private val tag = "RootShellClient"

    /**
     * Returns true if root is available and granted.
     * Triggers the root grant dialog via the root manager on first call.
     */
    suspend fun isRootAvailable(): Boolean = withContext(ioDispatcher) {
        try {
            Shell.getShell().isRoot
        } catch (e: Exception) {
            Log.d(tag, "Root not available: ${e.message}")
            false
        }
    }

    /**
     * Executes a privileged shell command and returns the result.
     */
    suspend fun executeDetailed(command: String): ShellCommandResult = withContext(ioDispatcher) {
        try {
            val result = Shell.cmd(command).exec()
            ShellCommandResult(
                exitCode = if (result.isSuccess) 0 else 1,
                stdout = result.out.joinToString("\n"),
                stderr = result.err.joinToString("\n")
            )
        } catch (e: Exception) {
            Log.e(tag, "Root command failed: ${e.message}", e)
            ShellCommandResult(exitCode = -1, stdout = "", stderr = e.message ?: "Root shell error")
        }
    }

    /**
     * Streams command output line by line.
     */
    fun stream(command: String): Flow<Result<String>> = flow {
        try {
            val out = mutableListOf<String>()
            val err = mutableListOf<String>()
            val result = Shell.cmd(command).to(out, err).exec()
            out.forEach { emit(Result.success(it)) }
            if (!result.isSuccess && err.isNotEmpty()) {
                emit(Result.failure(RuntimeException(err.joinToString("\n"))))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(ioDispatcher)
}
