package com.dreamstream.core.plugin.loader

import com.dreamstream.core.domain.extensions.info
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.plugin.api.extractor.Extractor
import com.dreamstream.plugin.api.plugin.DreamPlugin
import com.dreamstream.plugin.api.provider.ContentProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoadedPlugin(
    val id: String,
    val plugin: DreamPlugin,
    val providers: List<ContentProvider>,
    val extractors: List<Extractor>,
    val isEnabled: Boolean = true,
)

class PluginRegistry(
    loggerFactory: LoggerFactory
) {

    private val logger = loggerFactory.get("PluginRegistry")
    private val _plugins = MutableStateFlow<Map<String, LoadedPlugin>>(emptyMap())
    val plugins: StateFlow<Map<String, LoadedPlugin>> = _plugins.asStateFlow()

    fun register(loadedPlugin: LoadedPlugin) {
        _plugins.update { current ->
            current + (loadedPlugin.id to loadedPlugin)
        }
        logger.info {
            "Registered plugin: ${loadedPlugin.id} " +
                "(${loadedPlugin.providers.size} providers, " +
                "${loadedPlugin.extractors.size} extractors)"
        }
    }

    fun unregister(pluginId: String) {
        _plugins.update { current -> current - pluginId }
        logger.info { "Unregistered plugin: $pluginId" }
    }

    fun setEnabled(pluginId: String, enabled: Boolean) {
        _plugins.update { current ->
            val plugin = current[pluginId] ?: return@update current
            current + (pluginId to plugin.copy(isEnabled = enabled))
        }
    }

    fun getPlugin(pluginId: String): LoadedPlugin? = _plugins.value[pluginId]

    fun getAllProviders(): List<ContentProvider> =
        _plugins.value.values
            .filter { it.isEnabled }
            .flatMap { it.providers }

    fun getAllExtractors(): List<Extractor> =
        _plugins.value.values
            .filter { it.isEnabled }
            .flatMap { it.extractors }

    fun getExtractorForUrl(url: String): Extractor? =
        getAllExtractors().firstOrNull { it.canHandle(url) }

    fun getProviderById(providerId: String): ContentProvider? =
        getAllProviders().firstOrNull { it.name == providerId }

    fun isPluginLoaded(pluginId: String): Boolean =
        _plugins.value.containsKey(pluginId)

    fun clear() {
        _plugins.update { emptyMap() }
    }
}
