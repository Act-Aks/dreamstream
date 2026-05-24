package com.dreamstream.feature.search.presentation

import androidx.compose.runtime.Stable
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.search.presentation.model.SearchResultUi

// ─────────────────────────────────────────────────────────────────────────────
// State
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Complete UI state for the Search screen.
 *
 * @Stable required because [results] is a [List] — considered unstable by the
 * Compose compiler without the annotation.
 */
@Stable
data class SearchState(
    val query: String = "",
    val results: List<SearchResultUi> = emptyList(),
    val isLoading: Boolean = false,
    val error: UiText? = null,
)

// ─────────────────────────────────────────────────────────────────────────────
// Action  (user-triggered intents)
// ─────────────────────────────────────────────────────────────────────────────

sealed interface SearchAction {
    /** User typed into the search field. */
    data class OnQueryChange(val query: String) : SearchAction

    /** User pressed the clear button in the search field. */
    data object OnClearQuery : SearchAction

    /** User tapped a result row. */
    data class OnResultClick(val contentId: String) : SearchAction

    /** User pressed the back / close button. */
    data object OnBackClick : SearchAction
}

// ─────────────────────────────────────────────────────────────────────────────
// Event  (one-time side effects)
// ─────────────────────────────────────────────────────────────────────────────

sealed interface SearchEvent {
    /** Navigate to the detail screen for the given content. */
    data class NavigateToDetail(val contentId: String) : SearchEvent

    /** Pop the search screen off the back stack. */
    data object NavigateBack : SearchEvent
}
