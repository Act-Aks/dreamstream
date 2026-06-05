package com.dreamstream.core.plugin.loader

import com.dreamstream.plugin.api.plugin.DreamPlugin
import okio.Path
import java.net.URLClassLoader
import java.util.concurrent.ConcurrentHashMap

actual class PluginClassLoader {

    /**
     * Cache classloaders by plugin file path string.
     * Callers must use the same key when calling [release].
     */
    private val loaders = ConcurrentHashMap<String, URLClassLoader>()

    actual fun load(pluginFile: Path, mainClassName: String): DreamPlugin {
        val file = java.io.File(pluginFile.toString())
        require(file.exists()) { "Plugin file not found: $pluginFile" }

        val classLoader = URLClassLoader(
            arrayOf(file.toURI().toURL()),
            this::class.java.classLoader,
        )

        LoaderLogger.d { "Loading plugin class: $mainClassName from $file" }

        val clazz = classLoader.loadClass(mainClassName)
        val plugin = clazz.getDeclaredConstructor().newInstance() as DreamPlugin

        val key = pluginFile.toString()
        loaders[key] = classLoader
        LoaderLogger.v { "Cached classloader for key=$key" }

        return plugin
    }

    actual fun release(pluginIdOrKey: String) {
        loaders.remove(pluginIdOrKey)?.let { loader ->
            try {
                loader.close()
                LoaderLogger.d { "Released and closed classloader for key=$pluginIdOrKey" }
            } catch (t: Throwable) {
                LoaderLogger.e(t) { "Error closing classloader for key=$pluginIdOrKey" }
            }
        } ?: run {
            LoaderLogger.v { "No classloader found for key=$pluginIdOrKey" }
        }
    }
}
