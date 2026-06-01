package com.dreamstream.plugin.api.model.search

import com.dreamstream.core.model.catalog.ContentType

data class ApiAnimeResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.Anime,
    val year: Int? = null,
    val dubAvailable: Boolean = false,
    val subAvailable: Boolean = true,
) : ApiSearchResult()
