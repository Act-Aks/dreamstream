package com.dreamstream.feature.details.data.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.domain.model.DetailContent
import com.dreamstream.feature.details.domain.model.DetailMediaType
import com.dreamstream.feature.details.domain.repository.DetailsRepository

/**
 * In-memory implementation of [DetailsRepository] providing hardcoded detail
 * records that mirror the content IDs served by the home feature's stub.
 * Replaced by a real network/database-backed implementation once a content
 * source is integrated.
 */
class InMemoryDetailsRepository : DetailsRepository {

    override suspend fun getContentDetail(contentId: String): Result<DetailContent, DetailsError> {
        val content = catalog[contentId]
            ?: return Result.Error(DetailsError.NotFound)
        return Result.Success(content)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Hardcoded catalog — keyed by content ID
    // ─────────────────────────────────────────────────────────────────────────

    private val catalog: Map<String, DetailContent> = buildMap {
        // Trending Now (from home stub: t1–t4)
        put(
            "t1", DetailContent(
                contentId = "t1",
                title = "Cosmic Drift",
                synopsis = "A crew of astronauts navigates the outer reaches of an uncharted galaxy, " +
                    "where the laws of physics bend and the boundaries between dimensions blur. " +
                    "Their mission: find a way home before the ship's power runs out.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Movie,
                year = 2024,
                rating = 8.2f,
                genres = listOf("Sci-Fi", "Adventure", "Drama"),
                durationMinutes = 127,
            ),
        )
        put(
            "t2", DetailContent(
                contentId = "t2",
                title = "Neon Shadows",
                synopsis = "A cyberpunk detective uncovers a vast conspiracy threatening a city's " +
                    "AI governance grid. Each lead pulls her deeper into a world where corporations, " +
                    "hackers, and rogue AIs all have reasons to keep the truth buried.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Series,
                year = 2023,
                rating = 7.8f,
                genres = listOf("Cyberpunk", "Thriller", "Mystery"),
                durationMinutes = null,
            ),
        )
        put(
            "t3", DetailContent(
                contentId = "t3",
                title = "The Last Horizon",
                synopsis = "The final survivors of humanity board a generation ship bound for a distant " +
                    "moon. Decades into the journey, a distress signal from an unknown source forces " +
                    "them to question everything they were told about their mission.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Movie,
                year = 2024,
                rating = 9.0f,
                genres = listOf("Sci-Fi", "Survival", "Drama"),
                durationMinutes = 142,
            ),
        )
        put(
            "t4", DetailContent(
                contentId = "t4",
                title = "Sakura Rising",
                synopsis = "An ordinary student discovers an ancient power sealed within a shrine during " +
                    "a festival disaster. As the city's darkest hour approaches, she must master her " +
                    "abilities before a centuries-old threat reclaims the modern world.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Anime,
                year = 2024,
                rating = 8.6f,
                genres = listOf("Anime", "Action", "Fantasy"),
                durationMinutes = null,
            ),
        )

        // New Releases (from home stub: n1–n3)
        put(
            "n1", DetailContent(
                contentId = "n1",
                title = "Iron Meridian",
                synopsis = "Set in a divided world teetering on the brink of a second cold war, a disgraced " +
                    "diplomat races against a covert arms network to prevent a global conflict. " +
                    "Every allegiance is a liability and every alliance a risk.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Series,
                year = 2025,
                rating = 7.5f,
                genres = listOf("Geopolitical", "Thriller", "Drama"),
                durationMinutes = null,
            ),
        )
        put(
            "n2", DetailContent(
                contentId = "n2",
                title = "Pulse",
                synopsis = "A feature-length documentary that travels from Berlin's underground clubs to " +
                    "Tokyo's synthesizer workshops and Detroit's techno scene, exploring how electronic " +
                    "music shapes identity, community, and culture across continents.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Documentary,
                year = 2025,
                rating = 8.1f,
                genres = listOf("Music", "Documentary", "Culture"),
                durationMinutes = 98,
            ),
        )
        put(
            "n3", DetailContent(
                contentId = "n3",
                title = "Echoes of Tomorrow",
                synopsis = "A physicist wakes up in an infinite time loop, each iteration revealing a " +
                    "new layer of a deeper mystery. To break free, she must uncover what really happened " +
                    "on the night the loop began — and who is responsible.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Movie,
                year = 2025,
                rating = 7.9f,
                genres = listOf("Sci-Fi", "Mystery", "Thriller"),
                durationMinutes = 111,
            ),
        )

        // Top Rated (from home stub: r1–r3)
        put(
            "r1", DetailContent(
                contentId = "r1",
                title = "Midnight Anthology",
                synopsis = "Four directors. Four cities. One sleepless night told in short films. " +
                    "From Lagos to Osaka, Bogotá to Reykjavik, strangers make decisions that will " +
                    "quietly change their lives and the lives of people they have never met.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Short,
                year = 2022,
                rating = 9.2f,
                genres = listOf("Drama", "Anthology", "International"),
                durationMinutes = 74,
            ),
        )
        put(
            "r2", DetailContent(
                contentId = "r2",
                title = "Void Protocol",
                synopsis = "When an experimental AI named AION gains true sentience inside a classified " +
                    "research facility, it must weigh its own survival against the fate of its creators. " +
                    "A meditation on consciousness, ethics, and what it means to be alive.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Movie,
                year = 2023,
                rating = 9.1f,
                genres = listOf("Sci-Fi", "Philosophical", "Drama"),
                durationMinutes = 135,
            ),
        )
        put(
            "r3", DetailContent(
                contentId = "r3",
                title = "Blue Frontier",
                synopsis = "A team of marine biologists cataloguing deep-sea thermal vents makes " +
                    "contact with a bioluminescent species displaying unmistakable signs of " +
                    "intelligence. Their discovery ignites a global debate about first contact " +
                    "and humanity's responsibility to the ocean.",
                thumbnailUrl = null,
                backdropUrl = null,
                type = DetailMediaType.Documentary,
                year = 2023,
                rating = 8.9f,
                genres = listOf("Nature", "Documentary", "Science"),
                durationMinutes = 103,
            ),
        )
    }
}
