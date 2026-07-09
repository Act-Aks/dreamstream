package com.dreamstream.feature.home.presentation

import androidx.compose.runtime.Stable
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.presentation.model.HomeSectionUi

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
    val nextPageBySectionId: Map<String, Int> = emptyMap(),
    val isAppendingBySectionId: Map<String, Boolean> = emptyMap(),
)
