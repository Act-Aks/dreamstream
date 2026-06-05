package com.dreamstream.core.domain.model.detail

import com.dreamstream.core.domain.model.catalog.Actor
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Episode
import com.dreamstream.core.domain.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for an anime title.
 *
 * [AnimeDetail] represents a complete metadata payload for an anime series or movie.
 * It is the primary model returned by ***`ContentProvider.loadAnimeDetail`*** for anime-specific content.
 *
 * This class extends [ContentDetail] and adds anime-specific properties:
 * - **Separate episode tracks**: [subEpisodes] (subtitled) and [dubEpisodes] (dubbed)
 * - **Broadcast status**: [showStatus] (e.g., "Airing", "Completed")
 * - **Original title**: [japaneseTitle]
 * - **Cross-reference IDs**: [malId] (MyAnimeList), [anilistId] (AniList)
 *
 * ## Key Properties:
 * - [name]: Anime title (***required***)
 * - [url]: Stable content identifier (***required***)
 * - [subEpisodes]: Subtitled episode list (***required***)
 * - [dubEpisodes]: Dubbed episode list (***optional***)
 * - [showStatus]: Broadcast status (***optional***)
 * - [japaneseTitle]: Original Japanese title (***optional***)
 * - [malId]: MyAnimeList ID (***optional***)
 * - [anilistId]: AniList ID (***optional***)
 * - All inherited properties from [ContentDetail]: [posterUrl], [plot], [rating], [tags], [actors], etc.
 *
 * ## Sub vs. Dub Separation:
 * Sub (subtitled) and dub (dubbed) tracks are represented as separate episode lists,
 * reflecting the common provider pattern where sub and dub versions are served as
 * distinct streams with independent episode data tokens.
 * ```kotlin
 * val subCount = animeDetail.subEpisodes.size
 * val dubCount = animeDetail.dubEpisodes.size
 * ```
 *
 * ## Cross-Reference IDs:
 * [malId] and [anilistId] are optional IDs for metadata enrichment (e.g., fetching
 * external reviews, ratings). Do **not** use them as primary keys; use [url] instead.
 * ```kotlin
 * if (animeDetail.malId != null) {
 *     // Fetch external data from MyAnimeList API
 * }
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val anime = AnimeDetail(
 *     name = "Attack on Titan",
 *     url = "/anime/attack-on-titan",
 *     japaneseTitle = "Shingeki no Kyojin",
 *     malId = 16498,
 *     subEpisodes = listOf(/* ... */),
 *     dubEpisodes = listOf(/* ... */),
 *     showStatus = ShowStatus.Completed
 * )
 * ```
 *
 * ## In Content Detail:
 * ```kotlin
 * AnimeDetail(
 *     name = "Demon Slayer",
 *     posterUrl = "https://example.com/poster.jpg",
 *     plot = "Tanjiro seeks a cure for his sister...",
 *     subEpisodes = listOf(
 *         Episode(data = "ep1_token", name = "Cruelty", season = 1, episode = 1)
 *     ),
 *     dubEpisodes = listOf(
 *         Episode(data = "ep1_dub_token", name = "Cruelty", season = 1, episode = 1)
 *     ),
 *     tags = listOf("Action", "Supernatural"),
 *     actors = listOf(Actor("Natsuki Hanae", role = "Tanjiro"))
 * )
 * ```
 *
 * ## Related:
 * - Base class: [ContentDetail]
 * - Episode types: [Episode]
 * - Cast: [Actor]
 * - Content type: ***`ContentType.Anime`***, ***`ContentType.AnimeMovie`***
 * - Status: [ShowStatus]
 * - Provider method: ***`ContentProvider.loadAnimeDetail`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class AnimeDetail(
    /**
     * Anime title (display name).
     *
     * ***Required***. Primary title shown in UI (e.g., "Attack on Titan").
     * Inherits from [ContentDetail.name].
     */
    override val name: String,

    /**
     * Stable content identifier URL.
     *
     * ***Required***. Used as the primary key for bookmarks, history, and playback.
     * Inherits from [ContentDetail.url].
     *
     * ***Do not use [malId] or [anilistId] as primary keys***; use [url] instead.
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
     * Content type (always [ContentType.Anime] or [ContentType.AnimeMovie]).
     *
     * ***Optional***. Defaults to [ContentType.Anime].
     * Inherits from [ContentDetail.type].
     */
    override val type: ContentType = ContentType.Anime,

    /**
     * Release year.
     *
     * ***Optional***. Displayed in metadata (e.g., "2023").
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
     * ***Optional***. Displayed as a score (e.g., "8.7/10").
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
     * Recommended similar content.
     *
     * ***Optional***. Displayed in the "You May Also Like" section.
     * Inherits from [ContentDetail.recommendations].
     */
    override val recommendations: List<SearchResult> = emptyList(),

    /**
     * Cast members (voice actors, director, staff).
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
     * Episodes available with subtitles.
     *
     * ***Required***. Primary episode list for subtitled viewers.
     * Each [Episode] contains a unique [Episode.data] token for stream resolution.
     */
    val subEpisodes: List<Episode> = emptyList(),

    /**
     * Episodes available in dubbed audio.
     *
     * ***Optional***. Separate episode list for dubbed viewers.
     * May be empty if no dub is available. Independent from [subEpisodes].
     */
    val dubEpisodes: List<Episode> = emptyList(),

    /**
     * Current broadcast or release status.
     *
     * ***Optional***. Defaults to [ShowStatus.Unknown].
     * Examples: "Airing", "Completed", "Upcoming".
     * Displayed in the detail header metadata.
     */
    val showStatus: ShowStatus = ShowStatus.Unknown,

    /**
     * Original Japanese title.
     *
     * ***Optional***. Displayed alongside the English title (e.g., "Shingeki no Kyojin").
     * Null when not provided by the source.
     */
    val japaneseTitle: String? = null,

    /**
     * MyAnimeList series ID.
     *
     * ***Optional***. Used for cross-referencing external metadata.
     * Null when unavailable. Do **not** use as primary key; use [url] instead.
     */
    val malId: Int? = null,

    /**
     * AniList series ID.
     *
     * ***Optional***. Used for cross-referencing external metadata.
     * Null when unavailable. Do **not** use as primary key; use [url] instead.
     */
    val anilistId: Int? = null,
) : ContentDetail()
