package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/**
 * Known subtitle file formats.
 *
 * Use [fromUrl] to infer the format from a subtitle file URL.
 */
@Serializable
enum class SubtitleFormat {
    SRT,
    VTT,
    ASS,
    SSA,
    TTML,
    Unknown;

    companion object {
        fun fromUrl(url: String): SubtitleFormat = when {
            url.endsWith(".srt", ignoreCase = true) -> SRT
            url.endsWith(".vtt", ignoreCase = true) -> VTT
            url.endsWith(".ass", ignoreCase = true) -> ASS
            url.endsWith(".ssa", ignoreCase = true) -> SSA
            url.endsWith(".ttml", ignoreCase = true) -> TTML
            else -> Unknown
        }
    }
}
