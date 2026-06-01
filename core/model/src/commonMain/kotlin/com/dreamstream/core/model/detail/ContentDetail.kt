package com.dreamstream.core.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full metadata for a single piece of content loaded from a provider plugin.
 *
 * [ContentDetail] is the rich detail-level model used to render the detail
 * screen. Unlike [SearchResult], which is a lightweight list item, [ContentDetail]
 * carries the plot, backdrop, cast, genres (tags), recommendations, and
 * type-specific extras such as episodes, seasons, duration, and stream tokens.
 *
 * This sealed class defines the common schema for all content types:
 * - [MovieDetail]: Feature films
 * - [SeriesDetail]: TV shows (live-action)
 * - [AnimeDetail]: Japanese animation (sub/dub)
 * - [LiveDetail]: Live TV channels
 *
 * Use [url] as the stable content identifier when navigating between features.
 * Use [displayRating] for a formatted, user-facing rating string.
 *
 * Provider-specific DTOs should be mapped to subtypes of [ContentDetail] in
 * the data layer before reaching the domain or presentation layers.
 *
 * ## Key Properties:
 * - [name]: Display title (***required***)
 * - [url]: Stable content identifier (***required***)
 * - [posterUrl]: Poster image (***optional***)
 * - [backgroundPosterUrl]: Background banner (***optional***)
 * - [type]: Content type (***required***)
 * - [plot]: Synopsis (***optional***)
 * - [rating]: Numeric rating (***optional***)
 * - [displayRating]: Formatted rating string (***computed***)
 * - [tags]: Genres/categories (***required***)
 * - [actors]: Cast members (***required***)
 * - [recommendations]: Similar content (***required***)
 * - [trailerUrl]: Trailer link (***optional***)
 * - [providerId]: Source provider ID (***required***)
 *
 * ## Usage:
 * ```kotlin
 * val content = loadContentDetail("/anime/attack-on-titan")
 * val title = content.name
 * val rating = content.displayRating // "8.7"
 * ```
 *
 * ## In Detail Screen:
 * ```kotlin
 * ContentDetail(
 *     name = "Breaking Bad",
 *     posterUrl = "https://example.com/poster.jpg",
 *     plot = "A chemistry teacher turns...",
 *     rating = 9.5f,
 *     tags = listOf("Drama", "Crime"),
 *     actors = listOf(Actor("Bryan Cranston", role = "Walter White")),
 *     recommendations = listOf(/* ... */)
 * )
 * ```
 *
 * ## Related Subtypes:
 * - [MovieDetail]: Movies
 * - [SeriesDetail]: TV series
 * - [AnimeDetail]: Anime (sub/dub)
 * - [LiveDetail]: Live channels
 *
 * ## Related:
 * - Lightweight list item: [SearchResult]
 * - Content type: [ContentType]
 * - Cast: [Actor]
 *
 * @see MovieDetail
 * @see SeriesDetail
 * @see AnimeDetail
 * @see LiveDetail
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
sealed class ContentDetail {

    /**
     * Display title of the content.
     *
     * ***Required***. Primary name shown in UI (e.g., "Breaking Bad", "Inception").
     */
    abstract val name: String

    /**
     * Original URL or ID used to load this content.
     *
     * ***Required***. Stable identifier for navigation, bookmarks, and history.
     * Use this instead of provider-specific IDs for cross-feature consistency.
     */
    abstract val url: String

    /**
     * URL to the poster image.
     *
     * ***Optional***. Displayed in catalog cards, search results, and detail header.
     */
    abstract val posterUrl: String?

    /**
     * URL to the background/banner image.
     *
     * ***Optional***. Displayed as the full-screen hero background in the detail screen.
     */
    abstract val backgroundPosterUrl: String?

    /**
     * Type of content.
     *
     * ***Required***. Determines UI layout (e.g., episode selector for series,
     * direct play for movies). Values: [ContentType.Movie], [ContentType.TvSeries],
     * [ContentType.Anime], [ContentType.Live], etc.
     */
    abstract val type: ContentType

    /**
     * Release or airing year.
     *
     * ***Optional***. Displayed in metadata (e.g., "2023"). Null if unknown.
     */
    abstract val year: Int?

    /**
     * Plot summary or description.
     *
     * ***Optional***. Displayed in the "About" section of the detail screen.
     */
    abstract val plot: String?

    /**
     * User or critic rating (numeric).
     *
     * ***Optional***. Raw score (e.g., 8.7 out of 10). Null if unavailable.
     * Use [displayRating] for formatted output.
     */
    abstract val rating: Float?

    /**
     * Genre tags or categories.
     *
     * ***Required***. List of genres (e.g., ["Action", "Thriller", "Drama"]).
     * Displayed as chips in the detail header. Empty list if none.
     */
    abstract val tags: List<String>

    /**
     * Cast members (actors, voice actors, crew).
     *
     * ***Required***. List of [Actor] instances. Displayed in the cast section.
     * Empty list if cast is unavailable.
     */
    abstract val actors: List<Actor>

    /**
     * Recommended similar content.
     *
     * ***Required***. List of [SearchResult] for "You May Also Like" section.
     * Empty list if no recommendations.
     */
    abstract val recommendations: List<SearchResult>

    /**
     * Trailer URL (optional).
     *
     * ***Optional***. Link to a trailer video. Displayed as a clickable button
     * in the detail screen. Null if no trailer available.
     */
    abstract val trailerUrl: String?

    /**
     * Provider-specific unique identifier.
     *
     * ***Required***. Identifies which provider plugin returned this content.
     * Used for provider-aware operations (e.g., reload, cache invalidation).
     */
    abstract val providerId: String

    /**
     * Formatted rating string suitable for display.
     *
     * ***Computed***. Returns a formatted version of [rating] (e.g., `"8.5"`).
     * Returns `null` when [rating] is unavailable.
     *
     * Example:
     * ```kotlin
     * rating = 8.7f → displayRating = "8.7"
     * rating = null → displayRating = null
     * ```
     */
    val displayRating: String?
        get() = rating?.let { "%.1f".format(it) }
}
