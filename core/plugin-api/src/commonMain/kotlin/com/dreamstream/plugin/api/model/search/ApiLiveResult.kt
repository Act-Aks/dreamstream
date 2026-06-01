package com.dreamstream.plugin.api.model.search

import com.dreamstream.core.model.catalog.ContentType

data class ApiLiveResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.Live,
    val lang: String? = null,
    val tags: List<String> = emptyList(),
) : ApiSearchResult()
