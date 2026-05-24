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
 * Use [url] as the stable content identifier when navigating between features.
 * Use [displayRating] for a formatted, user-facing rating string.
 *
 * Provider-specific DTOs should be mapped to subtypes of [ContentDetail] in
 * the data layer before reaching the domain or presentation layers.
 *
 * @see MovieDetail
 * @see SeriesDetail
 * @see AnimeDetail
 * @see LiveDetail
 */
@Serializable
sealed class ContentDetail {
    abstract val name: String
    abstract val url: String
    abstract val posterUrl: String?
    abstract val backgroundPosterUrl: String?
    abstract val type: ContentType
    abstract val year: Int?
    abstract val plot: String?
    abstract val rating: Float?
    abstract val tags: List<String>
    abstract val recommendations: List<SearchResult>
    abstract val actors: List<Actor>
    abstract val providerId: String
    abstract val trailerUrl: String?

    /**
     * Formatted rating string suitable for display, e.g. `"8.5"`.
     * Returns null when [rating] is unavailable.
     */
    val displayRating: String?
        get() = rating?.let { "%.1f".format(it) }
}
