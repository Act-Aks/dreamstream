package com.dreamstream.core.presentation.model

import com.dreamstream.core.model.media.Subtitle
import com.dreamstream.core.presentation.ui.UiText

/**
 * Returns a human-readable display name for this [Subtitle].
 *
 * Resolution order:
 * 1. If [Subtitle.name] is non-empty, it is used as the display label.
 * 2. Otherwise, falls back to [Subtitle.lang] (e.g., "en", "hi", "ja").
 * 3. If both are missing/blank, returns an empty string.
 *
 * This property is used in subtitle track selectors and player settings UI.
 *
 * @return A [UiText] containing the chosen label as a dynamic string.
 */
val Subtitle.displayName: UiText
    get() = UiText.DynamicString(name.ifBlank { lang.ifBlank { "" } })
