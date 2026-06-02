package com.dreamstream.core.model.plugin

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Metadata describing a provider plugin available in a repository.
 *
 * [PluginManifest] is the declarative description of a plugin published in a
 * ***[PluginRepository]*** manifest. It is downloaded before installation and
 * used to display plugin details, verify integrity, and determine compatibility.
 *
 * This class contains:
 * - **Identity**: [id], [name], [version], [versionName], [authors]
 * - **Description**: [description], [iconUrl], [language], [changelog]
 * - **Download**: [url], [fileSize], [sha256] (integrity check)
 * - **Filtering**: [contentTypes], [isAdult], [status], [requiresAppVersion]
 * - **Source**: [repositoryUrl], [repositoryName]
 *
 * ## Key Properties:
 * - [id]: Stable plugin identifier (***required, unique***)
 * - [name]: Display name (***required***)
 * - [version]: Semantic version number as Int (***required***)
 * - [versionName]: Human-readable version string (***required***)
 * - [url]: Download URL (***required***)
 * - [sha256]: SHA-256 checksum for verification (***required***)
 * - [isAdult]: Parental control flag (***optional, defaults to false***)
 * - [status]: Availability status (***optional, defaults to [PluginStatus.Available]***)
 *
 * ## Identity & Versioning:
 * - [id]: Stable identifier used for installation tracking and updates.
 *   Never changes across versions (e.g., `"anime-provider"`).
 * - [version]: Integer version (e.g., `1`, `2`, `10`) for comparison.
 * - [versionName]: Human-readable version (e.g., `"1.0.0"`, `"2.1-beta"`).
 * - [requiresAppVersion]: Minimum app version ID required (defaults to `1`).
 *
 * ## Integrity Verification:
 * [sha256] is used to verify the downloaded plugin file **before loading**:
 * ```kotlin
 * val downloadedHash = sha256OfFile(plugin.filePath)
 * if (downloadedHash != manifest.sha256) {
 *     throw DreamError.ChecksumMismatch(manifest.id)
 * }
 * ```
 * [fileSize] is optional metadata for progress tracking (defaults to `0L`).
 *
 * ## Content Filtering:
 * - [contentTypes]: List of [ContentType]s the plugin supports (e.g., `ANIME`, `MOVIE`).
 *   Empty list means "all types".
 * - [isAdult]: Signals that **parental controls** should gate access to this plugin.
 *   If `true`, the plugin is hidden when adult content is disabled.
 * - [language]: Plugin UI/content language (defaults to `"en"`).
 *
 * ## Status & Availability:
 * [status] indicates the plugin's current state, matching the actual [PluginStatus] enum:
 * | Status | Meaning |
 * |--------|---------|
 * | [PluginStatus.Available] | Normal, installable, compatible |
 * | [PluginStatus.Installed] | Currently installed and up-to-date |
 * | [PluginStatus.UpdateAvailable] | Newer version exists in repository |
 * | [PluginStatus.Incompatible] | App version too old or requirements not met |
 * | [PluginStatus.Broken] | Failed to load or corrupted |
 *
 * ## UI Representation:
 * - Displayed in the **Plugin Store** screen
 * - [name] shown as title
 * - [versionName] shown as subtitle (e.g., "v1.2.0")
 * - [description] shown in details sheet
 * - [iconUrl] displayed as plugin icon (if provided)
 * - [authors] shown as "By: Author1, Author2"
 * - [isAdult] marked with an "18+" badge
 * - [status] affects visibility and button state (e.g., hide [PluginStatus.Incompatible] plugins when filtered)
 *
 * ## Usage:
 * ```kotlin
 * val manifest = PluginManifest(
 *     id = "anime-provider",
 *     name = "Anime Provider",
 *     version = 2,
 *     versionName = "2.0.0",
 *     description = "Watch anime from multiple sources",
 *     authors = listOf("John Doe"),
 *     iconUrl = "https://repo.example.com/icons/anime-provider.png",
 *     contentTypes = listOf(ContentType.ANIME),
 *     url = "https://repo.example.com/plugins/anime-provider-v2.jar",
 *     repositoryUrl = "https://repo.example.com/manifest.json",
 *     sha256 = "abc123...",
 *     fileSize = 1_500_000L,
 *     isAdult = false,
 *     status = PluginStatus.Available
 * )
 *
 * if (manifest.status == PluginStatus.Available) {
 *     pluginManager.install(manifest)
 * }
 * ```
 *
 * ## Installation Flow:
 * 1. Fetch repository manifest → list of [PluginManifest]
 * 2. User selects plugin → download from [url]
 * 3. Verify [sha256] checksum
 * 4. Save as [InstalledPlugin] with [filePath]
 * 5. Load ***`ContentProvider`*** via [id]
 *
 * ## Update Check:
 * ```kotlin
 * val installed = pluginManager.getInstalledPlugin(manifest.id)
 * if (installed != null && installed.manifest.version < manifest.version) {
 *     showUpdateAvailable(manifest)
 * }
 * ```
 *
 * ## Related:
 * - Installed plugin: [InstalledPlugin]
 * - Repository: [PluginRepository]
 * - Manager: ***`PluginManager`***
 * - Provider: ***`ContentProvider`***
 * - Status enum: [PluginStatus]
 * - Content type: [ContentType]
 * - Error: [com.dreamstream.core.domain.util.DreamError.ChecksumMismatch]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class PluginManifest(
    /**
     * Stable plugin identifier.
     *
     * ***Required, unique***. Never changes across versions. Used for:
     * - Installation tracking
     * - Update detection
     * - Provider loading (`pluginManager.loadProvider(id)`)
     *
     * Example: `"anime-provider"`, `"movies-provider"`
     */
    val id: String,

    /**
     * Human-readable plugin name.
     *
     * ***Required***. Displayed in the plugin store and settings.
     * Example: `"Anime Provider"`, `"Crunchyroll Plugin"`
     */
    val name: String,

    /**
     * Semantic version number (integer).
     *
     * ***Required***. Used for version comparison (e.g., `1 < 2`).
     * Incremented on every release. Example: `1`, `2`, `10`.
     */
    val version: Int,

    /**
     * Human-readable version string.
     *
     * ***Required***. Displayed in UI (e.g., "v1.0.0", "2.1-beta").
     * Example: `"1.0.0"`, `"2.1.0-beta.3"`
     */
    val versionName: String,

    /**
     * Plugin description.
     *
     * ***Required***. Brief description shown in the plugin store.
     * Example: `"Watch anime from multiple sources"`
     */
    val description: String,

    /**
     * Plugin author(s).
     *
     * ***Optional***. Defaults to empty list. Displayed as "By: Author1, Author2".
     * Example: `listOf("John Doe", "Jane Smith")`
     */
    val authors: List<String> = emptyList(),

    /**
     * Icon URL for the plugin.
     *
     * ***Optional***. Null if no icon. Downloaded and displayed in the plugin store.
     * Example: `"https://repo.example.com/icons/anime-provider.png"`
     */
    val iconUrl: String? = null,

    /**
     * Plugin UI/content language.
     *
     * ***Optional***. Defaults to `"en"` (English). BCP 47 tag.
     * Example: `"en"`, `"es"`, `"ja"`, `"pt-BR"`
     */
    val language: String = "en",

    /**
     * Content types supported by this plugin.
     *
     * ***Optional***. Defaults to empty list (means "all types").
     * Filter plugins by type in the plugin store (e.g., show only ANIME plugins).
     * Example: `listOf(ContentType.ANIME)`, `listOf(ContentType.MOVIE, ContentType.SERIES)`
     */
    val contentTypes: List<ContentType> = emptyList(),

    /**
     * Download URL for the plugin artifact.
     *
     * ***Required***. Direct link to the `.jar` or plugin file.
     * Used to download the plugin during installation.
     * Example: `"https://repo.example.com/plugins/anime-provider-v2.jar"`
     */
    val url: String,

    /**
     * Repository URL where this plugin was published.
     *
     * ***Required***. URL of the plugin repository manifest.
     * Used to track which repository the plugin came from.
     * Example: `"https://repo.example.com/manifest.json"`
     */
    val repositoryUrl: String,

    /**
     * Plugin artifact file size in bytes.
     *
     * ***Optional***. Defaults to `0L`. Used for progress tracking during download.
     * Example: `1_500_000L` (1.5 MB)
     */
    val fileSize: Long = 0L,

    /**
     * SHA-256 checksum of the plugin artifact.
     *
     * ***Required***. Used to verify integrity **after download** and **before loading**.
     * If mismatched, throw [com.dreamstream.core.domain.util.DreamError.ChecksumMismatch].
     * Example: `"abc123def456..."` (64-character hex string)
     */
    val sha256: String = "",

    /**
     * Minimum app version ID required to install this plugin.
     *
     * ***Optional***. Defaults to `1`. If app version < this value, show incompatibility error.
     * Example: `requiresAppVersion = 2` means app v2.0.0+ is required
     */
    val requiresAppVersion: Int = 1,

    /**
     * Changelog for this version.
     *
     * ***Optional***. Null if no changelog. Shown in update dialog.
     * Example: `"Bug fixes:\n- Fixed crash on episode 5\n- Added new source"`
     */
    val changelog: String? = null,

    /**
     * Repository name (human-readable).
     *
     * ***Optional***. Null if not provided. Displayed in plugin details.
     * Example: `"Official Repository"`, `"Community Plugins"`
     */
    val repositoryName: String? = null,

    /**
     * Whether this plugin contains adult content.
     *
     * ***Optional***. Defaults to `false`. If `true`, plugin is hidden when
     * parental controls are enabled (adult content gated).
     * Displayed with an "18+" badge in the plugin store.
     */
    val isAdult: Boolean = false,

    /**
     * Plugin availability status.
     *
     * ***Optional***. Defaults to [PluginStatus.Available]. Controls visibility
     * and button state in the plugin store (e.g., show "Install" for [PluginStatus.Available],
     * "Update" for [PluginStatus.UpdateAvailable], gray out for [PluginStatus.Incompatible]).
     */
    val status: PluginStatus = PluginStatus.Available,
)
