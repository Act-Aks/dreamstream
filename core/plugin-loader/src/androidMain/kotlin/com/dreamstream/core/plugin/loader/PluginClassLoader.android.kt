package com.dreamstream.core.plugin.loader

import android.content.Context
import com.dreamstream.plugin.api.plugin.DreamPlugin
import dalvik.system.DexClassLoader
import okio.Path
import java.util.concurrent.ConcurrentHashMap

actual class PluginClassLoader(private val context: Context) {

    /**
     * Cache classloaders by plugin file path string.
     * Callers must use the same key when calling [release].
     */
    private val loaders = ConcurrentHashMap<String, DexClassLoader>()

    actual fun load(pluginFile: Path, mainClassName: String): DreamPlugin {
        val file = pluginFile.toFile()
        require(file.exists()) { "Plugin file not found: $pluginFile" }

        val optimizedDir = context.codeCacheDir.absolutePath

        val classLoader = DexClassLoader(
            file.absolutePath,
            optimizedDir,
            null,
            context.classLoader,
        )

        LoaderLogger.d { "Loading plugin class: $mainClassName from $file" }

        val clazz = classLoader.loadClass(mainClassName)
        val plugin = clazz.getDeclaredConstructor().newInstance() as DreamPlugin

        // Cache classloader keyed by file path for later release
        val key = pluginFile.toString()
        loaders[key] = classLoader
        LoaderLogger.v { "Cached classloader for key=$key" }

        return plugin
    }

    actual fun release(pluginIdOrKey: String) {
        loaders.remove(pluginIdOrKey)?.let {
            LoaderLogger.d { "Released classloader for key=$pluginIdOrKey" }
        } ?: run {
            LoaderLogger.v { "No classloader found for key=$pluginIdOrKey" }
        }
    }
}
