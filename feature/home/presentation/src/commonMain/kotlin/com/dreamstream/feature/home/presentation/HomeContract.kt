package com.dreamstream.feature.home.presentation

import com.dreamstream.core.presentation.ui.UiText


// ─────────────────────────────────────────────────────────────────────────────
// Event  (one-time side effects)
// ─────────────────────────────────────────────────────────────────────────────

sealed interface HomeEvent {
    /** Navigate to the detail screen for the given content. */
    data class NavigateToDetail(val contentId: String) : HomeEvent

    /** Navigate to the search screen. */
    data object NavigateToSearch : HomeEvent

    /** Navigate to the settings screen. */
    data object NavigateToSettings : HomeEvent

    /** Show a transient error message. */
    data class ShowError(val message: UiText) : HomeEvent
}
