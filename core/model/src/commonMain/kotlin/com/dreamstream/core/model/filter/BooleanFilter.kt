package com.dreamstream.core.model.filter

import kotlinx.serialization.Serializable

/**
 * Boolean checkbox filter (on/off).
 *
 * [BooleanFilter] represents a simple toggle filter used in the filter sheet
 * to enable or disable a specific criterion. It is used in ***`FilterScreen.options`***
 * for boolean-based filters like "HD only", "Subtitled only", or "Dubbed only".
 *
 * This class extends [FilterOption] and adds a single boolean state:
 * - [isChecked]: Whether the filter is active (***required***)
 *
 * ## Key Properties:
 * - [name]: Filter label (***required***)
 * - [isChecked]: Toggle state (***optional, defaults to false***)
 *
 * ## Common Use Cases:
 * | Filter Name | isChecked = true | isChecked = false |
 * |-------------|------------------|-------------------|
 * | "HD only" | Show only HD streams | Show all qualities |
 * | "Subtitled only" | Show only sub tracks | Show sub + dub |
 * | "Dubbed only" | Show only dub tracks | Show sub + dub |
 * | "New episodes" | Show recently added | Show all content |
 *
 * ## Usage:
 * ```kotlin
 * val hdFilter = BooleanFilter(
 *     name = "HD only",
 *     isChecked = true
 * )
 *
 * if (hdFilter.isChecked) {
 *     // Filter streams to quality >= HD
 * }
 * ```
 *
 * ## In Filter Screen:
 * ```kotlin
 * FilterOption(
 *     name = "Quality",
 *     options = listOf(
 *         BooleanFilter(name = "HD only", isChecked = false),
 *         BooleanFilter(name = "4K only", isChecked = false)
 *     )
 * )
 * ```
 *
 * ## UI Representation:
 * - Displayed as a checkbox in the filter sheet
 * - [isChecked] controls the checked state
 * - Toggling triggers a filter refresh via ***`FilterViewModel.applyFilters`***
 *
 * ## Related:
 * - Base class: [FilterOption]
 * - Used in:
 *      ***`FilterScreen`***
 *      ***`FilterViewModel`***
 * - Other filter types: ***`SelectFilter`***, ***`RangeFilter`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class BooleanFilter(
    /**
     * Filter label displayed in the UI.
     *
     * ***Required***. Describes the filter criterion (e.g., "HD only", "Subtitled only").
     * Inherits from [FilterOption.name].
     */
    override val name: String,

    /**
     * Whether the filter is currently active.
     *
     * ***Optional***. Defaults to `false` (unchecked).
     * Set to `true` to enable the filter criterion.
     *
     * Example:
     * ```kotlin
     * value = true  // Show only HD streams
     * value = false // Show all streams
     * ```
     */
    override val value: Boolean = false,
) : FilterOption()
