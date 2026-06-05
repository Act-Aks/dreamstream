package com.dreamstream.core.domain.model.filter

import kotlinx.serialization.Serializable

/**
 * Base class for all filter types.
 *
 * [FilterOption] is the abstract base for filter criteria used in the filter sheet
 * and search refinement UI. It defines a common [name] for display purposes.
 *
 * This sealed class has three concrete subclasses:
 * - [SingleSelectFilter]: Dropdown/radio group (e.g., "Genre: Action")
 * - [BooleanFilter]: Checkbox toggle (e.g., "HD only")
 * - [TextSearchFilter]: Text input (e.g., "Search by title")
 *
 * ## Key Properties:
 * - [name]: Filter label (***required***)
 *
 * ## Filter Types:
 * | Type | Use Case | UI Widget |
 * |------|----------|-----------|
 * | [SingleSelectFilter] | Select one option | Dropdown/Radio |
 * | [BooleanFilter] | Toggle on/off | Checkbox |
 * | [TextSearchFilter] | Enter text | Search box |
 *
 * ## Usage:
 * ```kotlin
 * val genreFilter = SingleSelectFilter(
 *     name = "Genre",
 *     options = listOf("Action", "Drama", "Sci-Fi"),
 *     selected = "Action"
 * )
 *
 * val hdFilter = BooleanFilter(
 *     name = "HD only",
 *     isChecked = true
 * )
 * ```
 *
 * ## In Filter Screen:
 * ```kotlin
 * FilterOptions(
 *     filters = listOf(
 *         SingleSelectFilter(name = "Genre", options = listOf("Action", "Drama")),
 *         BooleanFilter(name = "HD only", isChecked = false),
 *         TextSearchFilter(name = "Search")
 *     )
 * )
 * ```
 *
 * ## Related Subclasses:
 * - [SingleSelectFilter]: Dropdown selection
 * - [BooleanFilter]: Checkbox toggle
 * - [TextSearchFilter]: Text input
 *
 * ## Related:
 * - Container: [FilterOptions]
 * - ViewModel: ***`FilterViewModel`***
 * - Screen: ***`FilterScreen`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
sealed class FilterOption {
    /**
     * Display name of the filter.
     *
     * ***Required***. Label shown in the filter sheet (e.g., "Genre", "Year", "Search").
     */
    abstract val name: String

    /**
     * Value of the filter.
     *
     * ***Required***. Value for the filter.
     */
    abstract val value: Any
}

/**
 * Available filter options for a provider's search.
 *
 * [FilterOptions] is the container model returned by ***`ContentProvider.getFilterOptions`***
 * to declare what filters a provider supports. It contains a list of [FilterOption]
 * instances, each representing a filter category.
 *
 * ## Key Properties:
 * - [filters]: List of filter definitions (***required***)
 *
 * ## Usage:
 * ```kotlin
 * val options = provider.getFilterOptions()
 * val genreFilter = options.filters.find { it.name == "Genre" }
 * ```
 *
 * ## In Provider:
 * ```kotlin
 * class AnimeProvider : ContentProvider() {
 *     override fun getFilterOptions(): FilterOptions = FilterOptions(
 *         filters = listOf(
 *             SingleSelectFilter(
 *                 name = "Genre",
 *                 options = listOf("Action", "Drama", "Romance")
 *             ),
 *             BooleanFilter(name = "Subtitled only"),
 *             BooleanFilter(name = "Dubbed only")
 *         )
 *     )
 * }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the filter sheet as a vertical list
 * - Each [FilterOption] renders as its specific widget (dropdown, checkbox, etc.)
 * - Applying filters triggers ***`FilterViewModel.applyFilters`***
 *
 * ## Related:
 * - Base type: [FilterOption]
 * - Filter types: [SingleSelectFilter], [BooleanFilter], [TextSearchFilter]
 * - Provider method: ***`ContentProvider.getFilterOptions`***
 * - ViewModel: ***`FilterViewModel`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class FilterOptions(
    /**
     * List of filter definitions.
     *
     * ***Required***. Contains all supported filters for this provider.
     * Empty list if the provider supports no filters.
     *
     * Examples:
     * ```kotlin
     * filters = listOf(
     *     SingleSelectFilter(name = "Genre", options = listOf("Action", "Drama")),
     *     BooleanFilter(name = "HD only"),
     *     TextSearchFilter(name = "Search")
     * )
     * ```
     */
    val filters: List<FilterOption> = emptyList()
)
