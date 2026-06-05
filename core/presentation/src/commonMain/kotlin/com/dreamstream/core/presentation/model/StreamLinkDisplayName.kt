package com.dreamstream.core.presentation.model

import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.presentation.ui.UiText

/**
 * Returns a human-readable display name for this [StreamLink].
 *
 * The format is:
 * - [StreamLink.name] if [StreamLink.quality] is [Quality.Unknown]
 * - `[StreamLink.name] • [quality displayName]` if [StreamLink.quality] is known
 *
 * Examples:
 * - `name = "StreamTape", quality = Quality.Unknown` → `"StreamTape"`
 * - `name = "VidSrc", quality = Quality.HD` → `"VidSrc • HD"`
 *
 * This property is used in episode player selectors, stream list items,
 * and quality badge UIs to identify both the source and its quality.
 *
 * @return A [UiText] containing the formatted display string.
 */
val StreamLink.displayName: UiText
    get() = buildString {
        append(name)
        if (quality != Quality.Unknown) append(" • ${quality.displayName}")
    }.let { UiText.DynamicString(it) }
