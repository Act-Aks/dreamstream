package com.dreamstream.feature.search.data.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.search.AnimeResult
import com.dreamstream.core.model.search.MovieResult
import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.model.search.SeriesResult
import com.dreamstream.feature.search.domain.error.SearchError
import com.dreamstream.feature.search.domain.repository.SearchRepository

/**
 * In-memory implementation of [SearchRepository] that filters the full stub
 * catalog by a case-insensitive substring match on [SearchResult.name].
 *
 * The catalog mirrors the 10 items used by the home and details stubs so that
 * search results navigate correctly to the details screen via their [SearchResult.url].
 *
 * Replaced by a real network/database-backed implementation once a content
 * source is integrated.
 *
 * Note: add debounce at the call site (e.g. in [SearchViewModel]) before
 * connecting to a network source that should not be hit on every keystroke.
 */
class InMemorySearchRepository : SearchRepository {

    private val catalog: List<SearchResult> = buildCatalog()

    override suspend fun search(query: String): Result<List<SearchResult>, SearchError> {
        val trimmed = query.trim()
        if (trimmed.isBlank()) return Result.Success(emptyList())
        val matches = catalog.filter { it.name.contains(trimmed, ignoreCase = true) }
        return Result.Success(matches)
    }

    private fun buildCatalog(): List<SearchResult> = listOf(
        // ── Trending ─────────────────────────────────────────────────────────
        MovieResult(
            name = "Cosmic Drift",
            url = "t1",
            providerId = PROVIDER_ID,
            year = 2024,
            rating = 8.2f,
        ),
        SeriesResult(
            name = "Neon Shadows",
            url = "t2",
            providerId = PROVIDER_ID,
            year = 2023,
            rating = 7.8f,
        ),
        MovieResult(
            name = "The Last Horizon",
            url = "t3",
            providerId = PROVIDER_ID,
            year = 2024,
            rating = 9.0f,
        ),
        AnimeResult(
            name = "Sakura Rising",
            url = "t4",
            providerId = PROVIDER_ID,
            year = 2024,
            rating = 8.6f,
        ),
        // ── New Releases ──────────────────────────────────────────────────────
        SeriesResult(
            name = "Iron Meridian",
            url = "n1",
            providerId = PROVIDER_ID,
            year = 2025,
            rating = 7.5f,
        ),
        MovieResult(
            name = "Pulse",
            url = "n2",
            providerId = PROVIDER_ID,
            type = ContentType.Documentary,
            year = 2025,
            rating = 8.1f,
        ),
        MovieResult(
            name = "Echoes of Tomorrow",
            url = "n3",
            providerId = PROVIDER_ID,
            year = 2025,
            rating = 7.9f,
        ),
        // ── Top Rated ─────────────────────────────────────────────────────────
        MovieResult(
            name = "Midnight Anthology",
            url = "r1",
            providerId = PROVIDER_ID,
            type = ContentType.Others,
            year = 2022,
            rating = 9.2f,
        ),
        MovieResult(
            name = "Void Protocol",
            url = "r2",
            providerId = PROVIDER_ID,
            year = 2023,
            rating = 9.1f,
        ),
        MovieResult(
            name = "Blue Frontier",
            url = "r3",
            providerId = PROVIDER_ID,
            type = ContentType.Documentary,
            year = 2023,
            rating = 8.9f,
        ),
    )

    private companion object {
        const val PROVIDER_ID = "local"
    }
}
