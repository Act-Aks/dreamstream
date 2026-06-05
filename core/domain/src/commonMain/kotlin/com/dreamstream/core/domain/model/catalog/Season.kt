package com.dreamstream.core.domain.model.catalog

import kotlinx.serialization.Serializable

/**
 * A named season grouping within a TV series or anime.
 *
 * [Season] represents a bounded collection of episodes (e.g., "Season 1", "Winter 2024").
 * It is used in ***`SeriesDetail.seasons`*** to group episodes and display season metadata
 * in the detail screen.
 *
 * ## Key Properties:
 * - [season]: Season number (***required***)
 * - [name]: Season title (***optional***)
 * - [displaySeason]: Custom display number (***optional***)
 * - [posterUrl]: Season poster (***optional***)
 * - [airDate]: First episode air date (***optional***)
 * - [episodeCount]: Total episodes in season (***optional***)
 *
 * ## Usage:
 * ```kotlin
 * val season = Season(
 *     season = 1,
 *     name = "The Beginning",
 *     episodeCount = 12,
 *     posterUrl = "https://example.com/s1.jpg"
 * )
 * ```
 *
 * ## In Content Detail:
 * ```kotlin
 * SeriesDetail(
 *     name = "Breaking Bad",
 *     seasons = listOf(
 *         Season(season = 1, name = "Season 1", episodeCount = 7),
 *         Season(season = 2, name = "Season 2", episodeCount = 13)
 *     )
 * )
 * ```
 *
 * ## Related:
 * - Used in:
 *      ***`SeriesDetail`***
 * - Companion type: [Episode]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class Season(
    /**
     * Season number (1-based).
     *
     * ***Required***. Used for ordering and grouping (e.g., 1, 2, 3).
     * Season `0` typically represents specials or unassigned episodes.
     */
    val season: Int,

    /**
     * Season title or label.
     *
     * ***Optional***. Examples: "Season 1", "Winter 2024", "The Offing".
     * Displayed as the season header in the UI. Falls back to "Season X" if null.
     */
    val name: String? = null,

    /**
     * Custom display number for the season.
     *
     * ***Optional***. Use if the display number differs from [season]
     * (e.g., provider uses 0-based indexing but UI shows 1-based).
     * If null, [season] is used for display.
     */
    val displaySeason: Int? = null,

    /**
     * URL to the season-specific poster or banner.
     *
     * ***Optional***. Displayed as a thumbnail in the season selector.
     * If null, the series poster is used instead.
     */
    val posterUrl: String? = null,

    /**
     * First episode air date.
     *
     * ***Optional***. ISO 8601 format (e.g., "2023-01-15").
     * Displayed as release date in season metadata.
     */
    val airDate: String? = null,

    /**
     * Total number of episodes in this season.
     *
     * ***Optional***. Displayed as "12 episodes" in the season card.
     * Useful for progress tracking (e.g., "5/12 watched").
     */
    val episodeCount: Int? = null,
)
