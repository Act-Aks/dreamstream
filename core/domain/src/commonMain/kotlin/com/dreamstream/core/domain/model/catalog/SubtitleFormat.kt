package com.dreamstream.core.domain.model.catalog

import com.dreamstream.core.domain.model.catalog.SubtitleFormat.Companion.fromUrl
import kotlinx.serialization.Serializable

/**
 * Known subtitle file formats.
 *
 * [SubtitleFormat] classifies the file type of subtitle track (e.g., "SRT", "VTT").
 * It is used in ***`SubtitleLink.format`*** to declare the subtitle type for the player
 * and in format selection/filtering logic.
 *
 * This enum provides:
 * - **Standard formats**: [SRT], [VTT], [ASS], [SSA], [TTML]
 * - **Fallback**: [Unknown] for unrecognized formats
 * - **Factory method** [fromUrl] to infer format from file extension
 *
 * ## Key Properties:
 * - Name: Format identifier (***required***)
 * - [fromUrl]: Auto-detection from URL (***optional helper***)
 *
 * ## Supported Formats:
 * | Format | Extension | Description |
 * |--------|-----------|-------------|
 * | [SRT] | `.srt` | SubRip (most common) |
 * | [VTT] | `.vtt` | WebVTT (web players) |
 * | [ASS] | `.ass` | Advanced SubStation Alpha |
 * | [SSA] | `.ssa` | SubStation Alpha (legacy) |
 * | [TTML] | `.ttml` | Timed Text Markup Language |
 * | [Unknown] | - | Unrecognized format |
 *
 * ## Usage:
 * ```kotlin
 * val format = SubtitleFormat.SRT
 * // Use format to configure player subtitle decoder
 * ```
 *
 * ## Inferring from URL:
 * ```kotlin
 * SubtitleFormat.fromUrl("subtitles.ep1.srt")   // SRT
 * SubtitleFormat.fromUrl("captions.vtt")        // VTT
 * SubtitleFormat.fromUrl("subs.ass")            // ASS
 * SubtitleFormat.fromUrl("unknown.xyz")         // Unknown
 * ```
 *
 * ## In Subtitle Link:
 * ```kotlin
 * SubtitleLink(
 *     url = "https://example.com/subs.srt",
 *     format = SubtitleFormat.SRT,
 *     language = "en"
 * )
 * ```
 *
 * ## Related:
 * - Factory method: [fromUrl]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
enum class SubtitleFormat {
    /**
     * SubRip format (`.srt`).
     *
     * Most common subtitle format. Plain text with timing.
     */
    SRT,

    /**
     * WebVTT format (`.vtt`).
     *
     * Web Video Text Tracks. Standard for HTML5 video players.
     */
    VTT,

    /**
     * Advanced SubStation Alpha format (`.ass`).
     *
     * Supports advanced styling, positioning, and karaoke effects.
     */
    ASS,

    /**
     * SubStation Alpha format (`.ssa`).
     *
     * Legacy format, predecessor to ASS.
     */
    SSA,

    /**
     * Timed Text Markup Language (`.ttml`).
     *
     * XML-based format used by streaming services (e.g., Netflix, YouTube).
     */
    TTML,

    /**
     * Unknown or unrecognized format.
     *
     * Used when the file extension doesn't match any known format.
     */
    Unknown;

    companion object {
        /**
         * Infers the [SubtitleFormat] from a file URL.
         *
         * Performs case-insensitive extension matching:
         * - `.srt` → [SRT]
         * - `.vtt` → [VTT]
         * - `.ass` → [ASS]
         * - `.ssa` → [SSA]
         * - `.ttml` → [TTML]
         *
         * Returns [Unknown] for unrecognized extensions.
         *
         * @param url Subtitle file URL (e.g., "subs.ep1.srt")
         * @return Matching [SubtitleFormat] or [Unknown]
         */
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
