package com.dreamstream.core.runtime.loader

import com.dreamstream.plugin.api.plugin.DreamPlugin

/**
 * Loads [DreamPlugin] instances from a source.
 *
 * Implementations:
 * - [BundledPluginLoader] — wraps a pre-constructed list; used for Gradle module plugins.
 * - `ApkPluginLoader` (androidMain) — discovers and loads APK-packaged plugins at runtime.
 */
interface PluginLoader {
    /** Returns all plugins provided by this loader. May suspend for disk I/O (APK loaders). */
    suspend fun load(): List<DreamPlugin>
}
