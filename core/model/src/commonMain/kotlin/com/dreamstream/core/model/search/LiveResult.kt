package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Search-level result for a live channel or stream.
 *
 * [LiveResult] represents a live TV channel, streaming channel, or continuous
 * broadcast returned by a provider's ***`ContentProvider.search(query)`*** method.
 * It is used to populate the **Search Results** screen for live content.
 *
 * Unlike [AnimeResult], live content does not carry [year] or [rating] at the
 * search level. Metadata is typically minimal and channel-specific — expect most
 * optional fields to be `null` for live sources.
 *
 * This class contains:
 * - **Identity**: [name], [url], [providerId]
 * - **Media**: [posterUrl], [type]
 * - **Channel Info**: [lang], [tags]
 *
 * ## Key Properties:
 * - [name]: Channel name (***required***)
 * - [url]: Deep link to stream/detail (***required***)
 * - [posterUrl]: Channel logo/thumbnail (***optional***)
 * - [providerId]: Source plugin ID (***required**_)
 * - [lang]: Language tag (***optional**_)
 * - [tags]: Category/genre tags (***optional, defaults to empty**_)
 *
 * ## Metadata Differences from Anime:
 * | Property | AnimeResult | LiveResult |
 * |----------|-------------|------------|
 * | [year] | Optional | **Not available** |
 * | [rating] | Optional | **Not available** |
 * | [totalEpisodes] | Optional | **Not available** |
 * | [lang] | Not used | Optional (channel language) |
 * | [tags] | Not used | Optional (genre categories) |
 *
 * ## Audio Language:
 * [lang] is the BCP 47 language tag for the channel's primary language:
 * - Example: `"en"` (English), `"ja"` (Japanese), `"es"` (Spanish), `"pt-BR"`
 * - `null` when not declared by the provider
 * - Used for language filtering in search results
 *
 * ## Tags/Categories:
 * [tags] are provider-assigned category or genre tags:
 * - Example: `["News", "Sports"]`, `["Anime", "Animation"]`, `["Music"]`
 * - Empty list if no tags provided
 * - Used for filtering and channel discovery
 *
 * ## Usage:
 * ```kotlin
 * val results = provider.search("News")
 *
 * results.filterIsInstance<LiveResult>().forEach { channel ->
 *     println("${channel.name} - ${channel.lang ?: "Unknown lang"}")
 *     if (channel.tags.isNotEmpty()) println("  Tags: ${channel.tags.joinToString()}")
 * }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the **Search Results** list/grid (mixed with [AnimeResult])
 * - [name] shown as channel name
 * - [posterUrl] displayed as channel logo (often square)
 * - [lang] shown as language badge (e.g., "EN", "JA") if not null
 * - [tags] shown as small category chips (e.g., "News", "Sports")
 * - Live badge (●) or "LIVE" indicator always shown
 * - [providerId] shown as source icon or small text
 *
 * ## Search Flow:
 * 1. User enters query → `searchViewModel.search(query)`
 * 2. Parallel search across all enabled providers
 * 3. Collect [AnimeResult] and [LiveResult] from each provider
 * 4. Merge results, optionally filter by [ContentType]
 * 5. Display unified **Search Results** screen
 * 6. User taps result → navigate to stream player or ***`LiveDetailScreen`***
 *
 * ## Filtering Example:
 * ```kotlin
 * // Show only live channels
 * val liveOnly = allResults.filterIsInstance<LiveResult>()
 *
 * // Show only English channels
 * val englishOnly = liveOnly.filter { it.lang == "en" }
 *
 * // Show only News channels
 * val newsOnly = liveOnly.filter { it.tags.contains("News") }
 * ```
 *
 * ## Related:
 * - Base class: [SearchResult]
 * - Anime result: [AnimeResult]
 * - Provider method: ***`ContentProvider.search(query)`***
 * - Content type: [ContentType.Live]
 * - Detail model: ***`LiveDetail`*** (if applicable)
 *
 * @property lang BCP 47 language tag for the channel's primary language
 *   (e.g. `"en"`, `"ja"`), or null when not declared by the provider.
 * @property tags Provider-assigned category or genre tags for the channel
 *   (e.g. `["News", "Sports"]`).
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class LiveResult(
    /**
     * Channel name.
     *
     * ***Required***. Display name shown in search results.
     * Example: `"CNN Live"`, `"AnimeSama"`, `"ESPN"`
     */
    override val name: String,

    /**
     * Deep link to the live stream or channel detail page.
     *
     * ***Required***. URL used to load the stream when user taps this result.
     * Provider-specific format. May point directly to stream URL or detail page.
     * Example: `"live-provider://cnn-live"`, `"channel://espn"`
     */
    override val url: String,

    /**
     * Channel logo/thumbnail image URL.
     *
     * ***Optional***. Null if unavailable. Often a square logo for channels.
     * Lazy-loaded in search results list.
     * Example: `"https://cdn.example.com/logos/cnn.png"`
     */
    override val posterUrl: String? = null,

    /**
     * Content type.
     *
     * ***Required***. Always [ContentType.Live] for this class.
     * Used for filtering and routing in search results.
     */
    override val type: ContentType = ContentType.Live,

    /**
     * Provider/plugin ID that returned this result.
     *
     * ***Required***. Identifies which plugin provided this channel.
     * Used for deduplication, source indication, and error tracking.
     * Example: `"live-provider"`, `"tv-plugin"`
     */
    override val providerId: String,

    /**
     * BCP 47 language tag for the channel's primary language.
     *
     * ***Optional***. Null when not declared by the provider.
     * Example: `"en"` (English), `"ja"` (Japanese), `"es"` (Spanish), `"pt-BR"`
     * Used for language filtering and UI language badges.
     */
    val lang: String? = null,

    /**
     * Provider-assigned category or genre tags for the channel.
     *
     * ***Optional***. Defaults to empty list. Example: `["News", "Sports"]`,
     * `["Anime", "Animation"]`, `["Music", "Entertainment"]`.
     * Used for filtering and channel discovery in the plugin store.
     */
    val tags: List<String> = emptyList(),
) : SearchResult()
