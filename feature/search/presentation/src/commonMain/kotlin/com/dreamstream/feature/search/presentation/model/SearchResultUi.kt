package com.dreamstream.feature.search.presentation.model

/**
 * Presentation-layer model for a single search result item.
 *
 * All fields are pre-formatted strings, ready for direct display in the UI.
 * Formatting is applied in [com.dreamstream.feature.search.presentation.util.SearchMappings].
 *
 * [id] is the provider-side [url] from [com.dreamstream.core.model.search.SearchResult],
 * used as the stable navigation key passed to the detail screen.
 */
data class SearchResultUi(
    val id: String,
    val title: String,
    val thumbnailUrl: String?,
    /** Singular content type label, e.g. "Movie", "TV Series". */
    val typeName: String,
    /** Formatted year string, e.g. "2024". Empty string if unknown. */
    val year: String,
    /** Formatted rating string, e.g. "8.2". Empty string if unknown. */
    val rating: String,
)
