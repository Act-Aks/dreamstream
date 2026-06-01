package com.dreamstream.core.model.media

import com.dreamstream.core.model.catalog.Quality
import kotlinx.serialization.Serializable

/**
 * A resolved stream URL with its associated metadata.
 *
 * [StreamLink] represents a playable media URL after resolution by a provider.
 * It is the final output of ***`ContentProvider.loadLinks`***, returned after
 * extracting the media source from a provider's page or token.
 *
 * This class contains:
 * - **Media URL**: [url] (required, playable link)
 * - **Quality**: [quality] (e.g., "HD", "4K")
 * - **Format detection**: [isM3u8] (HLS), [isDash] (DASH)
 * - **Metadata**: [name], [headers], [referer], [extractorName]
 *
 * ## Key Properties:
 * - [url]: Playable media URL (***required***)
 * - [name]: Server/source label (***optional***)
 * - [quality]: Stream quality (***optional***)
 * - [isM3u8]: HLS format flag (***computed***)
 * - [isDash]: DASH format flag (***computed***)
 * - [headers]: Request headers (***optional, sensitive***)
 * - [isWorking]: URL validity flag (***computed***)
 *
 * ## Format Detection:
 * [isM3u8] and [isDash] are inferred from the URL when not explicitly set:
 * - `.m3u8` in URL → [isM3u8] = `true` (HLS streaming)
 * - `.mpd` in URL → [isDash] = `true` (MPEG-DASH streaming)
 * ```kotlin
 * val link = StreamLink(url = "https://example.com/video.m3u8")
 * link.isM3u8 // true
 * link.isDash // false
 * ```
 *
 * ## Sensitive Data:
 * [headers] holds provider-required request headers (e.g., `Authorization`, `Cookie`).
 * Treat as **sensitive**: do not log or persist without explicit need.
 * ```kotlin
 * // Safe: use headers directly
 * httpClient.get(url, headers = link.headers)
 *
 * // Unsafe: do not log
 * Log.d("StreamLink", link.headers.toString()) // ❌
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val stream = StreamLink(
 *     url = "https://cdn.example.com/stream.m3u8",
 *     name = "Server 1",
 *     quality = Quality.FullHd,
 *     headers = mapOf("Referer" to "https://provider.com")
 * )
 *
 * if (stream.isWorking && stream.isM3u8) {
 *     // Play with ExoPlayer HLS source
 * }
 * ```
 *
 * ## In Stream Resolver:
 * ```kotlin
 * val links = provider.loadLinks(dataUrl, type = ContentType.Movie)
 * val hdLink = links.find { it.quality == Quality.HD }
 * player.setMediaItem(hdLink.url)
 * ```
 *
 * ## UI Representation:
 * - Displayed in the server/quality selection dialog
 * - [name] shown as server label (e.g., "Server 1", "Fast Stream")
 * - [quality] shown as badge (e.g., "HD", "4K")
 * - [isWorking] indicates if the link is valid (green checkmark if true)
 *
 * ## Related:
 * - Provider method: ***`ContentProvider.loadLinks`***
 * - Quality type: [Quality]
 * - Content detail: ***`MovieDetail.dataUrl`***, ***`LiveDetail.dataUrl`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class StreamLink(
    /**
     * Resolved playable media URL.
     *
     * ***Required***. Direct link to the media stream (e.g., `.m3u8`, `.mpd`, `.mp4`).
     * Must be a valid, accessible URL for playback.
     */
    val url: String,

    /**
     * Server or source name.
     *
     * ***Optional***. Label displayed in the server selection UI (e.g., "Server 1",
     * "Fast Stream", "Backup"). Defaults to "Default".
     */
    val name: String = "Default",

    /**
     * Stream quality.
     *
     * ***Optional***. Quality classification (e.g., [Quality.HD], [Quality.FourK]).
     * Defaults to [Quality.Unknown] if not provided. Used for sorting and filtering.
     */
    val quality: Quality = Quality.Unknown,

    /**
     * Request headers for the media URL.
     *
     * ***Optional***. Provider-required headers (e.g., `Authorization`, `Cookie`,
     * `Referer`). Defaults to empty map.
     *
     * ***Sensitive data***: Do not log or persist without explicit need.
     */
    val headers: Map<String, String> = emptyMap(),

    /**
     * Whether the stream is HLS (`.m3u8`).
     *
     * ***Computed***. `true` if [url] contains `.m3u8` (case-insensitive).
     * Used to select the correct ExoPlayer data source (HLS vs. progressive).
     */
    val isM3u8: Boolean = url.contains(".m3u8", ignoreCase = true),

    /**
     * Whether the stream is MPEG-DASH (`.mpd`).
     *
     * ***Computed***. `true` if [url] contains `.mpd` (case-insensitive).
     * Used to select the DASH data source in ExoPlayer.
     */
    val isDash: Boolean = url.contains(".mpd", ignoreCase = true),

    /**
     * Referer header value.
     *
     * ***Optional***. Explicit referer URL for the request. Redundant if
     * already in [headers], but provided for convenience.
     */
    val referer: String? = null,

    /**
     * Name of the extractor used to resolve this link.
     *
     * ***Optional***. Identifies which extractor/plugin resolved the stream
     * (e.g., " کوثر", "UQLoader"). Useful for debugging.
     */
    val extractorName: String? = null,
) {

    /**
     * Whether the stream URL is valid and usable.
     *
     * ***Computed***. Returns `true` if [url] is not blank.
     * Use this to filter out invalid or broken links.
     */
    val isWorking: Boolean get() = url.isNotBlank()
}
