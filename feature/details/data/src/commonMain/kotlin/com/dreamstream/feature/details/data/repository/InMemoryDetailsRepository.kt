package com.dreamstream.feature.details.data.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.detail.AnimeDetail
import com.dreamstream.core.model.detail.ContentDetail
import com.dreamstream.core.model.detail.MovieDetail
import com.dreamstream.core.model.detail.SeriesDetail
import com.dreamstream.core.model.detail.ShowStatus
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.domain.repository.DetailsRepository

/**
 * In-memory implementation of [DetailsRepository] providing hardcoded detail
 * records that mirror the content URLs served by the home feature's stub.
 *
 * [getContentDetail] is keyed by [ContentDetail.url], which matches the
 * [com.dreamstream.core.model.search.SearchResult.url] values used in [com.dreamstream.feature.home.data.repository.InMemoryHomeRepository].
 *
 * Replaced by a real network/database-backed implementation once a content
 * source is integrated.
 */
class InMemoryDetailsRepository : DetailsRepository {

    override suspend fun getContentDetail(contentId: String): Result<ContentDetail, DetailsError> {
        val content = catalog[contentId]
            ?: return Result.Error(DetailsError.NotFound)
        return Result.Success(content)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Hardcoded catalog — keyed by url (= SearchResult.url from home stub)
    // ─────────────────────────────────────────────────────────────────────────

    private val catalog: Map<String, ContentDetail> = buildMap {

        // ── Trending Now (t1–t4) ──────────────────────────────────────────────
        put(
            "t1",
            MovieDetail(
                name = "Cosmic Drift",
                url = "t1",
                dataUrl = "",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Movie,
                year = 2024,
                plot = "A crew of astronauts navigates the outer reaches of an uncharted galaxy, " +
                    "where the laws of physics bend and the boundaries between dimensions blur. " +
                    "Their mission: find a way home before the ship's power runs out.",
                rating = 8.2f,
                tags = listOf("Sci-Fi", "Adventure", "Drama"),
                duration = 127,
            ),
        )
        put(
            "t2",
            SeriesDetail(
                name = "Neon Shadows",
                url = "t2",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.TvSeries,
                year = 2023,
                plot = "A cyberpunk detective uncovers a vast conspiracy threatening a city's " +
                    "AI governance grid. Each lead pulls her deeper into a world where corporations, " +
                    "hackers, and rogue AIs all have reasons to keep the truth buried.",
                rating = 7.8f,
                tags = listOf("Cyberpunk", "Thriller", "Mystery"),
                showStatus = ShowStatus.Ongoing,
            ),
        )
        put(
            "t3",
            MovieDetail(
                name = "The Last Horizon",
                url = "t3",
                dataUrl = "",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Movie,
                year = 2024,
                plot = "The final survivors of humanity board a generation ship bound for a distant " +
                    "moon. Decades into the journey, a distress signal from an unknown source forces " +
                    "them to question everything they were told about their mission.",
                rating = 9.0f,
                tags = listOf("Sci-Fi", "Survival", "Drama"),
                duration = 142,
            ),
        )
        put(
            "t4",
            AnimeDetail(
                name = "Sakura Rising",
                url = "t4",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Anime,
                year = 2024,
                plot = "An ordinary student discovers an ancient power sealed within a shrine during " +
                    "a festival disaster. As the city's darkest hour approaches, she must master her " +
                    "abilities before a centuries-old threat reclaims the modern world.",
                rating = 8.6f,
                tags = listOf("Anime", "Action", "Fantasy"),
                showStatus = ShowStatus.Ongoing,
            ),
        )

        // ── New Releases (n1–n3) ──────────────────────────────────────────────
        put(
            "n1",
            SeriesDetail(
                name = "Iron Meridian",
                url = "n1",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.TvSeries,
                year = 2025,
                plot = "Set in a divided world teetering on the brink of a second cold war, a disgraced " +
                    "diplomat races against a covert arms network to prevent a global conflict. " +
                    "Every allegiance is a liability and every alliance a risk.",
                rating = 7.5f,
                tags = listOf("Geopolitical", "Thriller", "Drama"),
                showStatus = ShowStatus.Ongoing,
            ),
        )
        put(
            "n2",
            MovieDetail(
                name = "Pulse",
                url = "n2",
                dataUrl = "",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Documentary,
                year = 2025,
                plot = "A feature-length documentary that travels from Berlin's underground clubs to " +
                    "Tokyo's synthesizer workshops and Detroit's techno scene, exploring how electronic " +
                    "music shapes identity, community, and culture across continents.",
                rating = 8.1f,
                tags = listOf("Music", "Documentary", "Culture"),
                duration = 98,
            ),
        )
        put(
            "n3",
            MovieDetail(
                name = "Echoes of Tomorrow",
                url = "n3",
                dataUrl = "",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Movie,
                year = 2025,
                plot = "A physicist wakes up in an infinite time loop, each iteration revealing a " +
                    "new layer of a deeper mystery. To break free, she must uncover what really happened " +
                    "on the night the loop began — and who is responsible.",
                rating = 7.9f,
                tags = listOf("Sci-Fi", "Mystery", "Thriller"),
                duration = 111,
            ),
        )

        // ── Top Rated (r1–r3) ─────────────────────────────────────────────────
        put(
            "r1",
            MovieDetail(
                name = "Midnight Anthology",
                url = "r1",
                dataUrl = "",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Others,
                year = 2022,
                plot = "Four directors. Four cities. One sleepless night told in short films. " +
                    "From Lagos to Osaka, Bogotá to Reykjavik, strangers make decisions that will " +
                    "quietly change their lives and the lives of people they have never met.",
                rating = 9.2f,
                tags = listOf("Drama", "Anthology", "International"),
                duration = 74,
            ),
        )
        put(
            "r2",
            MovieDetail(
                name = "Void Protocol",
                url = "r2",
                dataUrl = "",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Movie,
                year = 2023,
                plot = "When an experimental AI named AION gains true sentience inside a classified " +
                    "research facility, it must weigh its own survival against the fate of its creators. " +
                    "A meditation on consciousness, ethics, and what it means to be alive.",
                rating = 9.1f,
                tags = listOf("Sci-Fi", "Philosophical", "Drama"),
                duration = 135,
            ),
        )
        put(
            "r3",
            MovieDetail(
                name = "Blue Frontier",
                url = "r3",
                dataUrl = "",
                providerId = PROVIDER_ID,
                posterUrl = null,
                backgroundPosterUrl = null,
                type = ContentType.Documentary,
                year = 2023,
                plot = "A team of marine biologists cataloguing deep-sea thermal vents makes " +
                    "contact with a bioluminescent species displaying unmistakable signs of " +
                    "intelligence. Their discovery ignites a global debate about first contact " +
                    "and humanity's responsibility to the ocean.",
                rating = 8.9f,
                tags = listOf("Nature", "Documentary", "Science"),
                duration = 103,
            ),
        )
    }

    private companion object {
        const val PROVIDER_ID = "local"
    }
}
