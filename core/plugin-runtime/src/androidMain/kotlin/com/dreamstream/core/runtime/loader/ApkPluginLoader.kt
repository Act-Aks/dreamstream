package com.dreamstream.core.runtime.loader

import android.content.Context
import com.dreamstream.plugin.api.plugin.DreamPlugin
import java.io.File

/**
 * [PluginLoader] that discovers and loads DreamStream plugins packaged as APK files.
 *
 * APKs are expected to reside under [pluginsDir] with a `.apk` extension.
 * Each APK must contain a class named in `META-INF/dreamstream-plugin.properties`
 * under the key `entryPoint` (e.g. `entryPoint=com.example.MyPlugin`).
 *
 * **Current status**: APK-based plugin loading is scaffolded but not yet implemented.
 * The method returns an empty list until the DexClassLoader integration is complete in
 * a future release.
 *
 * @param pluginsDir Directory scanned for `.apk` plugin files.
 *   Typically `context.filesDir.resolve("plugins")`.
 * @param context Android [Context] used for [dalvik.system.DexClassLoader] initialisation.
 */
class ApkPluginLoader(
    private val pluginsDir: File,
    private val context: Context,
) : PluginLoader {

    override suspend fun load(): List<DreamPlugin> {
        // APK-based plugin loading will be implemented in a future release.
        // Bundled Gradle-module plugins (BundledPluginLoader) are the primary mechanism
        // during the current development phase.
        return emptyList()
    }
}
