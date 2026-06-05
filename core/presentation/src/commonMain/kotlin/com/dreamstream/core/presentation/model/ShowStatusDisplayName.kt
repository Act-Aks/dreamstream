package com.dreamstream.core.presentation.model

import com.dreamstream.core.domain.model.detail.ShowStatus
import com.dreamstream.core.presentation.resources.CoreRes
import com.dreamstream.core.presentation.resources.status_cancelled
import com.dreamstream.core.presentation.resources.status_completed
import com.dreamstream.core.presentation.resources.status_hiatus
import com.dreamstream.core.presentation.resources.status_ongoing
import com.dreamstream.core.presentation.resources.status_unknown
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.core.presentation.ui.UiText.StringResourceId

/**
 * Returns a localized, human-readable display name for this [ShowStatus].
 *
 * This extension property is intended for UI presentation in detail screens,
 * card badges, and status indicators. It resolves to a localized string resource
 * supporting English, Hindi, German, and Japanese.
 *
 * Mappings:
 * - [ShowStatus.Ongoing] → "Ongoing"
 * - [ShowStatus.Completed] → "Completed"
 * - [ShowStatus.Cancelled] → "Cancelled"
 * - [ShowStatus.Hiatus] → "On Hiatus"
 * - [ShowStatus.Unknown] → "Unknown"
 *
 * @return A [UiText] wrapping the corresponding localized string resource ID.
 */
val ShowStatus.displayName: UiText
    get() = when (this) {
        ShowStatus.Ongoing -> StringResourceId(CoreRes.string.status_ongoing)
        ShowStatus.Completed -> StringResourceId(CoreRes.string.status_completed)
        ShowStatus.Cancelled -> StringResourceId(CoreRes.string.status_cancelled)
        ShowStatus.Hiatus -> StringResourceId(CoreRes.string.status_hiatus)
        ShowStatus.Unknown -> StringResourceId(CoreRes.string.status_unknown)
    }
