package com.dreamstream.plugin.api.model.detail

import com.dreamstream.core.model.catalog.Actor
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.plugin.api.model.search.ApiSearchResult

data class ApiMovieDetail(
    override val actors: List<Actor> = emptyList(),
    override val backgroundUrl: String? = null,
    override val name: String,
    override val plot: String? = null,
    override val posterUrl: String? = null,
    override val rating: Float? = null,
    override val recommendations: List<ApiSearchResult> = emptyList(),
    override val tags: List<String> = emptyList(),
    override val trailerUrl: String? = null,
    override val type: ContentType = ContentType.Movie,
    override val url: String,
    override val year: Int? = null,
    val comingSoon: Boolean = false,
    val dataUrl: String,
    val duration: Int? = null,
) : ApiContentDetail()
