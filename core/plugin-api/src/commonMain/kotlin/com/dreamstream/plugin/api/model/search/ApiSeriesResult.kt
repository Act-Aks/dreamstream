package com.dreamstream.plugin.api.model.search

import com.dreamstream.core.domain.model.catalog.ContentType

data class ApiSeriesResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.TvSeries,
    val year: Int? = null,
    val totalSeasons: Int? = null,
    val rating: Float? = null,
) : ApiSearchResult()
