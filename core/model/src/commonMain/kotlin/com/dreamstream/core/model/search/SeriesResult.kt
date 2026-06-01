package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Search-level result for a TV series.
 *
 * [SeriesResult] represents a single TV series entry returned by a provider's
 * ***`ContentProvider.search(query)`*** method. It is used to populate the
 * **Search Results** screen before the user selects a title to view details.
 *
 * This class contains:
 * - **Identity**: [name], [url], [providerId]
 * - **Media**: [posterUrl], [type], [year]
 * - **Structure**: [totalSeasons] (season count at search time)
 * - **Rating**: [rating] (0–10 scale)
 *
 * ## Key Properties:
 * - [name]: Series title (***required***)
 * - [url]: Deep link to detail page (***required***)
 * - [posterUrl]: Poster image (***optional***)
 * - [providerId]: Source plugin ID (***required***)
 * - [year]: First air year (***optional***)
 * - [totalSeasons]: Season count (***optional, may be inaccurate for ongoing***)
 * - [rating]: Provider rating 0–10 (***optional***)
 *
 * ## Season Count Accuracy:
 * [totalSeasons] is the **provider-reported season count** at search time and
 * may be `null` or inaccurate for:
 * - **Ongoing series**: New seasons may not be reflected yet
 * - **Newly added series**: Metadata may be incomplete
 *
 * Full episode and season structure is available on the corresponding
 * [com.dreamstream.core.model.detail.SeriesDetail] after loading:
 * ```kotlin
 * // Search time – use totalSeasons for quick hints
 * val result = seriesResult
 * result.totalSeasons?.let { println("Seasons: $it") }
 *
 * // Before playing – load full structure
 * val detail = contentProvider.loadSeriesDetail(seriesResult.url)
 * val seasons = detail.seasons // List<Season> with episodes
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val results = provider.search("Breaking Bad")
 *
 * results.forEach { series ->
 *     println("${series.name} (${series.year})")
 *     series.totalSeasons?.let { println("  Seasons: $it") }
 *     series.rating?.let { println("  Rating: $it/10") }
 * }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the **Search Results** list/grid
 * - [name] shown as title
 * - [posterUrl] displayed as poster thumbnail (lazy-loaded)
 * - [year] shown in subtitle (e.g., "2008")
 * - [totalSeasons] shown (e.g., "5 seasons")
 * - [rating] shown as star rating (e.g., "★ 9.5")
 * - [providerId] shown in small text or source icon
 *
 * ## Search Flow:
 * 1. User enters query → `searchViewModel.search(query)`
 * 2. Parallel search across all enabled providers
 * 3. Collect [SeriesResult] from each provider
 * 4. Merge and deduplicate by [name] + [year]
 * 5. Display in unified **Search Results** screen
 * 6. User taps result → navigate to ***`SeriesDetailScreen`*** with [url]
 *
 * ## Deduplication Example:
 * ```kotlin
 * val merged = allResults
 *     .groupBy { it.name.lowercase() to it.year }
 *     .mapValues { (_, list) ->
 *         list.maxByOrNull { it.rating ?: 0f } ?: list.first()
 *     }
 *     .values.toList()
 * ```
 *
 * ## Related:
 * - Base class: [SearchResult]
 * - Anime result: [AnimeResult]
 * - Movie result: [MovieResult]
 * - Live result: [LiveResult]
 * - Detail model: [com.dreamstream.core.model.detail.SeriesDetail]
 * - Provider method: ***`ContentProvider.search(query)`***
 * - Content type: [ContentType.TvSeries]
 * - Extension: [year] (computed property)
 * - Extension: [rating] (computed property)
 * - Extension: [displayRating] (formatted string)
 *
 * @property year Year the series first aired, or null when unknown.
 * @property totalSeasons Total number of seasons reported by the provider at
 *   search time, or null when not available.
 * @property rating Raw provider rating on a 0–10 scale, or null when unavailable.
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class SeriesResult(
    /**
     * TV series title.
     *
     * ***Required***. Display name shown in search results.
     * Example: `"Breaking Bad"`, `"The Office"`, `"Stranger Things"`
     */
    override val name: String,

    /**
     * Deep link to the series detail page.
     *
     * ***Required***. URL used to load [com.dreamstream.core.model.detail.SeriesDetail]
     * when user taps this result. Provider-specific format.
     * Example: `"series-provider://breaking-bad"`
     */
    override val url: String,

    /**
     * Poster image URL.
     *
     * ***Optional***. Null if unavailable. Lazy-loaded in search results list.
     * Example: `"https://cdn.example.com/posters/breaking-bad.jpg"`
     */
    override val posterUrl: String? = null,

    /**
     * Content type.
     *
     * ***Required***. Always [ContentType.TvSeries] for this class.
     * Used for filtering and routing in search results.
     */
    override val type: ContentType = ContentType.TvSeries,

    /**
     * Provider/plugin ID that returned this result.
     *
     * ***Required***. Identifies which plugin provided this series.
     * Used for deduplication, source indication, and error tracking.
     * Example: `"series-provider"`, `"netflix-plugin"`
     */
    override val providerId: String,

    /**
     * Year the series first aired.
     *
     * ***Optional***. Null when unknown. Displayed in search results subtitle.
     * Example: `2008`, `2013`, `2022`
     */
    val year: Int? = null,

    /**
     * Total number of seasons reported by the provider at search time.
     *
     * ***Optional***. Null when not available. May be inaccurate for ongoing
     * or newly added series. Displayed in search results (e.g., "5 seasons").
     * Full season/episode structure is available in [com.dreamstream.core.model.detail.SeriesDetail].
     * Example: `5`, `10`, `2`
     */
    val totalSeasons: Int? = null,

    /**
     * Raw provider rating on a 0–10 scale.
     *
     * ***Optional***. Null when unavailable. Displayed as star rating in search results.
     * Example: `9.5f`, `8.7f`, `7.2f`
     */
    val rating: Float? = null,
) : SearchResult()
