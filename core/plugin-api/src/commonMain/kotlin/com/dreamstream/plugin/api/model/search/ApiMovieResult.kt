package com.dreamstream.plugin.api.model.search

import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Quality

data class ApiMovieResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.Movie,
    val year: Int? = null,
    val quality: Quality = Quality.Unknown,
    val rating: Float? = null,
) : ApiSearchResult()
