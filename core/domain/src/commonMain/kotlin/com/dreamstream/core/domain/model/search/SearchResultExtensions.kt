package com.dreamstream.core.domain.model.search

/**
 * Year of release for a [SearchResult].
 *
 * ***Computed property***. Returns the release/air year for content types that
 * have one. Available on [MovieResult], [SeriesResult], and [AnimeResult].
 * Returns `null` for [LiveResult], which does not carry a release year.
 *
 * ## Usage:
 * ```kotlin
 * val result: SearchResult = getSearchResult()
 *
 * val year = result.year // Int?
 * if (year != null) {
 *     Text(text = "Released: $year")
 * }
 * ```
 *
 * ## Behavior by Type:
 * | Type | Returns |
 * |------|---------|
 * | [MovieResult] | [MovieResult.year] |
 * | [SeriesResult] | [SeriesResult.year] |
 * | [AnimeResult] | [AnimeResult.year] |
 * | [LiveResult] | `null` |
 *
 * ## UI Example:
 * ```kotlin
 * @Composable
 * fun SearchCardSubtitle(result: SearchResult) {
 *     Row {
 *         result.year?.let { Text(text = it.toString()) }
 *         if (result.year != null && result is MovieResult && result.quality != Quality.Unknown) {
 *             Text(text = " • ")
 *             Text(text = result.quality.name)
 *         }
 *     }
 * }
 * ```
 *
 * @see MovieResult.year
 * @see SeriesResult.year
 * @see AnimeResult.year
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
 * ***Computed property***. Returns the raw provider rating on a 0–10 scale for
 * content types that have one. Available on [MovieResult], [SeriesResult], and
 * [AnimeResult]. Returns `null` for [LiveResult], which does not carry a rating.
 *
 * ## Usage:
 * ```kotlin
 * val result: SearchResult = getSearchResult()
 *
 * val rating = result.rating // Float?
 * if (rating != null) {
 *     StarRating(rating = rating)
 * }
 * ```
 *
 * ## Behavior by Type:
 * | Type | Returns |
 * |------|---------|
 * | [MovieResult] | [MovieResult.rating] |
 * | [SeriesResult] | [SeriesResult.rating] |
 * | [AnimeResult] | [AnimeResult.rating] |
 * | [LiveResult] | `null` |
 *
 * ## UI Example:
 * ```kotlin
 * @Composable
 * fun SearchCardRating(result: SearchResult) {
 *     result.displayRating.ifEmpty {
 *         // Show placeholder or hide entirely
 *         return@ifEmpty Text(text = "No rating")
 *     }
 *     Text(text = "★ ${result.displayRating}")
 * }
 * ```
 *
 * @see MovieResult.rating
 * @see SeriesResult.rating
 * @see AnimeResult.rating
 * @see SearchResult.displayRating
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
 * ***Computed property***. Formats [rating] to one decimal place (`"%.1f"`).
 * Returns an empty string `""` when [rating] is unavailable, so callers can
 * use this directly in UI models without a null check.
 *
 * ## Usage:
 * ```kotlin
 * val result: SearchResult = getSearchResult()
 *
 * // Directly use in UI without null check
 * Text(text = result.displayRating) // "8.5" or ""
 * ```
 *
 * ## Formatting Examples:
 * | [rating] | [displayRating] |
 * |----------|-----------------|
 * | `8.56f` | `"8.6"` |
 * | `8.54f` | `"8.5"` |
 * | `10.0f` | `"10.0"` |
 * | `0.0f` | `"0.0"` |
 * | `null` | `""` |
 *
 * ## UI Example:
 * ```kotlin
 * @Composable
 * fun RatingBadge(result: SearchResult) {
 *     if (result.displayRating.isNotEmpty()) {
 *         Badge {
 *             Text(text = "★ ${result.displayRating}")
 *         }
 *     }
 * }
 * ```
 *
 * @see SearchResult.rating
 */
val SearchResult.displayRating: String
    get() = rating?.let { "%.1f".format(it) } ?: ""
