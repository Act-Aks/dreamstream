package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Base type for all search results returned by a provider plugin.
 *
 * [SearchResult] is a lightweight list-level model — it carries just enough
 * information to render a content card in **Search Results**, **Home Sections**,
 * and **Recommendation Rows**. For full metadata (episodes, streams, description),
 * load the corresponding [com.dreamstream.core.model.detail.ContentDetail] using [url].
 *
 * This sealed class has four concrete subtypes:
 * - [AnimeResult]: Anime titles with episode count, year, sub/dub flags
 * - [MovieResult]: Movies with release year, quality, rating
 * - [SeriesResult]: TV series with season/episode info
 * - [LiveResult]: Live channels with language and tags
 *
 * ## Key Properties:
 * - [name]: Display title (***required***)
 * - [url]: Provider-side lookup key for navigation (***required***)
 * - [posterUrl]: Thumbnail/poster image (***optional***)
 * - [type]: Content type ([ContentType.Anime], [ContentType.Movie], etc.) (***required***)
 * - [providerId]: Source plugin ID (***required***)
 * - [id]: Stable composite identifier for diffing/caching (***computed***)
 *
 * ## Navigation vs Caching:
 * | Property | Purpose | Example |
 * |----------|---------|---------|
 * | [url] | **Navigation key** to detail screen | `"anime-provider://attack-on-titan"` |
 * | [id] | **Cache/diff key** within provider results | `"anime-provider_-123456789"` |
 *
 * **Never use [id] as the navigation key** — always use [url] when navigating
 * to the detail screen:
 * ```kotlin
 * // Correct: Navigate using url
 * navigator.navigateToDetail(result.url)
 *
 * // Correct: Use id for RecyclerView diffing
 * RecyclerAdapter(result.id, result)
 *
 * // Incorrect: Don't use id for navigation
 * navigator.navigateToDetail(result.id) // ❌
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val results = provider.search("One Piece")
 *
 * results.forEach { result ->
 *     println("${result.name} (${result.type}) from ${result.providerId}")
 *     result.posterUrl?.let { loadPoster(it) }
 * }
 *
 * // Navigate to detail
 * val first = results.first()
 * navigator.navigateToDetail(first.url) // Use url, not id
 * ```
 *
 * ## Rendering Search Cards:
 * ```kotlin
 * @Composable
 * fun SearchCard(result: SearchResult) {
 *     AsyncImage(
 *         model = result.posterUrl,
 *         contentDescription = result.name
 *     )
 *     Text(text = result.name)
 *     Text(text = result.type.name)
 *     if (result is AnimeResult) {
 *         if (result.dubAvailable) Badge("DUB")
 *     }
 * }
 * ```
 *
 * ## Provider-Specific DTO Mapping:
 * Provider plugins return their own DTOs in the **data layer**. Map them to
 * [SearchResult] subtypes before reaching domain/presentation layers:
 * ```kotlin
 * // Data layer: Provider-specific DTO
 * data class ProviderAnimeDto(
 *     val title: String,
 *     val link: String,
 *     val image: String?,
 *     val year: Int?
 * )
 *
 * // Map to domain SearchResult
 * fun toAnimeResult(dto: ProviderAnimeDto, providerId: String): AnimeResult {
 *     return AnimeResult(
 *         name = dto.title,
 *         url = dto.link,
 *         posterUrl = dto.image,
 *         providerId = providerId,
 *         year = dto.year
 *     )
 * }
 * ```
 *
 * ## Compound Identifier:
 * [id] is computed as `"${providerId}_${url.hashCode()}"`:
 * - Stable within a single provider's result set
 * - Suitable for RecyclerView keys, lazy-list keys, cache keys
 * - Not globally unique across providers (same anime from different providers
 *   will have different [id] due to different [providerId])
 *
 * ```kotlin
 * val result = AnimeResult(
 *     name = "Attack on Titan",
 *     url = "anime-provider://aot",
 *     providerId = "anime-provider"
 * )
 * result.id // "anime-provider_-123456789" (hashCode of url)
 * ```
 *
 * ## Related Subtypes:
 * - [AnimeResult]: Anime with year, episodes, sub/dub flags
 * - [MovieResult]: Movies with year, quality, rating
 * - [SeriesResult]: TV series with season info
 * - [LiveResult]: Live channels with language, tags
 *
 * ## Related:
 * - Anime result: [AnimeResult]
 * - Movie result: [MovieResult]
 * - Series result: [SeriesResult]
 * - Live result: [LiveResult]
 * - Detail model: [com.dreamstream.core.model.detail.ContentDetail]
 * - Content type: [ContentType]
 *
 * @see MovieResult
 * @see SeriesResult
 * @see AnimeResult
 * @see LiveResult
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
sealed class SearchResult {
    /**
     * Display title of the content.
     *
     * ***Required***. Shown as the main text in search result cards.
     * Example: `"Attack on Titan"`, `"Inception"`, `"CNN Live"`
     */
    abstract val name: String

    /**
     * Provider-side URL for navigation to detail screen.
     *
     * ***Required***. Use this as the **navigation key** when loading
     * [com.dreamstream.core.model.detail.ContentDetail]. Provider-specific format.
     * Example: `"anime-provider://attack-on-titan"`, `"movie://inception"`
     */
    abstract val url: String

    /**
     * Poster/thumbnail image URL.
     *
     * ***Optional***. Null if unavailable. Used for displaying content artwork
     * in search results, home sections, and recommendation rows.
     * Example: `"https://cdn.example.com/posters/aot.jpg"`
     */
    abstract val posterUrl: String?

    /**
     * Content type (Anime, Movie, Series, Live).
     *
     * ***Required***. Used for filtering, routing, and UI differentiation.
     * Always matches the concrete subtype:
     * - [AnimeResult] → [ContentType.Anime]
     * - [MovieResult] → [ContentType.Movie]
     * - [SeriesResult] → [ContentType.TvSeries]
     * - [LiveResult] → [ContentType.Live]
     */
    abstract val type: ContentType

    /**
     * Provider/plugin ID that returned this result.
     *
     * ***Required***. Identifies which plugin provided this content.
     * Used for deduplication, source indication, error tracking, and [id] computation.
     * Example: `"anime-provider"`, `"crunchyroll-plugin"`, `"live-tv"`
     */
    abstract val providerId: String

    /**
     * Stable composite identifier for diffing and caching.
     *
     * ***Computed***. Format: `"${providerId}_${url.hashCode()}"`.
     * Suitable as:
     * - RecyclerView/LazyList key
     * - Cache key within a single provider's result set
     * - DiffUtil pinpoint key
     *
     * **Never use this as the navigation key** — always use [url] for navigation.
     *
     * Example:
     * ```kotlin
     * val result = AnimeResult(
     *     name = "One Piece",
     *     url = "anime-provider://one-piece",
     *     providerId = "anime-provider"
     * )
     * result.id // "anime-provider_1234567890"
     * ```
     */
    val id: String get() = "${providerId}_${url.hashCode()}"
}
