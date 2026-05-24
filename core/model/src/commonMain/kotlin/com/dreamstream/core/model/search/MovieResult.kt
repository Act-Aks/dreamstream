package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.catalog.Quality
import kotlinx.serialization.Serializable

/**
 * Search-level result for a movie.
 *
 * [quality] reflects the best stream quality reported by the provider for
 * this title at search time. It defaults to [Quality.Unknown] when the
 * provider does not declare a quality level. Do not treat this as authoritative
 * — always confirm quality when resolving streams.
 *
 * @property year Release year of the movie, or null when unknown.
 * @property quality Best available stream quality reported by the provider.
 * @property rating Raw provider rating on a 0–10 scale, or null when unavailable.
 */
@Serializable
data class MovieResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.Movie,
    override val providerId: String,
    val year: Int? = null,
    val quality: Quality = Quality.Unknown,
    val rating: Float? = null,
) : SearchResult()
