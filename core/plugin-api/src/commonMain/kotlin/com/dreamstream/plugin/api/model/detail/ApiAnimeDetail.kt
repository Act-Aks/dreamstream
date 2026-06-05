package com.dreamstream.plugin.api.model.detail

import com.dreamstream.core.domain.model.catalog.Actor
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Episode
import com.dreamstream.core.domain.model.detail.ShowStatus
import com.dreamstream.plugin.api.model.search.ApiSearchResult

data class ApiAnimeDetail(
    override val actors: List<Actor> = emptyList(),
    override val backgroundUrl: String? = null,
    override val name: String,
    override val plot: String? = null,
    override val posterUrl: String? = null,
    override val rating: Float? = null,
    override val recommendations: List<ApiSearchResult> = emptyList(),
    override val tags: List<String> = emptyList(),
    override val trailerUrl: String? = null,
    override val type: ContentType = ContentType.Anime,
    override val url: String,
    override val year: Int? = null,
    val dubEpisodes: List<Episode> = emptyList(),
    val japaneseTitle: String? = null,
    val showStatus: ShowStatus = ShowStatus.Unknown,
    val subEpisodes: List<Episode> = emptyList(),
) : ApiContentDetail()
