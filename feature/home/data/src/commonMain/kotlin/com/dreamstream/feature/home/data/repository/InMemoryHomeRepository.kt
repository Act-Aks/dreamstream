package com.dreamstream.feature.home.data.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.search.AnimeResult
import com.dreamstream.core.domain.model.search.MovieResult
import com.dreamstream.core.domain.model.search.SeriesResult
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.HomeSection
import com.dreamstream.feature.home.domain.repository.HomeRepository

/**
 * In-memory implementation of [HomeRepository] providing hardcoded sample
 * content. Replaced by a real network/database-backed implementation once a
 * content source is integrated.
 *
 * [getHomeSections] is keyed by [SearchResult.url], which matches the
 * [SearchResult.url] values used as navigation keys throughout the app.
 */
class InMemoryHomeRepository : HomeRepository {

    override suspend fun getHomeSections(): Result<List<HomeSection>, HomeError> =
        Result.Success(buildSampleSections())

    private fun buildSampleSections(): List<HomeSection> = listOf(
        HomeSection(
            id = "trending",
            title = "Trending Now",
            items = listOf(
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
            ),
        ),
        HomeSection(
            id = "new_releases",
            title = "New Releases",
            items = listOf(
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
            ),
        ),
        HomeSection(
            id = "top_rated",
            title = "Top Rated",
            items = listOf(
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
            ),
        ),
    )

    private companion object {
        const val PROVIDER_ID = "local"
    }
}
