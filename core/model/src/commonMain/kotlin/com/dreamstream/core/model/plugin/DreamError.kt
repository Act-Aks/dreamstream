package com.dreamstream.core.model.plugin

import com.dreamstream.core.domain.util.Error

/**
 * Rich error hierarchy for DreamStream operations.
 *
 * Implements [Error] so it can be used with the typed [com.dreamstream.core.domain.util.Result]
 * wrapper: `Result<T, DreamError>`.
 *
 * Use feature-specific error sealed interfaces (e.g. `HomeError`, `DetailsError`)
 * for user-facing error copy. Use [DreamError] where the operation is shared
 * across features or involves the plugin / provider system.
 */
sealed class DreamError : Error {

    /** A network-level failure. [code] is the HTTP status when available. */
    data class Network(
        val code: Int? = null,
        val message: String? = null,
        val cause: Throwable? = null,
    ) : DreamError()

    /** A provider plugin produced an unexpected error. */
    data class Plugin(
        val pluginId: String,
        val message: String,
        val cause: Throwable? = null,
    ) : DreamError()

    /** A content item resolved successfully but returned no playable sources. */
    data class NoSources(val contentName: String) : DreamError()

    /** A specific extractor could not resolve a stream URL. */
    data class ExtractorFailed(
        val extractorName: String,
        val cause: Throwable? = null,
    ) : DreamError()

    /** The user has no plugins installed. */
    data object NoPluginsInstalled : DreamError()

    /** The requested plugin was not found in the installed set. */
    data object PluginNotFound : DreamError()

    /** A plugin could not be loaded from its artefact file. */
    data class PluginLoadFailed(
        val pluginId: String,
        val cause: Throwable? = null,
    ) : DreamError()

    /** Fetching a plugin repository manifest failed. */
    data class RepositoryFetchFailed(
        val url: String,
        val cause: Throwable? = null,
    ) : DreamError()

    /** The downloaded plugin artefact did not match its declared checksum. */
    data class ChecksumMismatch(val pluginId: String) : DreamError()

    /** An unexpected error with no more specific category. */
    data class Unknown(val cause: Throwable? = null) : DreamError()

    /** Human-readable message suitable for logging. Not for direct UI display — use UiText mappings. */
    val userMessage: String
        get() = when (this) {
            is Network -> "Network error${code?.let { " ($it)" } ?: ""}: ${message ?: "Unknown"}"
            is Plugin -> "Plugin '$pluginId' error: $message"
            is NoSources -> "No sources found for '$contentName'"
            is ExtractorFailed -> "Extractor '$extractorName' failed"
            is NoPluginsInstalled -> "No plugins installed. Add a repository to get started."
            is PluginNotFound -> "Plugin not found"
            is PluginLoadFailed -> "Failed to load plugin '$pluginId'"
            is RepositoryFetchFailed -> "Failed to fetch repository: $url"
            is ChecksumMismatch -> "Plugin '$pluginId' failed checksum verification"
            is Unknown -> "An unknown error occurred"
        }
}
