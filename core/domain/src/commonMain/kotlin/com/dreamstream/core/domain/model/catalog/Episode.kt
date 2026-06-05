package com.dreamstream.core.domain.model.catalog

import kotlinx.serialization.Serializable

/**
 * A single episode within a TV series or anime.
 *
 * [Episode] represents one installment of an episodic series. It is used in
 * ***`SeriesDetail.episodes`*** and ***`AnimeDetail.subEpisodes/dubEpisodes`*** to display
 * the episode list on the detail screen.
 *
 * The [data] field is an opaque token used to resolve streaming links via
 * ***`ContentProvider.loadLinks`***. The [uniqueId] provides a stable key for list items
 * or bookmarks.
 *
 * ## Key Properties:
 * - [data]: Stream resolution token (***required***)
 * - [name]: Episode title (***optional***)
 * - [season]: Season number (***optional***)
 * - [episode]: Episode number (***optional***)
 * - [posterUrl]: Episode thumbnail (***optional***)
 * - [description]: Episode synopsis (***optional***)
 * - [runTime]: Duration in minutes (***optional***)
 * - [airDate]: Release date (***optional***)
 *
 * ## Usage:
 * ```kotlin
 * val episode = Episode(
 *     data = "token_abc123",
 *     name = "The Beginning",
 *     season = 1,
 *     episode = 1,
 *     runTime = 24
 * )
 * ```
 *
 * ## In Content Detail:
 * ```kotlin
 * AnimeDetail(
 *     name = "Attack on Titan",
 *     subEpisodes = listOf(
 *         Episode(data = "ep1_token", name = "Pilot", season = 1, episode = 1)
 *     )
 * )
 * ```
 *
 * ## Related:
 * - Used in:
 *      [com.dreamstream.core.model.detail.SeriesDetail]
 *      [com.dreamstream.core.model.detail.AnimeDetail]
 * - Resolved via: ***`ContentProvider.loadLinks`***
 * - Computed property: [uniqueId]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class Episode(
    /**
     * Provider-side data token or URL passed to the stream resolver.
     *
     * ***Required***. Passed to ***`ContentProvider.loadLinks`*** to extract playable URLs.
     * Maybe a URL, encoded token, or relative path.
     */
    val data: String,

    /**
     * Episode title.
     *
     * ***Optional***. Displayed as the episode name (e.g., "Pilot", "The Beginning").
     */
    val name: String? = null,

    /**
     * Season number (1-based).
     *
     * ***Optional***. Used to group episodes. Season `0` = specials.
     */
    val season: Int? = null,

    /**
     * Episode number within the season (1-based).
     *
     * ***Optional***. Used for ordering and display (e.g., "E1").
     */
    val episode: Int? = null,

    /**
     * URL to the episode thumbnail.
     *
     * ***Optional***. Displayed in the episode list. Falls back to series poster if null.
     */
    val posterUrl: String? = null,

    /**
     * Episode synopsis.
     *
     * ***Optional***. Shown in the episode details sheet.
     */
    val description: String? = null,

    /**
     * Episode-specific rating.
     *
     * ***Optional***. Displayed alongside the title (e.g., "8.5").
     */
    val rating: Float? = null,

    /**
     * Original air date (ISO 8601, e.g., "2023-04-08").
     *
     * ***Optional***. Displayed as release date.
     */
    val airDate: String? = null,

    /**
     * Runtime in milliseconds.
     *
     * ***Optional***. Alternative to [runTime] for millisecond precision.
     */
    val durationMs: Long? = null,

    /**
     * Runtime in minutes.
     *
     * ***Optional***. Displayed as "24 min" in the episode list.
     */
    val runTime: Int? = null,
) {

    /**
     * Stable composite key for list items and bookmarks.
     *
     * Format: `"season_episode_dataHash"` (e.g., `"1_1_-1234567890"`).
     * Use as a RecyclerView key or playback position tracker.
     */
    val uniqueId: String
        get() = "${season}_${episode}_${data.hashCode()}"
}
