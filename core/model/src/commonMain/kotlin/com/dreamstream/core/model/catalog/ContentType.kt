package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/**
 * High-level classification of a piece of media content.
 *
 * [ContentType] defines the primary category of content for filtering,
 * navigation, and provider capability declaration. It is used in:
 * - ***`SearchResult.type`*** to classify search results
 * - ***`ContentDetail.type`*** to classify full content metadata
 * - ***`ContentProvider.supportedTypes`*** to declare what a provider supports
 *
 * This enum distinguishes between **episodic** content (structured as episodes)
 * and **standalone** content (single unit).
 *
 * ## Content Types:
 * | Type | Classification | Examples |
 * |------|----------------|----------|
 * | [TvSeries] | ***Episodic*** | "Stranger Things", "Breaking Bad" |
 * | [Anime] | ***Episodic*** | "Attack on Titan", "One Piece" |
 * | [Movie] | ***Standalone*** | "Inception", "Dune" |
 * | [AnimeMovie] | ***Standalone*** | "Spirited Away", "Your Name" |
 * | [Documentary] | ***Standalone*** | "Our Planet", "Free Solo" |
 * | [Live] | ***Standalone*** | "CNN", "ESPN Live" |
 * | [Music] | ***Standalone*** | Music videos, concert recordings |
 * | [Others] | ***Standalone*** | Unclassified content |
 *
 * ## Check if episodic:
 * Use [isEpisodic] to determine if content has episodes:
 * ```kotlin
 * if (contentType.isEpisodic) {
 *     // Show season/episode selector
 * } else {
 *     // Show direct play button
 * }
 * ```
 *
 * ## Usage in Provider:
 * ```kotlin
 * class AnimeProvider : ContentProvider() {
 *     override val supportedTypes = setOf(ContentType.Anime, ContentType.AnimeMovie)
 * }
 * ```
 *
 * ## Related:
 * - Used in:
 *      [com.dreamstream.core.model.search.SearchResult]
 *      [com.dreamstream.core.model.detail.ContentDetail]
 * - Computed property: [isEpisodic]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
enum class ContentType {
    /**
     * Feature film (movie).
     *
     * ***Standalone*** content with a single runtime.
     * Examples: "Inception", "The Matrix", "Dune: Part Two"
     */
    Movie,

    /**
     * Television series (live-action or animated).
     *
     * ***Episodic*** content with multiple seasons and episodes.
     * Examples: "Stranger Things", "Breaking Bad", "The Office"
     */
    TvSeries,

    /**
     * Japanese animated series (anime).
     *
     * ***Episodic*** content, often with sub/dub episode tracks.
     * Examples: "Attack on Titan", "One Piece", "Demon Slayer"
     */
    Anime,

    /**
     * Japanese animated film (anime movie).
     *
     * ***Standalone*** animated feature.
     * Examples: "Spirited Away", "Your Name", "A Silent Voice"
     */
    AnimeMovie,

    /**
     * Documentary film or series.
     *
     * ***Standalone*** non-fiction content.
     * Examples: "Our Planet", "The Social Dilemma", "Free Solo"
     */
    Documentary,

    /**
     * Live TV channel or live stream.
     *
     * ***Standalone*** real-time broadcast content.
     * Examples: "CNN", "ESPN", "NBA Live Stream"
     */
    Live,

    /**
     * Music video or concert recording.
     *
     * ***Standalone*** audio-visual music content.
     * Examples: "Taylor Swift - Anti-Hero", "BTS Live Concert"
     */
    Music,

    /**
     * Unclassified or other content types.
     *
     * ***Standalone*** fallback for unrecognized content.
     */
    Others;

    /**
     * Whether this content type is episodic (structured as episodes).
     *
     * Returns ***true*** for:
     * - [TvSeries]
     * - [Anime]
     *
     * Returns ***false*** for all other types.
     *
     * Use this to determine UI behavior:
     * ```kotlin
     * if (type.isEpisodic) {
     *     // Show episode/season selector
     * } else {
     *     // Show single play button
     * }
     * ```
     */
    val isEpisodic: Boolean
        get() = this == TvSeries || this == Anime
}
