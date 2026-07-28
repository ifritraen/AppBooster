package com.raen.optidroid.data.util

import com.raen.optidroid.data.util.OptimizationLogger.Companion.MAX_LOG_ENTRIES
import com.raen.optidroid.domain.model.common.LogEntryType
import com.raen.optidroid.domain.model.common.LogMessageKey
import com.raen.optidroid.domain.model.common.OptimizationLogEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralised logger for optimization and analysis workflows.
 *
 * Owns the two observable log streams — raw text lines and structured
 * [OptimizationLogEntry] items — so that repository classes can
 * delegate all logging concerns without growing in size.
 *
 * @constructor Creates an empty logger ready to receive entries.
 */
@Singleton
class OptimizationLogger @Inject constructor() {

    companion object {
        /** Maximum number of structured log entries retained in memory. */
        private const val MAX_LOG_ENTRIES = 100
    }

    private val _commandOutput = MutableStateFlow<List<String>>(emptyList())

    /** Chronological list of raw shell output lines for terminal-like rendering. */
    val commandOutput: StateFlow<List<String>> = _commandOutput.asStateFlow()

    private val _logEntries = MutableStateFlow<List<OptimizationLogEntry>>(emptyList())

    /** Structured log entries for rich UI rendering. */
    val logEntries: StateFlow<List<OptimizationLogEntry>> = _logEntries.asStateFlow()

    /**
     * Appends a raw text line to the command output history.
     *
     * @param line Single textual log entry to append in execution order.
     */
    fun addLog(line: String) {
        _commandOutput.value = _commandOutput.value + line
    }

    /**
     * Appends a structured log entry for beautiful UI rendering.
     *
     * Entries are capped at [MAX_LOG_ENTRIES] to prevent memory bloat.
     *
     * @param type The type of log entry for visual differentiation.
     * @param message Human-readable message.
     * @param packageName Optional package name this entry relates to.
     * @param detail Optional additional detail text.
     */
    fun addLogEntry(
        type: LogEntryType,
        message: String = "",
        messageKey: LogMessageKey? = null,
        packageName: String? = null,
        detail: String? = null
    ) {
        val entry = OptimizationLogEntry(
            id = System.nanoTime(),
            timestamp = System.currentTimeMillis(),
            type = type,
            packageName = packageName,
            messageKey = messageKey,
            message = message,
            detail = detail
        )
        val updated = _logEntries.value + entry
        _logEntries.value = if (updated.size > MAX_LOG_ENTRIES) {
            updated.takeLast(MAX_LOG_ENTRIES)
        } else {
            updated
        }
    }

    /**
     * Clears all structured log entries. Called when starting a new run.
     */
    fun clearLogEntries() {
        _logEntries.value = emptyList()
    }
}

