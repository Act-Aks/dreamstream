package com.dreamstream.feature.home.presentation.model

/**
 * Presentation-layer model for a single piece of content.
 *
 * All fields are pre-formatted strings, ready for direct display in the UI.
 * Formatting is applied in [com.dreamstream.feature.home.presentation.util.HomeMappings].
 */
data class ContentUi(
    val id: String,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    /** Human-readable content type, e.g. "Movie", "Series". */
    val typeName: String,
    /** Formatted year string, e.g. "2024". Empty string if unknown. */
    val year: String,
    /** Formatted rating string, e.g. "8.2". Empty string if unknown. */
    val rating: String,
)

/**
 * Presentation-layer model for a named horizontal section on the home screen,
 * e.g. "Trending Now" or "New Releases".
 */
data class HomeSectionUi(
    val id: String,
    val title: String,
    val items: List<ContentUi>,
)
