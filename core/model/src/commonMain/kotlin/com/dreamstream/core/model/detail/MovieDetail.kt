package com.dreamstream.core.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for a movie.
 *
 * [dataUrl] is the provider-side playback URL or opaque data token used by
 * the stream resolver to obtain the final, playable media URL. It is **not**
 * a direct media URL and must be resolved before playback begins.
 *
 * @property dataUrl Provider-side playback URL or opaque data token for stream resolution.
 * @property duration Total runtime in minutes, or null when unknown.
 * @property comingSoon True when the title is announced but not yet available for streaming.
 */
@Serializable
data class MovieDetail(
    override val name: String,
    override val url: String,
    /** Provider-side playback URL or opaque data token passed to the stream resolver. */
    val dataUrl: String,
    override val posterUrl: String? = null,
    override val backgroundPosterUrl: String? = null,
    override val type: ContentType = ContentType.Movie,
    override val year: Int? = null,
    override val plot: String? = null,
    override val rating: Float? = null,
    override val tags: List<String> = emptyList(),
    override val recommendations: List<SearchResult> = emptyList(),
    override val actors: List<Actor> = emptyList(),
    override val providerId: String,
    override val trailerUrl: String? = null,
    /** Total runtime in minutes. */
    val duration: Int? = null,
    val comingSoon: Boolean = false,
) : ContentDetail()
