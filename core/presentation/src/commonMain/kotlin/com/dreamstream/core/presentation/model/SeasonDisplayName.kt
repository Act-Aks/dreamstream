package com.dreamstream.core.presentation.model

import com.dreamstream.core.model.catalog.Season
import com.dreamstream.core.presentation.resources.CoreRes
import com.dreamstream.core.presentation.resources.season
import com.dreamstream.core.presentation.ui.UiText

/**
 * Returns a localized display name for this [Season].
 *
 * Priority:
 * 1. If [Season.name] is present, it is returned as a dynamic string (e.g., custom season title).
 * 2. Otherwise, a localized "Season X" string is returned using [CoreRes.string.season]
 *    with the [Season.season] number as the placeholder argument.
 *
 * This property is used in season headers, navigation chips, and season selection UIs.
 *
 * @return A [UiText] containing either the custom name or a localized "Season X" string.
 */
val Season.displayName: UiText
    get() = name?.let { UiText.DynamicString(it) } ?: UiText.StringResourceId(
        CoreRes.string.season,
        listOf(season)
    )
