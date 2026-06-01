package com.dreamstream.core.model.catalog

import com.dreamstream.core.model.catalog.Quality.Companion.fromInt
import com.dreamstream.core.model.catalog.Quality.Companion.fromString
import kotlinx.serialization.Serializable

/**
 * Video stream quality descriptor.
 *
 * [Quality] classifies the visual quality of a video stream (e.g., "HD", "4K", "CAM").
 * It is used in stream link models to display stream quality to users
 * and in sorting/filtering logic.
 *
 * This enum provides:
 * - **Human-readable names** via [displayName] (e.g., "4K", "FHD")
 * - **Numeric ordering** via [qualityInt] for comparison and sorting
 * - **Factory methods** [fromInt] and [fromString] to parse provider values
 *
 * ## Key Properties:
 * - [displayName]: User-facing label (***required***)
 * - [qualityInt]: Numeric rank for sorting (***required***)
 *
 * ## Quality Ranking (lowest to highest):
 * | Rank | Quality | displayName |
 * |------|---------|-------------|
 * | -1 | [Unknown] | "Unknown" |
 * | 0 | [Cam] | "CAM" |
 * | 3 | [Low] | "Low" |
 * | 7 | [HD] | "HD" |
 * | 8 | [FullHd] | "FHD" |
 * | 9 | [FourK] | "4K" |
 * | 11 | [Auto] | "Auto" |
 *
 * ## Usage:
 * ```kotlin
 * val quality = Quality.FourK
 * val label = quality.displayName  // "4K"
 * val rank = quality.qualityInt    // 9
 * ```
 *
 * ## Parsing Provider Values:
 * ```kotlin
 * Quality.fromString("1080p")   // FullHd
 * Quality.fromString("4k web")  // FourK
 * Quality.fromString("CAMRip")  // Cam
 * Quality.fromString(null)      // Unknown
 * Quality.fromInt(7)            // HD
 * ```
 *
 * ## In Stream Link:
 * ```kotlin
 * StreamLink(
 *     url = "https://stream.example.com/video.m3u8",
 *     quality = Quality.FullHd,
 *     name = "Server 1"
 * )
 * ```
 *
 * ## Related:
 * - Used in:
 *      ***`StreamLink`***
 *      ***`MovieSearchResult.quality`***
 * - Factory methods: [fromInt], [fromString]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
enum class Quality(val displayName: String, val qualityInt: Int) {
    /**
     * Quality unknown or not specified.
     *
     * ***Rank***: -1 (lowest). Used when the provider doesn't declare quality.
     */
    Unknown("Unknown", -1),

    /**
     * CAM (filmed in theater with a camera).
     *
     * ***Rank***: 0. Poor quality, often with audience noise.
     */
    Cam("CAM", 0),

    /**
     * CamRip (slightly better than CAM).
     *
     * ***Rank***: 1. Improved audio/video but still theater-recorded.
     */
    CamRip("CamRip", 1),

    /**
     * HD CAM (high-quality theater recording).
     *
     * ***Rank***: 2. Better resolution than standard CAM.
     */
    HdCam("HD CAM", 2),

    /**
     * Low quality (e.g., 360p).
     *
     * ***Rank***: 3. Small file size, low resolution.
     */
    Low("Low", 3),

    /**
     * Medium quality (e.g., 480p).
     *
     * ***Rank***: 4. Standard definition.
     */
    Medium("Medium", 4),

    /**
     * Medium+ quality (e.g., 540p).
     *
     * ***Rank***: 5. Between SD and HD.
     */
    MediumL("Medium+", 5),

    /**
     * High quality (e.g., 720p).
     *
     * ***Rank***: 6. HD-ready resolution.
     */
    High("High", 6),

    /**
     * HD (720p).
     *
     * ***Rank***: 7. Standard high definition.
     */
    HD("HD", 7),

    /**
     * Full HD (1080p).
     *
     * ***Rank***: 8. High-definition with full horizontal resolution.
     */
    FullHd("FHD", 8),

    /**
     * 4K Ultra HD (2160p).
     *
     * ***Rank***: 9. Ultra-high definition.
     */
    FourK("4K", 9),

    /**
     * BluRay source.
     *
     * ***Rank***: 10. High-quality encode from BluRay disc.
     */
    BluRay("Blu-Ray", 10),

    /**
     * Auto-select the best available quality.
     *
     * ***Rank***: 11 (highest). Used for adaptive bitrate streaming.
     */
    Auto("Auto", 11);

    companion object {
        /**
         * Creates a [Quality] from a numeric rank.
         *
         * Returns [Unknown] if the value doesn't match any known quality.
         *
         * @param value Numeric rank (e.g., 7 → [HD])
         * @return Matching [Quality] or [Unknown]
         */
        fun fromInt(value: Int): Quality =
            entries.find { it.qualityInt == value } ?: Unknown

        /**
         * Creates a [Quality] from a string label.
         *
         * Performs case-insensitive pattern matching:
         * - "4k", "2160" → [FourK]
         * - "1080", "fhd" → [FullHd]
         * - "720", "hd" → [HD]
         * - "480" → [High]
         * - "360" → [Medium]
         * - "cam" → [Cam]
         *
         * Returns [Unknown] for null, empty, or unrecognized strings.
         *
         * @param value Raw string from provider (e.g., "1080p", "4k web")
         * @return Matching [Quality] or [Unknown]
         */
        fun fromString(value: String?): Quality = when {
            value == null -> Unknown
            value.contains("4k", ignoreCase = true) ||
                value.contains("2160", ignoreCase = true) -> FourK
            value.contains("1080", ignoreCase = true) ||
                value.contains("fhd", ignoreCase = true) -> FullHd
            value.contains("720", ignoreCase = true) ||
                value.contains("hd", ignoreCase = true) -> HD
            value.contains("480", ignoreCase = true) -> High
            value.contains("360", ignoreCase = true) -> Medium
            value.contains("cam", ignoreCase = true) -> Cam
            else -> Unknown
        }
    }
}
