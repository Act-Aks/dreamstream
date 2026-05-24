package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/**
 * Video stream quality descriptor.
 *
 * [qualityInt] provides a stable numeric ordering so qualities can be compared
 * and ranked. Use [fromInt] or [fromString] to create instances from raw
 * provider values.
 */
@Serializable
enum class Quality(val displayName: String, val qualityInt: Int) {
    Unknown("Unknown", -1),
    Cam("CAM", 0),
    CamRip("CamRip", 1),
    HdCam("HD CAM", 2),
    Low("Low", 3),
    Medium("Medium", 4),
    MediumL("Medium+", 5),
    High("High", 6),
    HD("HD", 7),
    FullHd("FHD", 8),
    FourK("4K", 9),
    BluRay("Blu-Ray", 10),
    Auto("Auto", 11);

    companion object {
        fun fromInt(value: Int): Quality =
            entries.find { it.qualityInt == value } ?: Unknown

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
