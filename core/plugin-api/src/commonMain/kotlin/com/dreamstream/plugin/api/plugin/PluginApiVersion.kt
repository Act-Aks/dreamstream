package com.dreamstream.plugin.api.plugin

import com.dreamstream.plugin.api.plugin.PluginApiVersion.CURRENT
import com.dreamstream.plugin.api.plugin.PluginApiVersion.MIN_SUPPORTED
import com.dreamstream.plugin.api.plugin.PluginApiVersion.isCompatible

/**
 * Plugin API version constants for managing compatibility between the app and plugins.
 *
 * This object defines the versioning scheme used to track breaking changes in the plugin API
 * and ensure backward compatibility. Plugins declare their target API version via
 * [PluginMetadata.requiresAppVersion], and the app uses these constants to determine
 * whether a plugin is compatible.
 *
 * ## Version Management Guidelines
 *
 * - Increment [CURRENT] when introducing **breaking API changes** (e.g., removing or modifying
 *   existing APIs, changing method signatures, or altering expected behavior).
 *
 * - Increment [MIN_SUPPORTED] when **dropping support for old plugins** that target earlier
 *   API versions. This should only be done after ensuring users have had adequate time
 *   to update their plugins.
 *
 * ## Compatibility Logic
 *
 * A plugin is considered compatible if its required version falls within the range
 * `[MIN_SUPPORTED, CURRENT]`. This is checked using [isCompatible].
 *
 * ### Example
 * ```kotlin
 * // Check if a plugin requiring API version 1 is compatible
 * if (PluginApiVersion.isCompatible(pluginRequiredVersion = 1)) {
 *     // Load and initialize the plugin
 * } else {
 *     // Reject the plugin with an appropriate error message
 * }
 * ```
 *
 * @see PluginMetadata.requiresAppVersion
 * @see isCompatible
 */
object PluginApiVersion {
    /**
     * The current API version that this app supports.
     *
     * Increment this value when introducing breaking changes to the plugin API.
     * Plugins targeting this version (or lower, down to [MIN_SUPPORTED]) will be compatible.
     *
     * @see CURRENT
     */
    const val CURRENT: Int = 1

    /**
     * The minimum API version this app can load.
     *
     * Plugins targeting versions below this will be rejected as incompatible.
     * Increment this value when dropping support for legacy plugins.
     *
     * @see MIN_SUPPORTED
     */
    const val MIN_SUPPORTED: Int = 1

    /**
     * Checks if a plugin with the given required API version is compatible with this app.
     *
     * A plugin is compatible if its required version falls within the supported range
     * `[MIN_SUPPORTED, CURRENT]`. This ensures the plugin can use APIs available in this app
     * without encountering missing or changed functionality.
     *
     * @param pluginRequiredVersion The API version the plugin requires (declared via
     *                              [PluginMetadata.requiresAppVersion])
     * @return `true` if the plugin is compatible; `false` otherwise
     *
     * ### Example
     * ```kotlin
     * when {
     *     PluginApiVersion.isCompatible(1) -> true  // 1 is in range 1..1
     *     PluginApiVersion.isCompatible(2) -> false // 2 is above CURRENT (1)
     *     PluginApiVersion.isCompatible(0) -> false // 0 is below MIN_SUPPORTED (1)
     * }
     * ```
     */
    fun isCompatible(pluginRequiredVersion: Int): Boolean =
        pluginRequiredVersion in MIN_SUPPORTED..CURRENT
}
