package com.dreamstream.core.model.media

import com.dreamstream.core.model.catalog.SubtitleFormat
import kotlinx.serialization.Serializable

/**
 * A subtitle track associated with a stream.
 *
 * [Subtitle] represents a single language track (e.g., "English", "Spanish")
 * for a video stream. It is used in ***`StreamLink.subtitles`*** or returned
 * separately by ***`ContentProvider.loadSubtitles`*** to populate the subtitle
 * selection menu in the player.
 *
 * This class contains:
 * - **URL**: [url] (required, subtitle file location)
 * - **Language**: [lang] (BCP 47 tag, e.g., "en", "es")
 * - **Format**: [format] (inferred from [url] if not provided)
 * - **Display name**: [name] (defaults to [lang])
 * - **Default flag**: [isDefault] (auto-select preference)
 *
 * ## Key Properties:
 * - [url]: Subtitle file URL (***required***)
 * - [lang]: Language tag (***required***)
 * - [format]: File format (***computed***)
 * - [name]: Display label (***optional***)
 * - [isDefault]: Auto-select flag (***optional***)
 *
 * ## Format Detection:
 * [format] is inferred from [url] via [SubtitleFormat.fromUrl] when not
 * explicitly provided:
 * - `.srt` → [SubtitleFormat.SRT]
 * - `.vtt` → [SubtitleFormat.VTT]
 * - `.ass` → [SubtitleFormat.ASS]
 * ```kotlin
 * val subtitle = Subtitle(url = "subs.en.vtt", lang = "en")
 * subtitle.format // SubtitleFormat.VTT
 * ```
 *
 * ## Usage:
 * ```kotlin
 * val engSub = Subtitle(
 *     url = "https://example.com/subs.en.vtt",
 *     lang = "en",
 *     name = "English (CC)",
 *     isDefault = true
 * )
 *
 * val jpnSub = Subtitle(
 *     url = "https://example.com/subs.ja.srt",
 *     lang = "ja"
 * )
 * ```
 *
 * ## In Stream Link:
 * ```kotlin
 * StreamLink(
 *     url = "https://cdn.example.com/stream.m3u8",
 *     subtitles = listOf(
 *         Subtitle(url = "subs.en.vtt", lang = "en", isDefault = true),
 *         Subtitle(url = "subs.es.vtt", lang = "es"),
 *         Subtitle(url = "subs.fr.vtt", lang = "fr")
 *     )
 * )
 * ```
 *
 * ## UI Representation:
 * - Displayed in the player's subtitle menu
 * - [name] shown as menu item (e.g., "English (CC)", "Español")
 * - [lang] used as fallback if [name] is not customized
 * - [isDefault] marked with a checkmark or "(default)" badge
 *
 * ## Related:
 * - Format type: [SubtitleFormat]
 * - Format detection: [SubtitleFormat.fromUrl]
 * - Stream link: ***`StreamLink`***
 * - Provider method: ***`ContentProvider.loadSubtitles`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class Subtitle(
    /**
     * URL to the subtitle file.
     *
     * ***Required***. Direct link to the `.srt`, `.vtt`, `.ass`, or other
     * subtitle file. Used by the player to load the track.
     */
    val url: String,

    /**
     * Language tag (BCP 47).
     *
     * ***Required***. Two-letter or culture code (e.g., "en", "es", "ja", "pt-BR").
     * Used for language filtering and UI display if [name] is not set.
     */
    val lang: String,

    /**
     * Subtitle file format.
     *
     * ***Optional***. Defaults to [SubtitleFormat.fromUrl(url)], which infers
     * the format from the file extension (e.g., `.vtt` → [SubtitleFormat.VTT]).
     * Explicitly set if the URL doesn't have an extension.
     */
    val format: SubtitleFormat = SubtitleFormat.fromUrl(url),

    /**
     * Display name for the subtitle track.
     *
     * ***Optional***. Label shown in the player's subtitle menu (e.g., "English (CC)",
     * "Español - Latino"). Defaults to [lang] if not provided.
     */
    val name: String = lang,

    /**
     * Whether this is the default subtitle track.
     *
     * ***Optional***. Defaults to `false`. When `true`, the player auto-selects
     * this track on startup (if user preferences allow).
     */
    val isDefault: Boolean = false,
)
