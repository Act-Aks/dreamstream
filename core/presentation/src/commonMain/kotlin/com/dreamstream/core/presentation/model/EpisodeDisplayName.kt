package com.dreamstream.core.presentation.model

import com.dreamstream.core.domain.model.catalog.Episode
import com.dreamstream.core.presentation.resources.CoreRes
import com.dreamstream.core.presentation.resources.episode
import com.dreamstream.core.presentation.ui.UiText

/**
 * Returns a localized display name for this [Episode].
 *
 * Resolution order:
 * 1. If [Episode.name] is present, it is returned as a dynamic string (e.g., episode title).
 * 2. Otherwise, a formatted "SxxExx" label is constructed using [Episode.season] and [Episode.episode].
 *    - Season and episode numbers are zero-padded to two digits (e.g., "S01E05").
 * 3. If neither season nor episode numbers are available, a localized "Episode X" string
 *    is returned using [CoreRes.string.episode] with [Episode.episode] as the placeholder argument.
 *
 * This property is used in episode list items, detail headers, and playback screens.
 *
 * Formatting examples:
 * - `name = "Pilot"` → `"Pilot"`
 * - `season = 1, episode = 5, name = null` → `"S01E05"`
 * - `season = null, episode = null, name = null` → localized `"Episode X"`
 *
 * @return A [UiText] containing the episode title, formatted "SxxExx" label, or localized fallback.
 */
val Episode.displayName: UiText
    get() = name?.let {
        UiText.DynamicString(it)
    } ?: run {
        val seasonPart = season?.let { "S${it.toString().padStart(2, '0')}" } ?: ""
        val episodePart = episode?.let { "E${it.toString().padStart(2, '0')}" } ?: ""
        val label = "$seasonPart$episodePart"

        if (label.isNotEmpty()) {
            UiText.DynamicString(label)
        } else {
            UiText.StringResourceId(CoreRes.string.episode, listOf(episode ?: 0))
        }
    }
