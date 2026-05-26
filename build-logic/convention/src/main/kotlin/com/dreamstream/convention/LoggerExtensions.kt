package com.dreamstream.convention

import org.gradle.api.logging.Logger

/* ───────────────────── Logger color extensions ───────────────────── */

private const val ANSI_RESET = "\u001B[0m"
private const val ANSI_RED = "\u001B[31m"
private const val ANSI_GREEN = "\u001B[32m"
private const val ANSI_YELLOW = "\u001B[33m"
private const val ANSI_DIM = "\u001B[2m"


private fun String.color(code: String): String = "$code$this$ANSI_RESET"

/**
 * Logs an informational/skip message in a dim color to de‑emphasize it.
 */
internal fun Logger.infoColor(message: String) {
    info(message.color(ANSI_DIM))
}

/**
 * Logs a warning message in yellow.
 */
internal fun Logger.warnColor(message: String) {
    warn(message.color(ANSI_YELLOW))
}

/**
 * Logs a success / lifecycle message in green.
 */
internal fun Logger.lifecycleSuccess(message: String) {
    lifecycle(message.color(ANSI_GREEN))
}

/**
 * Logs an error‑style message in red (does not change log level).
 */
internal fun Logger.errorColor(message: String) {
    error(message.color(ANSI_RED))
}
