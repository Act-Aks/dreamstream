package com.dreamstream.core.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for a movie.
 *
 * [MovieDetail] represents a complete metadata payload for a feature film.
 * It is the primary model returned by ***`ContentProvider.loadMovieDetail`*** for movie content.
 *
 * This class extends [ContentDetail] and adds movie-specific properties:
 * - **Playback token**: [dataUrl] (required, opaque token for stream resolution)
 * - **Runtime**: [duration] (total minutes)
 * - **Release status**: [comingSoon] (pre-release flag)
 *
 * ## Key Properties:
 * - [name]: Movie title (***required***)
 * - [url]: Stable content identifier (***required***)
 * - [dataUrl]: Playback token (***required***)
 * - [duration]: Runtime in minutes (***optional***)
 * - [comingSoon]: Pre-release flag (***optional***)
 * - All inherited properties from [ContentDetail]: [posterUrl], [plot], [rating], [actors], etc.
 *
 * ## Stream Resolution:
 * [dataUrl] is the provider-side playback URL or opaque data token used by
 * the stream resolver to obtain the final, playable media URL. It is **not**
 * a direct media URL and must be resolved via ***`ContentProvider.loadLinks`***
 * before playback begins.
 * ```kotlin
 * val links = provider.loadLinks(detail.dataUrl, type = ContentType.Movie)
 * ```
 *
 * ## Coming Soon:
 * [comingSoon] is `true` when the title is announced but not yet available for streaming.
 * In this state, the player should hide the "Play" button and show a "Notify Me" option.
 * ```kotlin
 * if (movie.comingSoon) {
 *     // Show pre-release UI
 * } else {
 *     // Show play button
 * }
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val movie = MovieDetail(
 *     name = "Dune: Part Two",
 *     url = "/movie/dune-part-two",
 *     dataUrl = "token_dune2_stream",
 *     year = 2024,
 *     duration = 166,
 *     tags = listOf("Sci-Fi", "Adventure"),
 *     actors = listOf(Actor("Timothée Chalamet", role = "Paul Atreides"))
 * )
 * ```
 *
 * ## In Content Detail:
 * ```kotlin
 * MovieDetail(
 *     name = "Inception",
 *     posterUrl = "https://example.com/poster.jpg",
 *     plot = "A thief who steals corporate secrets...",
 *     rating = 8.8f,
 *     dataUrl = "inception_token_xyz",
 *     duration = 148,
 *     comingSoon = false,
 *     tags = listOf("Sci-Fi", "Thriller"),
 *     actors = listOf(Actor("Leonardo DiCaprio", role = "Cobb"))
 * )
 * ```
 *
 * ## Related:
 * - Base class: [ContentDetail]
 * - Content type: ***`ContentType.Movie`***
 * - Stream resolution: ***`ContentProvider.loadLinks`***
 * - Live detail: [LiveDetail] (same [dataUrl] contract)
 * - Series detail: [SeriesDetail], [AnimeDetail]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class MovieDetail(
    /**
     * Movie title (display name).
     *
     * ***Required***. Primary title shown in UI (e.g., "Inception", "Dune").
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
     * Provider-side playback URL or opaque data token.
     *
     * ***Required***. Passed to ***`ContentProvider.loadLinks`*** to resolve actual
     * streaming URLs. **Not** a direct media URL — must be resolved before playback.
     *
     * Examples:
     * - Encoded token: `"movie_token_xyz123"`
     * - Relative path: `"/movie/inception/stream"`
     * - Full URL: `"https://provider.com/movie/inception"`
     */
    val dataUrl: String,

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
     * Content type (always [ContentType.Movie]).
     *
     * ***Optional***. Defaults to [ContentType.Movie].
     * Inherits from [ContentDetail.type].
     */
    override val type: ContentType = ContentType.Movie,

    /**
     * Release year.
     *
     * ***Optional***. Displayed in metadata (e.g., "2024").
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
     * ***Optional***. Displayed as a score (e.g., "8.8/10").
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
     * Recommended similar movies.
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
     * Total runtime in minutes.
     *
     * ***Optional***. Displayed in metadata (e.g., "148 min").
     * Null when unknown or not provided by the source.
     */
    val duration: Int? = null,

    /**
     * Pre-release flag.
     *
     * ***Optional***. `true` when the movie is announced but not yet available
     * for streaming. Defaults to `false`.
     *
     * When `true`, the player should hide the "Play" button and show
     * a "Notify Me" or "Coming Soon" UI instead.
     */
    val comingSoon: Boolean = false,
) : ContentDetail()
