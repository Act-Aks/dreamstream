package com.dreamstream.plugin.api.model.catalog

import com.dreamstream.core.model.search.SearchResult
import kotlinx.serialization.Serializable

/**
 * A section within a catalog response.
 *
 * Example sections: "Trending Now", "Action Movies", "Continue Watching"
 */
@Serializable
data class CatalogSection(
    /** Section title displayed in UI */
    val name: String,

    /** Content items in this section */
    val items: List<SearchResult> = emptyList(),

    /** `true` if more items available in this section */
    val hasNextPage: Boolean = false,
)
