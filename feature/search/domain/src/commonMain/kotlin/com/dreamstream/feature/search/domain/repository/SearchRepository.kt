package com.dreamstream.feature.search.domain.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.feature.search.domain.error.SearchError

/**
 * Contract for keyword search across available content sources.
 *
 * A blank [query] should return an empty list rather than all items — callers
 * are responsible for guarding against accidental all-catalog fetches.
 *
 * Defined in domain so the presentation layer can depend on this interface
 * without knowing about the data layer implementation.
 */
interface SearchRepository {
    suspend fun search(query: String): Result<List<SearchResult>, SearchError>
}
