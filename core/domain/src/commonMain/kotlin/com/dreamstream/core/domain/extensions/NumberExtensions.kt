package com.dreamstream.core.domain.extensions

// ============================================================================
// Time & Duration Formatting Utilities
// ============================================================================

/**
 * Converts a duration in milliseconds to a human-readable time format.
 *
 * Formats the duration as `HH:MM:SS` if hours > 0, otherwise `MM:SS`.
 *
 * ### Examples
 * ```kotlin
 * 3665000L.toReadableDuration()    // "1:01:05"  (1 hour, 1 min, 5 sec)
 * 65000L.toReadableDuration()      // "1:05"     (1 min, 5 sec)
 * 5000L.toReadableDuration()       // "0:05"     (5 sec)
 * ```
 *
 * @receiver Duration in milliseconds
 * @return Formatted time string (e.g., "1:01:05" or "1:05")
 */
fun Long.toReadableDuration(): String {
    val hours = this / 3_600_000
    val minutes = (this % 3_600_000) / 60_000
    val seconds = (this % 60_000) / 1_000
    return if (hours > 0) {
        "%d:%02d:%02d".format(hours, minutes, seconds)
    } else {
        "%d:%02d".format(minutes, seconds)
    }
}

/**
 * Converts minutes to a human-readable format with hours and minutes.
 *
 * Formats as `Xh Ym` if hours > 0, otherwise just `Ym`.
 *
 * ### Examples
 * ```kotlin
 * 65.toReadableMinutes()      // "1h 5m"
 * 30.toReadableMinutes()      // "30m"
 * 125.toReadableMinutes()     // "2h 5m"
 * ```
 *
 * @receiver Duration in minutes
 * @return Formatted duration string (e.g., "1h 5m" or "30m")
 */
fun Int.toReadableMinutes(): String {
    val hours = this / 60
    val minutes = this % 60
    return if (hours > 0) "${hours}h ${minutes}m" else "${minutes}m"
}

// ============================================================================
// File Size Formatting Utilities
// ============================================================================

/**
 * Converts bytes to a human-readable file size string.
 *
 * Automatically selects the appropriate unit (B, KB, MB, GB) based on size.
 * Uses 1 KB = 1024 bytes.
 *
 * ### Examples
 * ```kotlin
 * 500L.toReadableFileSize()             // "500B"
 * 1536L.toReadableFileSize()            // "1.5KB"
 * 5242880L.toReadableFileSize()         // "5.0MB"
 * 1073741824L.toReadableFileSize()      // "1.00GB"
 * ```
 *
 * @receiver File size in bytes
 * @return Formatted size string with unit (e.g., "1.5KB", "5.0MB", "1.00GB")
 */
fun Long.toReadableFileSize(): String = when {
    this < 1024L -> "${this}B"
    this < 1024L * 1024L -> "${"%.1f".format(this / 1024.0)}KB"
    this < 1024L * 1024L * 1024L -> "${"%.1f".format(this / (1024.0 * 1024.0))}MB"
    else -> "${"%.2f".format(this / (1024.0 * 1024.0 * 1024.0))}GB"
}

// ============================================================================
// Percentage Formatting Utilities
// ============================================================================

/**
 * Converts a decimal fraction (0.0 to 1.0) to a percentage string.
 *
 * Multiplies by 100 and appends the `%` symbol. Values are truncated to integers.
 *
 * ### Examples
 * ```kotlin
 * 0.5f.toPercentageString()      // "50%"
 * 0.75f.toPercentageString()     // "75%"
 * 1.0f.toPercentageString()      // "100%"
 * 0.0f.toPercentageString()      // "0%"
 * ```
 *
 * @receiver Decimal fraction (expected range: 0.0 to 1.0)
 * @return Percentage string (e.g., "50%", "75%", "100%")
 */
fun Float.toPercentageString(): String = "${(this * 100).toInt()}%"
