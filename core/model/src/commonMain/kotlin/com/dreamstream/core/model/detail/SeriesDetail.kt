package com.dreamstream.core.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.catalog.Episode
import com.dreamstream.core.model.catalog.Season
import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for a TV series.
 *
 * Episodes are stored as a flat list in [episodes]. The computed property
 * [episodesBySeason] groups them by season number; season `0` represents
 * specials or episodes not assigned to a season. Use [getEpisodesForSeason]
 * for safe per-season access without null checks.
 *
 * @property episodes All episodes for this series across all seasons.
 * @property seasons Structured season metadata when provided by the source.
 * @property showStatus Current broadcast or release status of the series.
 * @property totalEpisodes Total episode count reported by the provider, or
 *   null when not declared.
 */
@Serializable
data class SeriesDetail(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val backgroundPosterUrl: String? = null,
    override val type: ContentType = ContentType.TvSeries,
    override val year: Int? = null,
    override val plot: String? = null,
    override val rating: Float? = null,
    override val tags: List<String> = emptyList(),
    override val recommendations: List<SearchResult> = emptyList(),
    override val actors: List<Actor> = emptyList(),
    override val providerId: String,
    override val trailerUrl: String? = null,
    val episodes: List<Episode> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val showStatus: ShowStatus = ShowStatus.Unknown,
    val totalEpisodes: Int? = null,
) : ContentDetail() {

    /**
     * Episodes grouped by season number.
     *
     * Season `0` collects specials and any episodes without a season assignment.
     */
    val episodesBySeason: Map<Int, List<Episode>>
        get() = episodes.groupBy { it.season ?: 0 }

    /**
     * Returns all episodes for [seasonNumber], or an empty list when the
     * season does not exist or has no episodes.
     */
    fun getEpisodesForSeason(seasonNumber: Int): List<Episode> =
        episodesBySeason[seasonNumber] ?: emptyList()
}
