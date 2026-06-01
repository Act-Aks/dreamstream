package com.dreamstream.plugin.api.plugin

import com.dreamstream.plugin.api.extractor.Extractor
import com.dreamstream.plugin.api.provider.ContentProvider

/**
 * Entry point for all DreamStream plugins.
 *
 * Plugin authors must:
 * 1. Create a class extending [DreamPlugin]
 * 2. Annotate it with [PluginMetadata]
 * 3. Register providers and/or extractors
 *
 * Example:
 * ```kotlin
 * @PluginMetadata(
 *     id = "com.example.myplugin",
 *     name = "My Plugin",
 *     version = 1,
 *     versionName = "1.0.0",
 *     description = "My awesome plugin",
 *     authors = ["YourName"]
 * )
 * class MyPlugin : DreamPlugin() {
 *     override fun registerProviders() = listOf(MyProvider())
 * }
 * ```
 */
abstract class DreamPlugin {
    protected lateinit var context: PluginContext
        private set

    /** Called by host app after instantiation. Do NOT call directly. */
    fun initialize(context: PluginContext) {
        this.context = context
        onLoad()
    }

    /** Override to perform initialization logic after context is available */
    open fun onLoad() {}

    /** Override to perform cleanup before plugin is unloaded */
    open fun onUnload() {}

    /** Return all content providers this plugin provides */
    abstract fun registerProviders(): List<ContentProvider>

    /** Return all video extractors this plugin provides */
    open fun registerExtractors(): List<Extractor> = emptyList()
}
