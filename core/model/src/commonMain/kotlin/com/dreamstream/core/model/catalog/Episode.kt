package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/**
 * A single episode within a TV series or anime.
 *
 * [data] is an opaque string (usually a URL or token) that the provider plugin
 * uses to resolve stream links. [uniqueId] is a stable composite key that can
 * be used as a list item key or bookmark reference.
 */
@Serializable
data class Episode(
    /** Provider-side data token or URL passed to the stream resolver. */
    val data: String,
    val name: String? = null,
    val season: Int? = null,
    val episode: Int? = null,
    val posterUrl: String? = null,
    val description: String? = null,
    val rating: Float? = null,
    val airDate: String? = null,
    val durationMs: Long? = null,
    /** Runtime in minutes. */
    val runTime: Int? = null,
) {
    val displayName: String
        get() = name ?: buildString {
            season?.let { append("S${it.toString().padStart(2, '0')}") }
            episode?.let { append("E${it.toString().padStart(2, '0')}") }
            if (isEmpty()) append("Episode $episode")
        }

    val uniqueId: String
        get() = "${season}_${episode}_${data.hashCode()}"
}
