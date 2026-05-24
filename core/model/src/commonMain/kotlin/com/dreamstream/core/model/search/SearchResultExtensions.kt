package com.dreamstream.core.model.search

/**
 * Year of release for a [SearchResult].
 *
 * Available on [MovieResult], [SeriesResult], and [AnimeResult].
 * Returns null for [LiveResult], which does not carry a release year.
 */
val SearchResult.year: Int?
    get() = when (this) {
        is MovieResult -> year
        is SeriesResult -> year
        is AnimeResult -> year
        is LiveResult -> null
    }

/**
 * Audience rating for a [SearchResult].
 *
 * Available on [MovieResult], [SeriesResult], and [AnimeResult].
 * Returns null for [LiveResult], which does not carry a rating.
 */
val SearchResult.rating: Float?
    get() = when (this) {
        is MovieResult -> rating
        is SeriesResult -> rating
        is AnimeResult -> rating
        is LiveResult -> null
    }

/**
 * Formatted rating string ready for UI display, e.g. `"8.5"`.
 *
 * Returns an empty string when [rating] is unavailable so callers can use
 * this directly in UI models without a null check.
 */
val SearchResult.displayRating: String
    get() = rating?.let { "%.1f".format(it) } ?: ""
