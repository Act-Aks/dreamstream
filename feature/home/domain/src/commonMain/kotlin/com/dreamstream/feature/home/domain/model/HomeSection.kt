package com.dreamstream.feature.home.domain.model

/**
 * A named horizontal row of content items displayed on the home screen.
 *
 * Examples: "Trending Now", "New Releases", "Continue Watching".
 */
data class HomeSection(
    val id: String,
    val title: String,
    val items: List<Content>,
)
