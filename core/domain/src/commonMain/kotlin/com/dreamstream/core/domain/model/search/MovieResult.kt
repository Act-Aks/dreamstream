package com.dreamstream.core.domain.model.search

import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Quality
import kotlinx.serialization.Serializable

/**
 * Search-level result for a movie.
 *
 * [MovieResult] represents a single movie entry returned by a provider's
 * ***`ContentProvider.search(query)`*** method. It is used to populate the
 * **Search Results** screen before the user selects a title to view details.
 *
 * This class contains:
 * - **Identity**: [name], [url], [providerId]
 * - **Media**: [posterUrl], [type], [year]
 * - **Quality**: [quality] ( the best available at search time)
 * - **Rating**: [rating] (0–10 scale)
 *
 * ## Key Properties:
 * - [name]: Movie title (***required***)
 * - [url]: Deep link to detail page (***required***)
 * - [posterUrl]: Poster image (***optional***)
 * - [providerId]: Source plugin ID (***required***)
 * - [year]: Release year (***optional***)
 * - [quality]: Best stream quality (***optional, defaults to [Quality.Unknown]***)
 * - [rating]: Provider rating 0–10 (***optional***)
 *
 * ## Stream Quality:
 * [quality] reflects the **best stream quality** reported by the provider for
 * this title at search time. It defaults to [Quality.Unknown] when the provider
 * does not declare a quality level.
 *
 * **Do not treat this as authoritative** — always confirm quality when resolving
 * streams before showing quality indicators to the user:
 * ```kotlin
 * // Search time – use quality for quick UI hints
 * val result = movieResult
 * if (result.quality == Quality.Android) showHD badge()
 *
 * // Before playing – verify actual available qualities
 * val streams = contentProvider.loadStreams(movieResult.url)
 * val bestQuality = streams.maxByOrNull { it.quality.rank }?.quality
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val results = provider.search("Inception")
 *
 * results.forEach { movie ->
 *     println("${movie.name} (${movie.year})")
 *     movie.quality.let { q -> if (q != Quality.Unknown) println("  Best: $q") }
 *     movie.rating?.let { println("  Rating: $it/10") }
 * }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the **Search Results** list/grid
 * - [name] shown as title
 * - [posterUrl] displayed as poster thumbnail (lazy-loaded)
 * - [year] shown in subtitle (e.g., "2010")
 * - [quality] shown as quality badge (e.g., "HD", "4K") if not [Quality.Unknown]
 * - [rating] shown as star rating (e.g., "★ 8.8")
 * - [providerId] shown in small text or source icon
 *
 * ## Search Flow:
 * 1. User enters query → `searchViewModel.search(query)`
 * 2. Parallel search across all enabled providers
 * 3. Collect [MovieResult] from each provider
 * 4. Merge and deduplicate by [name] + [year]
 * 5. Display in unified **Search Results** screen
 * 6. User taps result → navigate to ***`MovieDetailScreen`*** with [url]
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
 * - Live result: [LiveResult]
 * - Detail model: ***`MovieDetail`***
 * - Provider method: ***`ContentProvider.search(query)`***
 * - Content type: [ContentType.Movie]
 * - Quality enum: [Quality]
 *
 * @property year Release year of the movie, or null when unknown.
 * @property quality Best available stream quality reported by the provider.
 * @property rating Raw provider rating on a 0–10 scale, or null when unavailable.
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class MovieResult(
    /**
     * Movie title.
     *
     * ***Required***. Display name shown in search results.
     * Example: `"Inception"`, `"The Dark Knight"`
     */
    override val name: String,

    /**
     * Deep link to the movie detail page.
     *
     * ***Required***. URL used to load [com.dreamstream.core.model.detail.MovieDetail]
     * when user taps this result. Provider-specific format.
     * Example: `"movie-provider://inception"`
     */
    override val url: String,

    /**
     * Poster image URL.
     *
     * ***Optional***. Null if unavailable. Lazy-loaded in search results list.
     * Example: `"https://cdn.example.com/posters/inception.jpg"`
     */
    override val posterUrl: String? = null,

    /**
     * Content type.
     *
     * ***Required***. Always [ContentType.Movie] for this class.
     * Used for filtering and routing in search results.
     */
    override val type: ContentType = ContentType.Movie,

    /**
     * Provider/plugin ID that returned this result.
     *
     * ***Required***. Identifies which plugin provided this movie.
     * Used for deduplication, source indication, and error tracking.
     * Example: `"movie-provider"`, `"netflix-plugin"`
     */
    override val providerId: String,

    /**
     * Release year of the movie.
     *
     * ***Optional***. Null when unknown. Displayed in search results subtitle.
     * Example: `2010`, `2008`, `1994`
     */
    val year: Int? = null,

    /**
     * Best available stream quality reported by the provider.
     *
     * ***Optional***. Defaults to [Quality.Unknown]. Indicates the highest
     * quality stream available for this movie (e.g., [Quality.Auto], [Quality.FullHd]).
     * May not be accurate for all sources — verify when resolving streams.
     * Used to show quality badge (e.g., "HD", "4K") in search results.
     */
    val quality: Quality = Quality.Unknown,

    /**
     * Raw provider rating on a 0–10 scale.
     *
     * ***Optional***. Null when unavailable. Displayed as star rating in search results.
     * Example: `8.8f`, `9.0f`, `7.2f`
     */
    val rating: Float? = null,
) : SearchResult()
