package com.dreamstream.core.domain.model.detail

import com.dreamstream.core.domain.model.catalog.Actor
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for a live channel or stream.
 *
 * [LiveDetail] represents a live TV channel or continuous stream. It is the primary
 * model returned by ***`ContentProvider.loadLiveDetail`*** for live content.
 *
 * This class extends [ContentDetail] and adds live-specific properties:
 * - **Playback token**: [dataUrl] (required, opaque token for stream resolution)
 * - **Language**: [lang] (BCP 47 tag, e.g., "en", "ja")
 *
 * Live content typically has minimal metadata: [plot], [rating], and [year]
 * will commonly be null. Expect providers to omit most optional fields.
 *
 * ## Key Properties:
 * - [name]: Channel name (***required***)
 * - [url]: Stable content identifier (***required***)
 * - [dataUrl]: Playback token (***required***)
 * - [lang]: Primary language tag (***optional***)
 * - [posterUrl]: Channel logo (***optional***)
 * - All inherited properties from [ContentDetail]: [type], [tags], [actors], etc.
 *
 * ## Stream Resolution:
 * [dataUrl] is the provider-side playback URL or opaque data token used by
 * the stream resolver. It follows the same contract as [MovieDetail.dataUrl] —
 * it is **not** a direct media URL and must be resolved via ***`ContentProvider.loadLinks`***
 * before playback.
 * ```kotlin
 * val links = provider.loadLinks(detail.dataUrl, type = ContentType.Live)
 * ```
 *
 * ## Minimal Metadata:
 * Live content often lacks detailed metadata. Providers typically omit:
 * - [plot] (null)
 * - [rating] (null)
 * - [year] (null)
 * - [actors] (empty list)
 * Only [name], [url], [dataUrl], and [posterUrl] are commonly provided.
 *
 * ## Usage:
 * ```kotlin
 * val channel = LiveDetail(
 *     name = "CNN International",
 *     url = "/live/cnn-intl",
 *     dataUrl = "token_cnn_live",
 *     posterUrl = "https://example.com/cnn-logo.png",
 *     lang = "en"
 * )
 * ```
 *
 * ## In Content Detail:
 * ```kotlin
 * LiveDetail(
 *     name = "ESPN",
 *     posterUrl = "https://example.com/espn.jpg",
 *     dataUrl = "espn_stream_token_123",
 *     tags = listOf("Sports", "Live"),
 *     lang = "en",
 *     providerId = "theSportsProvider"
 * )
 * ```
 *
 * ## Related:
 * - Base class: [ContentDetail]
 * - Content type: ***`ContentType.Live`***
 * - Stream resolution: ***`ContentProvider.loadLinks`***
 * - Movie detail: [MovieDetail] (same [dataUrl] contract)
 * - Episode detail: [AnimeDetail], [SeriesDetail]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class LiveDetail(
    /**
     * Channel or stream name.
     *
     * ***Required***. Primary display name (e.g., "CNN", "ESPN Live").
     * Inherits from [ContentDetail.name].
     */
    override val name: String,

    /**
     * Stable content identifier URL.
     *
     * ***Required***. Used as the primary key for bookmarks, history, and playback.
     * Inherits from [ContentDetail.url].
     */
    override val url: String,

    /**
     * Provider-side playback URL or opaque data token.
     *
     * ***Required***. Passed to ***`ContentProvider.loadLinks`*** to resolve actual
     * streaming URLs. **Not** a direct media URL — must be resolved before playback.
     *
     * Examples:
     * - Encoded token: `"live_token_xyz123"`
     * - Relative path: `"/live/cnn/stream"`
     * - Full URL: `"https://provider.com/live/cnn"`
     */
    val dataUrl: String,

    /**
     * URL to the channel logo or poster.
     *
     * ***Optional***. Displayed in the live channel list and detail header.
     * Inherits from [ContentDetail.posterUrl].
     */
    override val posterUrl: String? = null,

    /**
     * URL to the background/banner image.
     *
     * ***Optional***. Displayed as the full-screen background in the detail screen.
     * Rarely provided for live channels.
     * Inherits from [ContentDetail.backgroundPosterUrl].
     */
    override val backgroundPosterUrl: String? = null,

    /**
     * Content type (always [ContentType.Live]).
     *
     * ***Optional***. Defaults to [ContentType.Live].
     * Inherits from [ContentDetail.type].
     */
    override val type: ContentType = ContentType.Live,

    /**
     * Year established or launched.
     *
     * ***Optional***. Rarely provided for live channels. Null if unavailable.
     * Inherits from [ContentDetail.year].
     */
    override val year: Int? = null,

    /**
     * Channel description or plot.
     *
     * ***Optional***. Rarely provided for live channels. Null if unavailable.
     * Inherits from [ContentDetail.plot].
     */
    override val plot: String? = null,

    /**
     * User or critic rating.
     *
     * ***Optional***. Rarely provided for live channels. Null if unavailable.
     * Inherits from [ContentDetail.rating].
     */
    override val rating: Float? = null,

    /**
     * Genre tags or categories.
     *
     * ***Optional***. Examples: ["Sports", "News", "Entertainment"].
     * Inherits from [ContentDetail.tags].
     */
    override val tags: List<String> = emptyList(),

    /**
     * Recommended similar channels.
     *
     * ***Optional***. Displayed in the "You May Also Like" section.
     * Inherits from [ContentDetail.recommendations].
     */
    override val recommendations: List<SearchResult> = emptyList(),

    /**
     * Cast members (host, anchors, crew).
     *
     * ***Optional***. Rarely provided for live channels. Empty list if unavailable.
     * Inherits from [ContentDetail.actors].
     */
    override val actors: List<Actor> = emptyList(),

    /**
     * Provider-specific unique identifier.
     *
     * ***Required***. Identifies which provider plugin returned this content.
     * Inherits from [ContentDetail.providerId].
     */
    override val providerId: String,

    /**
     * Trailer or promo video URL.
     *
     * ***Optional***. Rarely provided for live channels. Null if unavailable.
     * Inherits from [ContentDetail.trailerUrl].
     */
    override val trailerUrl: String? = null,

    /**
     * BCP 47 language tag for the channel's primary language.
     *
     * ***Optional***. Examples: "en" (English), "ja" (Japanese), "es" (Spanish).
     * Null if not declared by the provider. Used for language filtering and UI.
     */
    val lang: String? = null,
) : ContentDetail()
