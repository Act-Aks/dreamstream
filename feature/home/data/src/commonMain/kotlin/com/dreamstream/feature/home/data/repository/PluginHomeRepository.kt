package com.dreamstream.feature.home.data.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.runtime.registry.PluginRegistry
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.HomeSection
import com.dreamstream.feature.home.domain.repository.HomeRepository

/**
 * [HomeRepository] implementation backed by the plugin system.
 *
 * Delegates to [PluginRegistry.getHomeSections] which queries all loaded
 * [com.dreamstream.plugin.api.provider.ContentProvider] instances that declare
 * [supportsHomePage = true]. Sections returned by the registry are already
 * mapped to domain [com.dreamstream.core.model.search.SearchResult] types.
 *
 * Falls back to [HomeError.LoadFailed] on exceptions and returns
 * [HomeError.NoContentAvailable] when no provider returns any sections.
 */
class PluginHomeRepository(
    private val registry: PluginRegistry,
) : HomeRepository {

    override suspend fun getHomeSections(): Result<List<HomeSection>, HomeError> {
        return try {
            val sections = registry.getHomeSections(page = 1)
            if (sections.isEmpty()) {
                Result.Error(HomeError.NoContentAvailable)
            } else {
                Result.Success(
                    sections.map { section ->
                        HomeSection(
                            id = section.name
                                .lowercase()
                                .replace(" ", "_")
                                .replace(Regex("[^a-z0-9_]"), ""),
                            title = section.name,
                            items = section.items,
                        )
                    },
                )
            }
        } catch (e: Exception) {
            Result.Error(HomeError.LoadFailed)
        }
    }
}
