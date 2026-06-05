package com.dreamstream.core.domain.model.filter

import kotlinx.serialization.Serializable

/**
 * Single-choice filter (dropdown/radio buttons).
 *
 * [SingleSelectFilter] represents a filter where the user selects exactly one
 * option from a list. It is used in ***`FilterScreen.options`*** for categorical
 * filters like "Genre", "Year", "Status", or "Country".
 *
 * This class extends [FilterOption] and adds:
 * - [options]: Available choices (***required***)
 * - [value]: Currently selected index (***optional, defaults to 0***)
 *
 * ## Key Properties:
 * - [name]: Filter label (***required***)
 * - [options]: List of choices (***required***)
 * - [value]: Selected index (***optional***)
 *
 * ## Common Use Cases:
 * | Filter Name | Options | Selected |
 * |-------------|---------|----------|
 * | "Genre" | ["Action", "Comedy", "Drama"] | 0 ("Action") |
 * | "Year" | ["2024", "2023", "2022"] | 1 ("2023") |
 * | "Status" | ["Ongoing", "Completed"] | 0 ("Ongoing") |
 * | "Country" | ["Japan", "USA", "India"] | 0 ("Japan") |
 *
 * ## Usage:
 * ```kotlin
 * val genreFilter = SingleSelectFilter(
 *     name = "Genre",
 *     options = listOf("Action", "Comedy", "Drama", "Sci-Fi"),
 *     value = 0
 * )
 *
 * val selectedGenre = genreFilter.options[genreFilter.value] // "Action"
 * ```
 *
 * ## In Filter Screen:
 * ```kotlin
 * FilterOptions(
 *     filters = listOf(
 *         SingleSelectFilter(
 *             name = "Genre",
 *             options = listOf("Action", "Drama", "Romance"),
 *             value = 1
 *         ),
 *         SingleSelectFilter(
 *             name = "Year",
 *             options = listOf("2024", "2023", "2022"),
 *             value = 0
 *         )
 *     )
 * )
 * ```
 *
 * ## UI Representation:
 * - Displayed as a dropdown menu or radio group in the filter sheet
 * - [value] controls the default selection
 * - Changing selection triggers a filter refresh via ***`FilterViewModel.applyFilters`***
 * - Out-of-range [value] defaults to `0`
 *
 * ## Related:
 * - Base class: [FilterOption]
 * - Used in:
 *      ***`FilterScreen`***
 *      ***`FilterViewModel`***
 * - Other filter types: [BooleanFilter], [TextSearchFilter]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class SingleSelectFilter(
    /**
     * Filter label displayed in the UI.
     *
     * ***Required***. Describes the filter category (e.g., "Genre", "Year", "Status").
     * Inherits from [FilterOption.name].
     */
    override val name: String,

    /**
     * Index of the currently selected option.
     *
     * ***Optional***. Defaults to `0` (first option).
     * Must be within range `0 until options.size`. Out-of-range values
     * are clamped to `0` at runtime.
     *
     * Example:
     * ```kotlin
     * value = 0  // "Action"
     * value = 1  // "Drama"
     * ```
     */
    override val value: Int = 0,

    /**
     * List of available filter options.
     *
     * ***Required***. Non-empty list of choices (e.g., ["Action", "Drama", "Comedy"]).
     * Must contain at least one option. Index corresponds to [value].
     */
    val options: List<String>,
) : FilterOption()
