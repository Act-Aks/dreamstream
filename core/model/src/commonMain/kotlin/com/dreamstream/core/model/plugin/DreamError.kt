package com.dreamstream.core.model.plugin

import com.dreamstream.core.domain.util.Error

/**
 * Rich error hierarchy for DreamStream operations.
 *
 * [DreamError] is the central error type for plugin and provider-related failures
 * in DreamStream. It implements [Error] so it can be used with the typed
 * ***`com.dreamstream.core.domain.util.Result`*** wrapper: `Result<T, DreamError>`.
 *
 * This sealed class categorizes errors into:
 * - **Network failures**: [Network] (HTTP errors, timeouts)
 * - **Plugin failures**: [Plugin], [PluginNotFound], [NoPluginsInstalled], [PluginLoadFailed]
 * - **Content failures**: [NoSources], [ExtractorFailed]
 * - **Repository failures**: [RepositoryFetchFailed], [ChecksumMismatch]
 * - **Fallback**: [Unknown]
 *
 * ## Key Properties:
 * - [userMessage]: Human-readable log message (***computed***)
 *
 * ## Error Categories:
 * | Type | Use Case | Example |
 * |------|----------|---------|
 * | [Network] | HTTP/API failure | `503 Service Unavailable` |
 * | [Plugin] | Provider plugin error | `"AnimeProvider: Token expired"` |
 * | [NoSources] | Content resolved but no streams | `"No sources for 'Attack on Titan'"` |
 * | [ExtractorFailed] | Stream extraction failed | `"Kusosalver failed"` |
 * | [NoPluginsInstalled] | No plugins installed | `"Add a repository"` |
 * | [PluginNotFound] | Plugin missing | `"Plugin 'x' not found"` |
 * | [PluginLoadFailed] | Artifact load failure | `"Failed to load 'y'"` |
 * | [RepositoryFetchFailed] | Manifest fetch failed | `"Repo URL unreachable"` |
 * | [ChecksumMismatch] | Integrity check failed | `"Checksum mismatch for 'z'"` |
 * | [Unknown] | Unhandled error | `"Unknown error"` |
 *
 * ## Usage:
 * ```kotlin
 * val result: Result<StreamLink, DreamError> = provider.loadLinks(dataUrl)
 *
 * result.onFailure { error ->
 *     when (error) {
 *         is DreamError.Network -> Log.e("Network", error.userMessage)
 *         is DreamError.NoSources -> showSnackbar("No streams found")
 *         is DreamError.NoPluginsInstalled -> navigateToPluginStore()
 *     }
 * }
 * ```
 *
 * ## UI Text Mapping:
 * [userMessage] is suitable for **logging**, not direct UI display.
 * For user-facing messages, use a `UiText` mapping:
 * ```kotlin
 * fun toUiText(error: DreamError): UiText = when (error) {
 *     is DreamError.Network -> UiString.Resource(R.string.error_network)
 *     is DreamError.NoSources -> UiString.Resource(R.string.error_no_sources, error.contentName)
 *     is DreamError.NoPluginsInstalled -> UiString.Resource(R.string.error_no_plugins)
 *     // ...
 * }
 * ```
 *
 * ## Feature-Specific Errors:
 * Use feature-specific error sealed interfaces (e.g., `HomeError`, `DetailsError`)
 * for user-facing error copy. Use [DreamError] where the operation is shared
 * across features or involves the plugin/provider system.
 *
 * ## Related:
 * - Error wrapper: ***`com.dreamstream.core.domain.util.Result`***
 * - Base interface: [Error]
 * - UI text: `UiText`, `UiString`
 * - Plugin system: ***`ContentProvider`***, ***`PluginManager`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
sealed class DreamError : Error {

    /**
     * Network-level failure.
     *
     * ***Cause***: HTTP errors, timeouts, DNS failures, no internet.
     * [code] is the HTTP status when available (e.g., `404`, `503`).
     * [message] is the error description from the server (optional).
     * [cause] is the underlying exception (optional).
     *
     * Example:
     * ```kotlin
     * DreamError.Network(code = 503, message = "Service Unavailable")
     * ```
     */
    data class Network(
        /** HTTP status code (e.g., 404, 503). Null if unavailable. */
        val code: Int? = null,
        /** Error message from server. Null if unavailable. */
        val message: String? = null,
        /** Underlying exception. Null if unavailable. */
        val cause: Throwable? = null,
    ) : DreamError()

    /**
     * Provider plugin produced an unexpected error.
     *
     * ***Cause***: Plugin logic failure, token expiration, API change.
     * [pluginId] identifies the failing plugin. [message] describes the error.
     * [cause] is the underlying exception (optional).
     *
     * Example:
     * ```kotlin
     * DreamError.Plugin(pluginId = "AnimeProvider", message = "Token expired")
     * ```
     */
    data class Plugin(
        /** ID of the plugin that produced the error. */
        val pluginId: String,
        /** Error message from the plugin. */
        val message: String,
        /** Underlying exception. Null if unavailable. */
        val cause: Throwable? = null,
    ) : DreamError()

    /**
     * Content resolved successfully but returned no playable sources.
     *
     * ***Cause***: Content removed, geo-blocked, or provider returned empty list.
     * [contentName] is the title that was searched.
     *
     * Example:
     * ```kotlin
     * DreamError.NoSources(contentName = "Attack on Titan")
     * ```
     */
    data class NoSources(
        /** Title of the content with no sources. */
        val contentName: String,
    ) : DreamError()

    /**
     * A specific extractor could not resolve a stream URL.
     *
     * ***Cause***: Extractor logic failure, changed website structure,反诈 protection.
     * [extractorName] identifies the failing extractor. [cause] is the exception.
     *
     * Example:
     * ```kotlin
     * DreamError.ExtractorFailed(extractorName = "Kusosalver")
     * ```
     */
    data class ExtractorFailed(
        /** Name of the extractor that failed. */
        val extractorName: String,
        /** Underlying exception. Null if unavailable. */
        val cause: Throwable? = null,
    ) : DreamError()

    /**
     * No plugins are installed.
     *
     * ***Cause***: User hasn't installed any plugin repositories yet.
     * Action: Navigate to plugin store or repository management screen.
     */
    data object NoPluginsInstalled : DreamError()

    /**
     * Requested plugin not found in installed set.
     *
     * ***Cause***: Plugin was uninstalled, repository changed, or typo in plugin ID.
     */
    data object PluginNotFound : DreamError()

    /**
     * Plugin could not be loaded from its artifact file.
     *
     * ***Cause***: Corrupted file, incompatible version, DSL parsing error.
     * [pluginId] identifies the failing plugin. [cause] is the exception.
     */
    data class PluginLoadFailed(
        /** ID of the plugin that failed to load. */
        val pluginId: String,
        /** Underlying exception. Null if unavailable. */
        val cause: Throwable? = null,
    ) : DreamError()

    /**
     * Fetching a plugin repository manifest failed.
     *
     * ***Cause***: Network error, invalid URL, server down, manifest parse error.
     * [url] is the repository manifest URL. [cause] is the exception.
     */
    data class RepositoryFetchFailed(
        /** Repository manifest URL that failed. */
        val url: String,
        /** Underlying exception. Null if unavailable. */
        val cause: Throwable? = null,
    ) : DreamError()

    /**
     * Downloaded plugin artifact did not match declared checksum.
     *
     * ***Cause***: Integrity check failed (potential tampering, corrupted download).
     * [pluginId] identifies the failing plugin. Reject the plugin.
     */
    data class ChecksumMismatch(
        /** ID of the plugin with checksum mismatch. */
        val pluginId: String,
    ) : DreamError()

    /**
     * Unexpected error with no more specific category.
     *
     * ***Cause***: Unhandled exception in plugin/system code.
     * [cause] is the underlying exception (optional).
     */
    data class Unknown(
        /** Underlying exception. Null if unavailable. */
        val cause: Throwable? = null,
    ) : DreamError()

    /**
     * Human-readable message suitable for logging.
     *
     * ***Computed***. Generates a concise error description for logs/debugging.
     * **Not for direct UI display** — use `UiText` mappings instead.
     *
     * Examples:
     * ```kotlin
     * Network(503, "Service Unavailable")       → "Network error (503): Service Unavailable"
     * Plugin("AnimeProvider", "Token expired")  → "Plugin 'AnimeProvider' error: Token expired"
     * NoSources("Attack on Titan")              → "No sources found for 'Attack on Titan'"
     * NoPluginsInstalled                        → "No plugins installed. Add a repository..."
     * ```
     */
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
