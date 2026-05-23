package com.dreamstream.feature.details.presentation

import androidx.compose.runtime.Stable
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.details.presentation.model.DetailContentUi

@Stable
data class DetailsState(
    val content: DetailContentUi? = null,
    val isLoading: Boolean = false,
    val error: UiText? = null,
)

sealed interface DetailsAction {
    data object OnBackClick : DetailsAction
    data object OnRetry : DetailsAction
}

sealed interface DetailsEvent {
    data object NavigateBack : DetailsEvent
}
