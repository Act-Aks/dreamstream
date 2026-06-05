package com.dreamstream.core.domain.repository

import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.core.domain.model.catalog.CatalogResponse
import com.dreamstream.core.domain.model.detail.ContentDetail
import com.dreamstream.core.domain.model.search.SearchResult
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface ContentRepository {

    /**
     * Search across all enabled providers or a specific one.
     */
    suspend fun search(
        query: String,
        providerId: String? = null,
    ): Result<List<SearchResult>, DreamError>

    /**
     * Load the home page for a specific provider.
     */
    fun getHomePage(
        providerId: String,
        page: Int = 1,
        request: CatalogRequest = CatalogRequest(),
    ): Flow<Result<CatalogResponse, DreamError>>

    /**
     * Load full content details.
     */
    suspend fun loadContent(
        url: String,
        providerId: String,
    ): Result<ContentDetail, DreamError>

    /**
     * Get streaming links for content.
     * Emits links as they are discovered.
     */
    fun getStreamLinks(
        data: String,
        providerId: String,
        isCasting: Boolean = false,
    ): Flow<StreamResult>
}
