package com.dreamstream.core.runtime.registry

import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.runtime.loader.PluginLoader
import com.dreamstream.plugin.api.mapper.ApiMapper
import com.dreamstream.plugin.api.mapper.ApiMapper.toCoreModel
import com.dreamstream.plugin.api.model.catalog.CatalogRequest
import com.dreamstream.plugin.api.model.catalog.CatalogSection
import com.dreamstream.plugin.api.plugin.PluginContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * Central registry for all loaded [com.dreamstream.plugin.api.plugin.DreamPlugin] instances.
 *
 * Initialization happens asynchronously in a background [CoroutineScope] immediately on
 * construction. Callers that need data suspend on [state] until [RegistryState.Ready] — for
 * bundled plugins this is effectively instant (no disk I/O), for APK plugins it covers the
 * DexClassLoader round-trip.
 *
 * @param loaders Ordered list of [PluginLoader]s to query during startup.
 * @param context Shared [PluginContext] injected into every loaded plugin and provider.
 */
class PluginRegistry(
    private val loaders: List<PluginLoader>,
    private val context: PluginContext,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default,
) {
    private val _state = MutableStateFlow<RegistryState>(RegistryState.Initializing)
    val state: StateFlow<RegistryState> = _state.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)

    init {
        scope.launch {
            val providers = buildList {
                for (loader in loaders) {
                    val plugins = try {
                        loader.load()
                    } catch (e: Exception) {
                        emptyList()
                    }
                    for (plugin in plugins) {
                        try {
                            plugin.initialize(context)
                            val pluginProviders = plugin.registerProviders()
                            pluginProviders.forEach { provider ->
                                provider.inject(context.httpClient, context)
                            }
                            addAll(pluginProviders)
                        } catch (e: Exception) {
                            // Skip failed plugins; do not crash the whole registry.
                        }
                    }
                }
            }
            _state.value = RegistryState.Ready(providers)
        }
    }

    /**
     * Returns catalog sections from all providers that declare [supportsHomePage].
     *
     * Suspends until [RegistryState.Ready]. Individual provider failures are swallowed and
     * produce empty sections so a single broken plugin does not block the home screen.
     */
    suspend fun getHomeSections(page: Int = 1): List<CatalogSection> {
        val ready = state.first { it is RegistryState.Ready } as RegistryState.Ready
        return ready.providers
            .filter { it.supportsHomePage }
            .flatMap { provider ->
                try {
                    provider.getMainPage(page, CatalogRequest(page))?.sections ?: emptyList()
                } catch (e: Exception) {
                    emptyList()
                }
            }
    }

    /**
     * Searches all providers that declare [supportsSearch] and merges the results.
     *
     * Suspends until [RegistryState.Ready]. Individual provider failures are swallowed.
     */
    suspend fun search(query: String): List<SearchResult> {
        val ready = state.first { it is RegistryState.Ready } as RegistryState.Ready
        return ready.providers
            .filter { it.supportsSearch }
            .flatMap { provider ->
                try {
                    provider.search(query).map { with(ApiMapper) { it.toCoreModel(provider.name) } }
                } catch (e: Exception) {
                    emptyList()
                }
            }
    }
}
