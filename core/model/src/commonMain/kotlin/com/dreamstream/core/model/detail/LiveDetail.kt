package com.dreamstream.core.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for a live channel or stream.
 *
 * [dataUrl] is the provider-side playback URL or opaque data token used by
 * the stream resolver. It follows the same contract as [MovieDetail.dataUrl] —
 * it is **not** a direct media URL and must be resolved before playback.
 *
 * Live content typically has minimal metadata: [plot], [rating], and [year]
 * will commonly be null. Expect providers to omit most optional fields.
 *
 * @property dataUrl Provider-side playback URL or opaque data token for stream resolution.
 * @property lang BCP 47 language tag for the channel's primary language
 *   (e.g. `"en"`, `"ja"`), or null when not declared by the provider.
 */
@Serializable
data class LiveDetail(
    override val name: String,
    override val url: String,
    val dataUrl: String,
    override val posterUrl: String? = null,
    override val backgroundPosterUrl: String? = null,
    override val type: ContentType = ContentType.Live,
    override val year: Int? = null,
    override val plot: String? = null,
    override val rating: Float? = null,
    override val tags: List<String> = emptyList(),
    override val recommendations: List<SearchResult> = emptyList(),
    override val actors: List<Actor> = emptyList(),
    override val providerId: String,
    override val trailerUrl: String? = null,
    val lang: String? = null,
) : ContentDetail()
