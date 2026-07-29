package com.raen.optidroid.data.client

import android.util.Log
import com.raen.optidroid.domain.model.common.ShellCommandResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Direct root (su) implementation for shell command execution.
 * Allows OptiDroid to run privileged system commands when direct root access
 * (Magisk, KernelSU, APatch, SuperSU) is granted.
 */
@Singleton
class RootShellClientImpl(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val TAG = "RootShellClient"
    }

    /**
     * Checks whether root access (su) is available and granted.
     */
    suspend fun isRootAvailable(): Boolean = withContext(ioDispatcher) {
        try {
            val process = ProcessBuilder("su", "-c", "id").start()
            val output = process.inputStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()
            exitCode == 0 && output.contains("uid=0")
        } catch (e: Exception) {
            Log.d(TAG, "Root check failed: ${e.message}")
            false
        }
    }

    /**
     * Executes a command using `su -c`.
     */
    suspend fun executeDetailed(command: String): ShellCommandResult = withContext(ioDispatcher) {
        try {
            val process = ProcessBuilder("su", "-c", command).start()

            val stdoutBuilder = StringBuilder()
            val stderrBuilder = StringBuilder()

            val readerThread = Thread {
                try {
                    process.inputStream.bufferedReader().use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            stdoutBuilder.append(line).append("\n")
                        }
                    }
                } catch (_: Exception) {}
            }

            val errReaderThread = Thread {
                try {
                    process.errorStream.bufferedReader().use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            stderrBuilder.append(line).append("\n")
                        }
                    }
                } catch (_: Exception) {}
            }

            readerThread.start()
            errReaderThread.start()

            val exitCode = process.waitFor()
            readerThread.join(2000)
            errReaderThread.join(2000)

            ShellCommandResult(
                exitCode = exitCode,
                stdout = stdoutBuilder.toString(),
                stderr = stderrBuilder.toString()
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error executing root command: ${e.message}", e)
            ShellCommandResult(
                exitCode = -1,
                stdout = "",
                stderr = e.message ?: "Failed to execute root command"
            )
        }
    }

    /**
     * Streams command output line by line using root shell.
     */
    fun stream(command: String): Flow<Result<String>> = flow {
        try {
            val process = ProcessBuilder("su", "-c", command).start()
            process.inputStream.bufferedReader().use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    emit(Result.success(line!!))
                }
            }
            val exitCode = process.waitFor()
            if (exitCode != 0) {
                val err = process.errorStream.bufferedReader().use { it.readText() }
                if (err.isNotBlank()) {
                    emit(Result.failure(RuntimeException("Root command exited with code $exitCode: $err")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }.flowOn(ioDispatcher)
}
