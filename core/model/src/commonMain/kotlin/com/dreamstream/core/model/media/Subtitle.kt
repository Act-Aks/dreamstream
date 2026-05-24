package com.dreamstream.core.model.media

import com.dreamstream.core.model.catalog.SubtitleFormat
import kotlinx.serialization.Serializable

/**
 * A subtitle track associated with a stream.
 *
 * [format] is inferred from [url] via [SubtitleFormat.fromUrl] when not
 * explicitly provided.
 */
@Serializable
data class Subtitle(
    val url: String,
    val lang: String,
    val format: SubtitleFormat = SubtitleFormat.fromUrl(url),
    val name: String = lang,
    val isDefault: Boolean = false,
) {
    val displayName: String get() = name.ifBlank { lang }
}
