package com.dreamstream.feature.home.presentation

sealed interface HomeAction {
    data object OnRefresh : HomeAction
    data class OnContentClick(val contentId: String) : HomeAction
    data object OnSearchClick : HomeAction
    data object OnSettingsClick : HomeAction
    data class OnLoadMoreSection(val sectionId: String) : HomeAction
}
