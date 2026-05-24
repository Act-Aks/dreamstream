package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/** A named season grouping within a TV series or anime. */
@Serializable
data class Season(
    val season: Int,
    val name: String? = null,
    val displaySeason: Int? = null,
    val posterUrl: String? = null,
    val airDate: String? = null,
    val episodeCount: Int? = null,
) {
    val displayName: String
        get() = name ?: "Season $season"
}
