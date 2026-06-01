package com.dreamstream.core.presentation.model

import com.dreamstream.core.model.plugin.PluginRepository
import com.dreamstream.core.presentation.ui.UiText

/**
 * Returns a human-readable display name for this [PluginRepository].
 *
 * Resolution order:
 * 1. If [PluginRepository.name] is non-empty, it is used as the label.
 * 2. Otherwise, falls back to [PluginRepository.url].
 * 3. If both are missing/blank, returns an empty string.
 *
 * This property is used in plugin repository selection screens, settings lists,
 * and repository management UIs to show a concise identifier for the repository.
 *
 * @return A display string suitable for UI labels.
 */
val PluginRepository.displayName: UiText
    get() = UiText.DynamicString(name.ifBlank { url.ifBlank { "" } })
