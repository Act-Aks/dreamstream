package com.dreamstream.core.domain.model.plugin

import kotlinx.serialization.Serializable

/**
 * A plugin that has been installed on this device.
 *
 * [InstalledPlugin] represents a provider plugin that has been downloaded,
 * verified, and installed in DreamStream. It is managed by ***`PluginManager`***
 * and used to load ***`ContentProvider`*** instances for content discovery.
 *
 * This class contains:
 * - **Metadata**: [manifest] (plugin info, version, repository)
 * - **Storage**: [filePath] (absolute path to artifact)
 * - **State**: [isEnabled] (active/inactive), [installedAt], [updatedAt]
 *
 * ## Key Properties:
 * - [manifest]: Plugin metadata (***required***)
 * - [filePath]: Artifact path (***required***)
 * - [isEnabled]: Active state (***optional, defaults to true***)
 * - [installedAt]: Install timestamp (***optional***)
 * - [updatedAt]: Update timestamp (***optional***)
 *
 * ## Timestamps:
 * [installedAt] and [updatedAt] are Unix epoch timestamps in **milliseconds**:
 * - `0L` = not set (default)
 * - Example: `1716945600000` = May 29, 2024 00:00:00 UTC
 * ```kotlin
 * val installedDate = Instant.ofEpochMilli(plugin.installedAt)
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val plugin = InstalledPlugin(
 *     manifest = PluginManifest(
 *         id = "anime-provider",
 *         name = "Anime Provider",
 *         version = "1.2.0"
 *     ),
 *     filePath = "/data/data/com.dreamstream/plugins/anime-provider.jar",
 *     isEnabled = true,
 *     installedAt = 1716945600000L,
 *     updatedAt = 1716945600000L
 * )
 *
 * if (plugin.isEnabled) {
 *     pluginManager.loadProvider(plugin.manifest.id)
 * }
 * ```
 *
 * ## In Plugin Manager:
 * ```kotlin
 * val plugins = pluginManager.getInstalledPlugins()
 * val enabledCount = plugins.count { it.isEnabled }
 *
 * plugins.forEach { plugin ->
 *     LoadProviderJob.enqueue(plugin.manifest.id, plugin.filePath)
 * }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the **Plugin Settings** screen
 * - [manifest.name] shown as plugin title
 * - [manifest.version] shown as subtitle (e.g., "v1.2.0")
 * - [isEnabled] controls the toggle switch
 * - [filePath] shown in "Details" (for debugging)
 * - [updatedAt] shown as "Last updated: May 29, 2024"
 *
 * ## Related:
 * - Metadata: [PluginManifest]
 * - Manager: ***`PluginManager`***
 * - Provider: ***`ContentProvider`***
 * - Repository: [PluginRepository]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class InstalledPlugin(
    /**
     * Plugin metadata (ID, name, version, repository, etc.).
     *
     * ***Required***. Contains all declarative information about the plugin.
     * Used for display, version checking, and provider loading.
     */
    val manifest: PluginManifest,

    /**
     * Absolute path to the installed plugin artifact file.
     *
     * ***Required***. File system path to the `.jar` or plugin artifact
     * (e.g., `/data/data/com.dreamstream/plugins/anime-provider.jar`).
     * Used by ***`PluginManager`*** to load the plugin classloader.
     */
    val filePath: String,

    /**
     * Whether the plugin is currently enabled.
     *
     * ***Optional***. Defaults to `true` (enabled).
     * Set to `false` to temporarily disable a plugin without uninstalling.
     * Disabled plugins are skipped during content loading.
     */
    val isEnabled: Boolean = true,

    /**
     * Installation timestamp (Unix epoch, milliseconds).
     *
     * ***Optional***. Defaults to `0L` (not set).
     * Example: `1716945600000L` = May 29, 2024 00:00:00 UTC.
     * Used to display "Installed on: ..." in plugin details.
     */
    val installedAt: Long = 0L,

    /**
     * Last update timestamp (Unix epoch, milliseconds).
     *
     * ***Optional***. Defaults to `0L` (not set).
     * Same format as [installedAt]. Updated when the plugin is upgraded.
     * Used to display "Last updated: ..." and check for new versions.
     */
    val updatedAt: Long = 0L,
)
