package com.dreamstream.core.model.plugin

import kotlinx.serialization.Serializable

/**
 * Availability status of a plugin in the repository or on the device.
 *
 * [PluginStatus] indicates the current state of a plugin for display in the
 * **Plugin Store** and **Plugin Settings** screens. It is used in [PluginManifest.status]
 * and computed for installed plugins to determine update availability and compatibility.
 *
 * ## Status Values:
 * | Status | Meaning | UI Indicator | Action |
 * |--------|---------|--------------|--------|
 * | [Available] | Plugin is downloadable and compatible | "Install" button | Install plugin |
 * | [Installed] | Plugin is currently installed and up-to-date | "Installed" badge | Open settings |
 * | [UpdateAvailable] | Newer version exists in repository | "Update" button | Update plugin |
 * | [Incompatible] | App version too old or requirements not met | "Incompatible" badge + grayed out | Upgrade app |
 * | [Broken] | Plugin failed to load or is corrupted | "Broken" badge + error message | Reinstall/Remove |
 *
 * ## Usage in PluginManifest:
 * ```kotlin
 * val manifest = PluginManifest(
 *     id = "anime-provider",
 *     name = "Anime Provider",
 *     status = PluginStatus.Available
 * )
 *
 * when (manifest.status) {
 *     PluginStatus.Available -> showInstallButton()
 *     PluginStatus.Incompatible -> showCompatibilityError()
 *     PluginStatus.Broken -> showBrokenPluginWarning()
 *     else -> hideButton()
 * }
 * ```
 *
 * ## Computing Status for Installed Plugins:
 * ```kotlin
 * fun getStatus(installed: InstalledPlugin, repoManifest: PluginManifest?): PluginStatus {
 *     if (repoManifest == null) return PluginStatus.Installed
 *
 *     return when {
 *         installed.manifest.version < repoManifest.version -> PluginStatus.UpdateAvailable
 *         !installed.isEnabled -> PluginStatus.Incompatible // or custom state
 *         else -> PluginStatus.Installed
 *     }
 * }
 *
 * // After loading attempt:
 * if (loadAttemptFailed) {
 *     PluginStatus.Broken
 * }
 * ```
 *
 * ## UI Representation:
 * - **Plugin Store**:
 *   - [Available]: Show "Install" button, normal opacity
 *   - [Installed]: Show "Installed" badge, disable install button
 *   - [UpdateAvailable]: Show "Update" button, highlight with new badge
 *   - [Incompatible]: Gray out plugin card, show "Requires app vX.X+"
 *   - [Broken]: Show warning icon, display error message
 *
 * - **Plugin Settings**:
 *   - [Installed]: Show toggle switch, version number
 *   - [UpdateAvailable]: Show "Update available" banner
 *   - [Broken]: Show "Failed to load" error with "Reinstall" option
 *
 * ## Related:
 * - Manifest: [PluginManifest]
 * - Installed plugin: [InstalledPlugin]
 * - Error: [com.dreamstream.core.domain.util.DreamError.PluginLoadFailed], [com.dreamstream.core.domain.util.DreamError.ChecksumMismatch]
 * - Manager: ***`PluginManager`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
enum class PluginStatus {
    /**
     * Plugin is available for installation and compatible with the current app.
     *
     * ***Meaning***: Plugin is in the repository, meets all requirements
     * ([PluginManifest.requiresAppVersion], content filters, etc.), and is not yet installed.
     *
     * ***UI Action***: Show "Install" button.
     */
    Available,

    /**
     * Plugin is currently installed and up-to-date.
     *
     * ***Meaning***: Installed version matches the latest repository version.
     * Plugin is functioning correctly (no load errors).
     *
     * ***UI Action***: Show "Installed" badge, open settings on tap.
     */
    Installed,

    /**
     * A newer version of the plugin is available in the repository.
     *
     * ***Meaning***: Installed [InstalledPlugin.manifest.version] < repository [PluginManifest.version].
     * User should update to get new features/bug fixes.
     *
     * ***UI Action***: Show "Update" button, highlight with "New" badge.
     */
    UpdateAvailable,

    /**
     * Plugin is incompatible with the current app version or environment.
     *
     * ***Meaning***: App version < [PluginManifest.requiresAppVersion], or platform
     * requirements not met (e.g., architecture, OS version).
     *
     * ***UI Action***: Gray out plugin card, show "Requires app vX.X+" message.
     */
    Incompatible,

    /**
     * Plugin failed to load or is corrupted.
     *
     * ***Meaning***: Plugin artifact exists but loading threw an exception
     * ([com.dreamstream.core.domain.util.DreamError.PluginLoadFailed]), checksum failed ([com.dreamstream.core.domain.util.DreamError.ChecksumMismatch]),
     * or DSL parsing error. Plugin is non-functional.
     *
     * ***UI Action***: Show warning icon, display error message, offer "Reinstall" or "Remove".
     */
    Broken,
}
