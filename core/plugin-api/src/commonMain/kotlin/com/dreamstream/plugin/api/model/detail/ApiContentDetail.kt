package com.dreamstream.plugin.api.model.detail

import com.dreamstream.core.domain.model.catalog.Actor
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.plugin.api.model.search.ApiSearchResult

// Load Response
sealed class ApiContentDetail {
    abstract val name: String
    abstract val url: String
    abstract val posterUrl: String?
    abstract val backgroundUrl: String?
    abstract val type: ContentType
    abstract val year: Int?
    abstract val plot: String?
    abstract val rating: Float?
    abstract val tags: List<String>
    abstract val actors: List<Actor>
    abstract val recommendations: List<ApiSearchResult>
    abstract val trailerUrl: String?
}
