package com.dreamstream.feature.home.presentation

import androidx.compose.runtime.Stable
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.presentation.model.HomeSectionUi

// ─────────────────────────────────────────────────────────────────────────────
// State
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Complete UI state for the Home screen.
 *
 * @Stable required because [sections] is a [List] and [error] is a sealed
 * interface — both considered unstable by the Compose compiler without it.
 */
@Stable
data class HomeState(
    val sections: List<HomeSectionUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null,
)

// ─────────────────────────────────────────────────────────────────────────────
// Action  (user-triggered intents)
// ─────────────────────────────────────────────────────────────────────────────

sealed interface HomeAction {
    /** User explicitly requested a content refresh (e.g. pull-to-refresh). */
    data object OnRefresh : HomeAction

    /** User tapped a content card. */
    data class OnContentClick(val contentId: String) : HomeAction

    /** User tapped the search icon in the top bar. */
    data object OnSearchClick : HomeAction
}

// ─────────────────────────────────────────────────────────────────────────────
// Event  (one-time side effects)
// ─────────────────────────────────────────────────────────────────────────────

sealed interface HomeEvent {
    /** Navigate to the detail screen for the given content. */
    data class NavigateToDetail(val contentId: String) : HomeEvent

    /** Navigate to the search screen. */
    data object NavigateToSearch : HomeEvent

    /** Show a transient error message. */
    data class ShowError(val message: UiText) : HomeEvent
}
