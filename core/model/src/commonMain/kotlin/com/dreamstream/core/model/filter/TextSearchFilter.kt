package com.dreamstream.core.model.filter

import kotlinx.serialization.Serializable

/**
 * Text input filter for search or keyword filtering.
 *
 * [TextSearchFilter] represents a text-based filter where the user enters
 * a query string. It is used in ***`FilterScreen.options`*** for free-text
 * filters like "Search by title", "Min rating", or "Genre keyword".
 *
 * This class extends [FilterOption] and adds:
 * - [value]: Current input text (***optional, defaults to empty***)
 *
 * ## Key Properties:
 * - [name]: Filter label (***required***)
 * - [value]: Input text (***optional***)
 *
 * ## Common Use Cases:
 * | Filter Name | Example Input | Purpose |
 * |-------------|---------------|---------|
 * | "Search by title" | "Attack on Titan" | Filter by content name |
 * | "Min rating" | "8.5" | Filter by minimum rating |
 * | "Keyword" | "supernatural" | Filter by tag/genre keyword |
 * | "Year" | "2023" | Filter by release year |
 *
 * ## Usage:
 * ```kotlin
 * val searchFilter = TextSearchFilter(
 *     name = "Search by title",
 *     value = "Demon Slayer"
 * )
 *
 * if (searchFilter.value.isNotEmpty()) {
 *     // Filter content by title containing "Demon Slayer"
 * }
 * ```
 *
 * ## In Filter Screen:
 * ```kotlin
 * FilterOptions(
 *     filters = listOf(
 *         TextSearchFilter(name = "Search by title", value = ""),
 *         TextSearchFilter(name = "Min rating", value = "8.0"),
 *         SingleSelectFilter(name = "Genre", options = listOf("Action", "Drama"))
 *     )
 * )
 * ```
 *
 * ## UI Representation:
 * - Displayed as a text input field in the filter sheet
 * - [value] shows the current text (empty by default)
 * - Clearing [value] removes the filter constraint
 * - Changing input triggers a filter refresh via ***`FilterViewModel.applyFilters`***
 * - Supports debounced search for performance
 *
 * ## Related:
 * - Base class: [FilterOption]
 * - Used in:
 *      ***`FilterScreen`***
 *      ***`FilterViewModel`***
 * - Other filter types: [SingleSelectFilter], [BooleanFilter]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class TextSearchFilter(
    /**
     * Filter label displayed in the UI.
     *
     * ***Required***. Describes the filter purpose (e.g., "Search by title", "Min rating").
     * Inherits from [FilterOption.name].
     */
    override val name: String,

    /**
     * Current input text.
     *
     * ***Optional***. Defaults to empty string (`""`).
     * Non-empty values trigger the filter logic. Empty values disable the filter.
     *
     * Example:
     * ```kotlin
     * value = "Attack on Titan"  // Filter by title
     * value = ""                 // No filter applied
     * ```
     */
    override val value: String = "",
) : FilterOption()
