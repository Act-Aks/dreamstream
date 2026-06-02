package com.dreamstream.feature.search.data.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.runtime.registry.PluginRegistry
import com.dreamstream.feature.search.domain.error.SearchError
import com.dreamstream.feature.search.domain.repository.SearchRepository

/**
 * [SearchRepository] implementation backed by the plugin system.
 *
 * Delegates to [PluginRegistry.search] which fans out the query across all loaded
 * [com.dreamstream.plugin.api.provider.ContentProvider] instances that declare
 * [supportsSearch = true]. Results from all providers are merged into a single list.
 *
 * A blank query returns an empty list immediately without hitting any provider.
 * Falls back to [SearchError.SearchFailed] on unexpected exceptions.
 */
class PluginSearchRepository(
    private val registry: PluginRegistry,
) : SearchRepository {

    override suspend fun search(query: String): Result<List<SearchResult>, SearchError> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return Result.Success(emptyList())
        return try {
            Result.Success(registry.search(trimmed))
        } catch (e: Exception) {
            Result.Error(SearchError.SearchFailed)
        }
    }
}
