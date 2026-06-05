package com.dreamstream.feature.home.domain.model

import com.dreamstream.core.domain.model.search.SearchResult

/**
 * A named horizontal row of content items displayed on the home screen.
 *
 * Examples: "Trending Now", "New Releases", "Continue Watching".
 * Items are [SearchResult] instances returned by the provider plugin system.
 */
data class HomeSection(
    val id: String,
    val title: String,
    val items: List<SearchResult>,
)
