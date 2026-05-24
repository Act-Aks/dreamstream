package com.dreamstream.core.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.catalog.Episode
import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * Full detail record for an anime title.
 *
 * Sub (subtitled) and dub (dubbed) tracks are represented as separate episode
 * lists, reflecting the common provider pattern where sub and dub versions are
 * served as distinct streams with independent episode data tokens.
 *
 * [malId] and [anilistId] are optional cross-reference IDs to MyAnimeList and
 * AniList respectively. Use them only for metadata enrichment — do **not** use
 * them as primary keys in the DreamStream data model; use [url] instead.
 *
 * @property subEpisodes Episodes available with subtitles.
 * @property dubEpisodes Episodes available in dubbed audio.
 * @property showStatus Current broadcast or release status.
 * @property japaneseTitle Original Japanese title, or null when not provided by the source.
 * @property malId MyAnimeList series ID for cross-referencing, or null when unavailable.
 * @property anilistId AniList series ID for cross-referencing, or null when unavailable.
 */
@Serializable
data class AnimeDetail(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val backgroundPosterUrl: String? = null,
    override val type: ContentType = ContentType.Anime,
    override val year: Int? = null,
    override val plot: String? = null,
    override val rating: Float? = null,
    override val tags: List<String> = emptyList(),
    override val recommendations: List<SearchResult> = emptyList(),
    override val actors: List<Actor> = emptyList(),
    override val providerId: String,
    override val trailerUrl: String? = null,
    val subEpisodes: List<Episode> = emptyList(),
    val dubEpisodes: List<Episode> = emptyList(),
    val showStatus: ShowStatus = ShowStatus.Unknown,
    val japaneseTitle: String? = null,
    val malId: Int? = null,
    val anilistId: Int? = null,
) : ContentDetail()
