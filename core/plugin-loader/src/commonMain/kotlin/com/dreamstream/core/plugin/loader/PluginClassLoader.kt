package com.dreamstream.core.plugin.loader

import co.touchlab.kermit.Logger
import com.dreamstream.plugin.api.plugin.DreamPlugin
import okio.Path

/**
 * Platform-specific plugin class loader.
 * Android uses DexClassLoader, Desktop uses URLClassLoader.
 */
expect class PluginClassLoader {

    /**
     * Load a [DreamPlugin] from the given file.
     * @param pluginFile Path to the [PLUGIN_FILE_EXTENSION] plugin file
     * @param mainClassName Fully qualified name of the DreamPlugin subclass
     * @return Instantiated but uninitialized [DreamPlugin]
     */
    fun load(pluginFile: Path, mainClassName: String): DreamPlugin

    /**
     * Release resources for the plugin with [pluginIdOrKey].
     *
     * The key must match what the platform implementation used when caching
     * the classloader (by default, the plugin file path string).
     */
    fun release(pluginIdOrKey: String)
}

internal val LoaderLogger = Logger.withTag("PluginClassLoader")
