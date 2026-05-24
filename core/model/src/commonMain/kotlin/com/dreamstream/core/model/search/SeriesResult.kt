package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Search-level result for a TV series.
 *
 * [totalSeasons] is the provider-reported season count at search time and may
 * be null or inaccurate for ongoing or newly added series. Full episode and
 * season structure is available on the corresponding [com.dreamstream.core.model.detail.SeriesDetail].
 *
 * @property year Year the series first aired, or null when unknown.
 * @property totalSeasons Total number of seasons reported by the provider at
 *   search time, or null when not available.
 * @property rating Raw provider rating on a 0–10 scale, or null when unavailable.
 */
@Serializable
data class SeriesResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.TvSeries,
    override val providerId: String,
    val year: Int? = null,
    val totalSeasons: Int? = null,
    val rating: Float? = null,
) : SearchResult()
