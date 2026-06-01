package com.dreamstream.plugin.api.model.search

import com.dreamstream.core.model.catalog.ContentType

sealed class ApiSearchResult {
    abstract val name: String
    abstract val url: String
    abstract val posterUrl: String?
    abstract val type: ContentType
}
