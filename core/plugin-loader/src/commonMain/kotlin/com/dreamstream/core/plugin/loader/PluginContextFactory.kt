package com.dreamstream.core.plugin.loader

import com.dreamstream.core.domain.extensions.debug
import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.extensions.info
import com.dreamstream.core.domain.extensions.verbose
import com.dreamstream.core.domain.extensions.warn
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.plugin.api.plugin.LogLevel
import com.dreamstream.plugin.api.plugin.PluginContext
import io.ktor.client.HttpClient
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath


class PluginContextFactory(
    private val httpClient: HttpClient,
    private val apiVersion: Int,
    private val storageProvider: AppStorageProvider,
    private val loggerFactory: LoggerFactory,
) {
    private val fileSystem = storageProvider.fileSystem
    private val logger = loggerFactory.get("PluginContextFactory")

    fun create(pluginId: String): PluginContext {
        val pluginDir: Path = "${storageProvider.appStorageDir}/plugin_data/$pluginId".toPath()

        fileSystem.createDirectories(pluginDir)

        logger.debug { "Created plugin context for: $pluginId at $pluginDir" }

        return PluginContextImpl(
            pluginId = pluginId,
            httpClient = httpClient,
            storageDir = pluginDir.toString(),
            apiVersion = apiVersion,
            fileSystem = fileSystem,
            loggerFactory = loggerFactory,
        )
    }
}

private class PluginContextImpl(
    private val pluginId: String,
    override val httpClient: HttpClient,
    override val storageDir: String,
    override val apiVersion: Int,
    private val fileSystem: FileSystem,
    loggerFactory: LoggerFactory
) : PluginContext {

    private val logger = loggerFactory.get("Plugin[$pluginId]")
    private val prefFile: Path = "$storageDir/prefs.properties".toPath()
    private val prefs = mutableMapOf<String, String>()

    init {
        loadPrefs()
    }

    override fun getString(key: String): String? = prefs[key]

    override fun putString(key: String, value: String) {
        prefs[key] = value
        savePrefs()
    }

    override fun log(tag: String, message: String, level: LogLevel) {
        val fullTag = "Plugin[$pluginId]/$tag"
        when (level) {
            LogLevel.VERBOSE -> logger.verbose { "[$fullTag] $message" }
            LogLevel.DEBUG -> logger.debug { "[$fullTag] $message" }
            LogLevel.INFO -> logger.info { "[$fullTag] $message" }
            LogLevel.WARNING -> logger.warn { "[$fullTag] $message" }
            LogLevel.ERROR -> logger.error { "[$fullTag] $message" }
        }
    }

    private fun loadPrefs() {
        runCatching {
            if (!fileSystem.exists(prefFile)) return
            fileSystem.read(prefFile) {
                while (true) {
                    val line = readUtf8Line() ?: break
                    val eq = line.indexOf('=')
                    if (eq > 0) {
                        prefs[line.substring(0, eq)] = line.substring(eq + 1)
                    }
                }
            }
            logger.debug { "Loaded preferences for $pluginId" }
        }.onFailure { throwable ->
            logger.error(throwable) { "Failed to load preferences for $pluginId" }
        }
    }

    private fun savePrefs() {
        runCatching {
            fileSystem.write(prefFile) {
                prefs.forEach { (k, v) -> writeUtf8("$k=$v\n") }
            }
            logger.verbose { "Saved preferences for $pluginId" }
        }.onFailure { throwable ->
            logger.error(throwable) { "Failed to save preferences for $pluginId" }
        }
    }
}
