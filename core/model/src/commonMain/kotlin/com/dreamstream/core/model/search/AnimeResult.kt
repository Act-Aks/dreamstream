package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Search-level result for an anime title.
 *
 * [AnimeResult] represents a single anime entry returned by a provider's
 * ***`ContentProvider.search(query)`*** method. It is used to populate the
 * **Search Results** screen before the user selects a title to view details.
 *
 * This class contains:
 * - **Identity**: [name], [url], [providerId]
 * - **Media**: [posterUrl], [type], [year], [totalEpisodes]
 * - **Audio Flags**: [subAvailable], [dubAvailable] (provider-level hints)
 * - **Rating**: [rating] (0–10 scale)
 *
 * ## Key Properties:
 * - [name]: Anime title (***required***)
 * - [url]: Deep link to detail page (***required***)
 * - [posterUrl]: Thumbnail image (***optional***)
 * - [providerId]: Source plugin ID (***required***)
 * - [subAvailable]: Subtitled track available (***optional, defaults to true***)
 * - [dubAvailable]: Dubbed track available (***optional, defaults to false***)
 * - [year]: Air year (***optional***)
 * - [totalEpisodes]: Episode count (***optional**_)
 * - [rating]: Provider rating 0–10 (***optional**_)
 *
 * ## Audio Availability Flags:
 * [subAvailable] and [dubAvailable] are **provider-level hints** reported at
 * search time. They may not be accurate for **all sources**:
 * - Provider might report `dubAvailable = true` but a specific episode lacks dub
 * - Always verify when resolving streams before showing track indicators
 *
 * ```kotlin
 * // Search time – use flags for quick UI hints
 * val result = animeResult
 * if (result.dubAvailable) showDubBadge()
 *
 * // Before playing – verify actual availability
 * val detail = contentProvider.loadAnimeDetail(animeResult.url)
 * val hasDub = detail.episodes.any { it.dubUrl != null }
 * ```
 *
 * Full sub/dub episode lists are available on the corresponding
 * [com.dreamstream.core.model.detail.AnimeDetail].
 *
 * ## Usage:
 * ```kotlin
 * val results = provider.search("Attack on Titan")
 *
 * results.forEach { anime ->
 *     println("${anime.name} (${anime.year}) - ${anime.totalEpisodes} eps")
 *     if (anime.dubAvailable) println("  Dub available")
 *     if (anime.subAvailable) println("  Sub available")
 *     anime.rating?.let { println("  Rating: $it/10") }
 * }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the **Search Results** list/grid
 * - [name] shown as title
 * - [posterUrl] displayed as thumbnail (lazy-loaded)
 * - [year] shown in subtitle (e.g., "2013")
 * - [totalEpisodes] shown (e.g., "25 eps")
 * - [dubAvailable] marked with "DUB" badge
 * - [subAvailable] marked with "SUB" badge (usually implied if true)
 * - [rating] shown as star rating (e.g., "★ 8.7")
 * - [providerId] shown in small text or source icon
 *
 * ## Search Flow:
 * 1. User enters query → `searchViewModel.search(query)`
 * 2. Parallel search across all enabled providers
 * 3. Collect [AnimeResult] from each provider
 * 4. Merge and deduplicate by [name] + [year]
 * 5. Display in unified **Search Results** screen
 * 6. User taps result → navigate to ***`AnimeDetailScreen`*** with [url]
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
 * - Detail model: ***`AnimeDetail`***
 * - Provider method: ***`ContentProvider.search(query)`***
 * - Content type: [ContentType.Anime]
 *
 * @property year Year the anime first aired, or null when unknown.
 * @property totalEpisodes Total episode count reported by the provider at
 *   search time, or null when not available.
 * @property dubAvailable True if a dubbed audio track is available for this title.
 * @property subAvailable True if a subtitled track is available for this title.
 * @property rating Raw provider rating on a 0–10 scale, or null when unavailable.
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class AnimeResult(
    /**
     * Anime title.
     *
     * ***Required***. Display name shown in search results.
     * Example: `"Attack on Titan"`, `"One Piece"`
     */
    override val name: String,

    /**
     * Deep link to the anime detail page.
     *
     * ***Required***. URL used to load [com.dreamstream.core.model.detail.AnimeDetail]
     * when user taps this result. Provider-specific format.
     * Example: `"anime-provider://attack-on-titan"`
     */
    override val url: String,

    /**
     * Poster/thumbnail image URL.
     *
     * ***Optional***. Null if unavailable. Lazy-loaded in search results list.
     * Example: `"https://cdn.example.com/posters/aot.jpg"`
     */
    override val posterUrl: String? = null,

    /**
     * Content type.
     *
     * ***Required***. Always [ContentType.Anime] for this class.
     * Used for filtering and routing in search results.
     */
    override val type: ContentType = ContentType.Anime,

    /**
     * Provider/plugin ID that returned this result.
     *
     * ***Required***. Identifies which plugin provided this anime.
     * Used for deduplication, source indication, and error tracking.
     * Example: `"anime-provider"`, `"crunchyroll-plugin"`
     */
    override val providerId: String,

    /**
     * Year the anime first aired.
     *
     * ***Optional***. Null when unknown. Displayed in search results subtitle.
     * Example: `2013`, `2022`, `1999`
     */
    val year: Int? = null,

    /**
     * Total episode count reported by the provider at search time.
     *
     * ***Optional***. Null when not available. Displayed in search results.
     * May be inaccurate for ongoing series.
     * Example: `25`, `1000`, `12`
     */
    val totalEpisodes: Int? = null,

    /**
     * True if a dubbed audio track is available for this title.
     *
     * ***Optional***. Defaults to `false`. Provider-level hint at search time.
     * May not be accurate for all episodes — verify in ***`AnimeDetail`*** before playing.
     * Used to show "DUB" badge in search results.
     */
    val dubAvailable: Boolean = false,

    /**
     * True if a subtitled track is available for this title.
     *
     * ***Optional***. Defaults to `true`. Provider-level hint at search time.
     * May not be accurate for all episodes — verify in ***`AnimeDetail`*** before playing.
     * Usually implied if `true`, so badge may not always show.
     */
    val subAvailable: Boolean = true,

    /**
     * Raw provider rating on a 0–10 scale.
     *
     * ***Optional***. Null when unavailable. Displayed as star rating in search results.
     * Example: `8.7f`, `9.2f`, `7.5f`
     */
    val rating: Float? = null,
) : SearchResult()
