package com.dreamstream.core.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.catalog.Episode
import com.dreamstream.core.model.catalog.Season
import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for a TV series.
 *
 * [SeriesDetail] represents a complete metadata payload for a live-action TV show.
 * It is the primary model returned by ***`ContentProvider.loadSeriesDetail`*** for TV series content.
 *
 * This class extends [ContentDetail] and adds series-specific properties:
 * - **Episodes**: Flat list in [episodes], grouped by [episodesBySeason]
 * - **Seasons**: Structured metadata in [seasons]
 * - **Status**: [showStatus] (e.g., "Airing", "Completed")
 * - **Count**: [totalEpisodes] (total from provider)
 *
 * ## Key Properties:
 * - [name]: Series title (***required***)
 * - [url]: Stable content identifier (***required***)
 * - [episodes]: All episodes (***required***)
 * - [seasons]: Season metadata (***optional***)
 * - [showStatus]: Broadcast status (***optional***)
 * - [totalEpisodes]: Total count (***optional***)
 * - All inherited properties from [ContentDetail]: [posterUrl], [plot], [rating], [actors], etc.
 *
 * ## Episode Grouping:
 * Episodes are stored as a flat list in [episodes]. The computed property
 * [episodesBySeason] groups them by season number; season `0` represents
 * specials or episodes not assigned to a season.
 * ```kotlin
 * val season1Episodes = seriesDetail.episodesBySeason // List<Episode>[1]
 * val specials = seriesDetail.episodesBySeason        // Specials
 * ```
 *
 * ## Safe Season Access:
 * Use [getEpisodesForSeason] for safe per-season access without null checks:
 * ```kotlin
 * val ep3 = seriesDetail.getEpisodesForSeason(2).firstOrNull()
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val series = SeriesDetail(
 *     name = "Breaking Bad",
 *     url = "/tv/breaking-bad",
 *     year = 2008,
 *     episodes = listOf(
 *         Episode(data = "s1e1_token", name = "Pilot", season = 1, episode = 1),
 *         Episode(data = "s1e2_token", name = "Cats in the Bag", season = 1, episode = 2)
 *     ),
 *     seasons = listOf(Season(season = 1, name = "Season 1", episodeCount = 7)),
 *     showStatus = ShowStatus.Completed,
 *     totalEpisodes = 62
 * )
 * ```
 *
 * ## In Content Detail:
 * ```kotlin
 * SeriesDetail(
 *     name = "Stranger Things",
 *     posterUrl = "https://example.com/poster.jpg",
 *     plot = "When a young boy vanishes...",
 *     rating = 8.7f,
 *     episodes = listOf(/* ... */),
 *     tags = listOf("Sci-Fi", "Horror"),
 *     actors = listOf(Actor("Millie Bobby Brown", role = "Eleven"))
 * )
 * ```
 *
 * ## Related:
 * - Base class: [ContentDetail]
 * - Content type: ***`ContentType.TvSeries`***
 * - Episode type: [Episode]
 * - Season type: [Season]
 * - Status: [ShowStatus]
 * - Provider method: ***`ContentProvider.loadSeriesDetail`***
 * - Anime detail: [AnimeDetail] (separate sub/dub lists)
 * - Movie detail: [MovieDetail]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class SeriesDetail(
    /**
     * Series title (display name).
     *
     * ***Required***. Primary title shown in UI (e.g., "Breaking Bad", "Stranger Things").
     * Inherits from [ContentDetail.name].
     */
    override val name: String,

    /**
     * Stable content identifier URL.
     *
     * ***Required***. Used as the primary key for bookmarks, history, and playback.
     * Inherits from [ContentDetail.url].
     */
    override val url: String,

    /**
     * URL to the poster image.
     *
     * ***Optional***. Displayed in search results, catalog cards, and detail header.
     * Inherits from [ContentDetail.posterUrl].
     */
    override val posterUrl: String? = null,

    /**
     * URL to the background/heroes poster.
     *
     * ***Optional***. Displayed as the full-screen background in the detail screen.
     * Inherits from [ContentDetail.backgroundPosterUrl].
     */
    override val backgroundPosterUrl: String? = null,

    /**
     * Content type (always [ContentType.TvSeries]).
     *
     * ***Optional***. Defaults to [ContentType.TvSeries].
     * Inherits from [ContentDetail.type].
     */
    override val type: ContentType = ContentType.TvSeries,

    /**
     * Release year.
     *
     * ***Optional***. Displayed in metadata (e.g., "2008").
     * Inherits from [ContentDetail.year].
     */
    override val year: Int? = null,

    /**
     * Synopsis or plot summary.
     *
     * ***Optional***. Displayed in the "About" section of the detail screen.
     * Inherits from [ContentDetail.plot].
     */
    override val plot: String? = null,

    /**
     * User or critic rating.
     *
     * ***Optional***. Displayed as a score (e.g., "9.5/10").
     * Inherits from [ContentDetail.rating].
     */
    override val rating: Float? = null,

    /**
     * Genres, themes, or tags.
     *
     * ***Optional***. Displayed as chips in the detail header.
     * Inherits from [ContentDetail.tags].
     */
    override val tags: List<String> = emptyList(),

    /**
     * Recommended similar series.
     *
     * ***Optional***. Displayed in the "You May Also Like" section.
     * Inherits from [ContentDetail.recommendations].
     */
    override val recommendations: List<SearchResult> = emptyList(),

    /**
     * Cast members (actors, director, staff).
     *
     * ***Optional***. Displayed in the cast section.
     * Inherits from [ContentDetail.actors].
     */
    override val actors: List<Actor> = emptyList(),

    /**
     * Provider-specific unique identifier.
     *
     * ***Required***. Used to identify which provider returned this detail.
     * Inherits from [ContentDetail.providerId].
     */
    override val providerId: String,

    /**
     * URL to the trailer video.
     *
     * ***Optional***. Displayed as a clickable trailer button in the detail screen.
     * Inherits from [ContentDetail.trailerUrl].
     */
    override val trailerUrl: String? = null,

    /**
     * All episodes for this series across all seasons.
     *
     * ***Required***. Flat list of [Episode] instances. Ordered by season/episode.
     * Use [episodesBySeason] for grouped access or [getEpisodesForSeason] for safe lookup.
     * Empty list if episodes are unavailable.
     */
    val episodes: List<Episode> = emptyList(),

    /**
     * Structured season metadata.
     *
     * ***Optional***. List of [Season] with names, posters, and episode counts.
     * May be empty if the provider only provides a flat episode list.
     */
    val seasons: List<Season> = emptyList(),

    /**
     * Current broadcast or release status.
     *
     * ***Optional***. Defaults to [ShowStatus.Unknown].
     * Examples: "Airing", "Completed", "Upcoming".
     * Displayed in the detail header metadata.
     */
    val showStatus: ShowStatus = ShowStatus.Unknown,

    /**
     * Total episode count reported by the provider.
     *
     * ***Optional***. Displayed as "62 episodes" in the series header.
     * Null when not declared by the source.
     */
    val totalEpisodes: Int? = null,
) : ContentDetail() {

    /**
     * Episodes grouped by season number.
     *
     * ***Computed***. Groups [episodes] by [Episode.season].
     * Season `0` collects specials and unassigned episodes.
     *
     * Example:
     * ```kotlin
     * episodesBySeason // List of Season 1 episodes[1]
     * episodesBySeason // List of specials
     * ```
     */
    val episodesBySeason: Map<Int, List<Episode>>
        get() = episodes.groupBy { it.season ?: 0 }

    /**
     * Returns all episodes for [seasonNumber], or an empty list when the
     * season does not exist or has no episodes.
     *
     * ***Safe access***: No null checks required. Use this instead of
     * directly accessing [episodesBySeason] to avoid `null` handling.
     *
     * @param seasonNumber Season number (1-based). Use `0` for specials.
     * @return List of episodes for the season (may be empty).
     */
    fun getEpisodesForSeason(seasonNumber: Int): List<Episode> =
        episodesBySeason[seasonNumber] ?: emptyList()
}
