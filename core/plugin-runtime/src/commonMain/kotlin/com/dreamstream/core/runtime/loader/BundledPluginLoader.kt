package com.dreamstream.core.runtime.loader

import com.dreamstream.plugin.api.plugin.DreamPlugin

/**
 * [PluginLoader] that wraps a pre-constructed list of [DreamPlugin] instances.
 *
 * Use this for plugins that ship as Gradle sub-modules (`:plugin:flixhq`, etc.).
 * The plugins are already on the classpath and need no dynamic loading.
 *
 * Example:
 * ```kotlin
 * BundledPluginLoader(listOf(FlixHqPlugin()))
 * ```
 */
class BundledPluginLoader(private val plugins: List<DreamPlugin>) : PluginLoader {
    override suspend fun load(): List<DreamPlugin> = plugins
}
