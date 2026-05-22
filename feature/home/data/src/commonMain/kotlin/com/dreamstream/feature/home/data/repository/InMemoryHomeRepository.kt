package com.dreamstream.feature.home.data.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.Content
import com.dreamstream.feature.home.domain.model.ContentId
import com.dreamstream.feature.home.domain.model.ContentType
import com.dreamstream.feature.home.domain.model.HomeSection
import com.dreamstream.feature.home.domain.repository.HomeRepository

/**
 * In-memory implementation of [HomeRepository] providing hardcoded sample
 * content. Replaced by a real network/database-backed implementation once a
 * content source is integrated.
 */
class InMemoryHomeRepository : HomeRepository {

    override suspend fun getHomeSections(): Result<List<HomeSection>, HomeError> =
        Result.Success(buildSampleSections())

    private fun buildSampleSections(): List<HomeSection> = listOf(
        HomeSection(
            id = "trending",
            title = "Trending Now",
            items = listOf(
                Content(
                    id = ContentId("t1"),
                    title = "Cosmic Drift",
                    description = "A crew of astronauts navigates the outer reaches of an uncharted galaxy.",
                    thumbnailUrl = null,
                    type = ContentType.Movie,
                    year = 2024,
                    rating = 8.2f,
                ),
                Content(
                    id = ContentId("t2"),
                    title = "Neon Shadows",
                    description = "A cyberpunk detective uncovers a conspiracy threatening a city's AI grid.",
                    thumbnailUrl = null,
                    type = ContentType.Series,
                    year = 2023,
                    rating = 7.8f,
                ),
                Content(
                    id = ContentId("t3"),
                    title = "The Last Horizon",
                    description = "The final survivors of humanity seek refuge on a distant moon.",
                    thumbnailUrl = null,
                    type = ContentType.Movie,
                    year = 2024,
                    rating = 9.0f,
                ),
                Content(
                    id = ContentId("t4"),
                    title = "Sakura Rising",
                    description = "An unlikely hero awakens extraordinary powers during a city's darkest hour.",
                    thumbnailUrl = null,
                    type = ContentType.Anime,
                    year = 2024,
                    rating = 8.6f,
                ),
            ),
        ),
        HomeSection(
            id = "new_releases",
            title = "New Releases",
            items = listOf(
                Content(
                    id = ContentId("n1"),
                    title = "Iron Meridian",
                    description = "A geopolitical thriller set in a divided world on the brink of war.",
                    thumbnailUrl = null,
                    type = ContentType.Series,
                    year = 2025,
                    rating = 7.5f,
                ),
                Content(
                    id = ContentId("n2"),
                    title = "Pulse",
                    description = "A documentary exploring the science and culture of electronic music.",
                    thumbnailUrl = null,
                    type = ContentType.Documentary,
                    year = 2025,
                    rating = 8.1f,
                ),
                Content(
                    id = ContentId("n3"),
                    title = "Echoes of Tomorrow",
                    description = "A time-loop story where each iteration reveals a deeper mystery.",
                    thumbnailUrl = null,
                    type = ContentType.Movie,
                    year = 2025,
                    rating = 7.9f,
                ),
            ),
        ),
        HomeSection(
            id = "top_rated",
            title = "Top Rated",
            items = listOf(
                Content(
                    id = ContentId("r1"),
                    title = "Midnight Anthology",
                    description = "Four directors. Four cities. One sleepless night told in short films.",
                    thumbnailUrl = null,
                    type = ContentType.Short,
                    year = 2022,
                    rating = 9.2f,
                ),
                Content(
                    id = ContentId("r2"),
                    title = "Void Protocol",
                    description = "An AI gains sentience and must decide the fate of its creators.",
                    thumbnailUrl = null,
                    type = ContentType.Movie,
                    year = 2023,
                    rating = 9.1f,
                ),
                Content(
                    id = ContentId("r3"),
                    title = "Blue Frontier",
                    description = "Marine biologists discover an intelligent species in the deep ocean.",
                    thumbnailUrl = null,
                    type = ContentType.Documentary,
                    year = 2023,
                    rating = 8.9f,
                ),
            ),
        ),
    )
}
