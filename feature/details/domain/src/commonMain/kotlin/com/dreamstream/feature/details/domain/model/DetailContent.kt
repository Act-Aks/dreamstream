package com.dreamstream.feature.details.domain.model

/**
 * Richer representation of a single piece of content shown on the Details
 * screen. Intentionally separate from the home feature's `Content` list-item
 * model — features are isolated and a detail API typically returns more data
 * than a catalog/list API.
 */
data class DetailContent(
    /** Opaque string identifier. Matches the id used by the home catalog. */
    val contentId: String,
    val title: String,
    val synopsis: String,
    val thumbnailUrl: String?,
    val backdropUrl: String?,
    val type: DetailMediaType,
    val year: Int?,
    /** Aggregate audience rating on a 0–10 scale. */
    val rating: Float?,
    val genres: List<String>,
    /** Total runtime in minutes. Null when unknown or not applicable (e.g. series). */
    val durationMinutes: Int?,
)
