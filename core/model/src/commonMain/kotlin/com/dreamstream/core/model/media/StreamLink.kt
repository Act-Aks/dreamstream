package com.dreamstream.core.model.media

import com.dreamstream.core.model.catalog.Quality
import kotlinx.serialization.Serializable

/**
 * A resolved stream URL with its associated metadata.
 *
 * [isM3u8] and [isDash] are inferred from the URL when not explicitly set.
 * [headers] holds any provider-required request headers (treat as sensitive —
 * do not log or persist without explicit need).
 */
@Serializable
data class StreamLink(
    val url: String,
    val name: String = "Default",
    val quality: Quality = Quality.Unknown,
    val headers: Map<String, String> = emptyMap(),
    val isM3u8: Boolean = url.contains(".m3u8", ignoreCase = true),
    val isDash: Boolean = url.contains(".mpd", ignoreCase = true),
    val referer: String? = null,
    val extractorName: String? = null,
) {
    val isWorking: Boolean get() = url.isNotBlank()

    val displayName: String
        get() = buildString {
            append(name)
            if (quality != Quality.Unknown) append(" • ${quality.displayName}")
        }
}
