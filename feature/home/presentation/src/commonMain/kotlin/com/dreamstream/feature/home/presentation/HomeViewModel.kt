package com.dreamstream.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamstream.core.domain.extensions.onFailure
import com.dreamstream.core.domain.extensions.onSuccess
import com.dreamstream.feature.home.domain.repository.HomeRepository
import com.dreamstream.feature.home.presentation.model.HomeSectionUi
import com.dreamstream.feature.home.presentation.util.toHomeSectionUi
import com.dreamstream.feature.home.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _events = Channel<HomeEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadHomeSections()
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnRefresh -> loadHomeSections()
            is HomeAction.OnContentClick -> navigateToDetail(action.contentId)
            is HomeAction.OnSearchClick -> navigateToSearch()
            is HomeAction.OnSettingsClick -> navigateToSettings()
            is HomeAction.OnLoadMoreSection -> loadMoreForSection(action.sectionId)
        }
    }

    private fun loadHomeSections() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            homeRepository.getHomeSections()
                .onSuccess { sections ->
                    val uiSections = sections.map { it.toHomeSectionUi() }
                    _state.update {
                        it.copy(
                            sections = uiSections,
                            isLoading = false,
                            error = null,
                            // initial page index per section (next after page 1)
                            nextPageBySectionId = uiSections.associate { section ->
                                section.id to 2
                            },
                            isAppendingBySectionId = emptyMap(),
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = error.toUiText(),
                            sections = emptyList(),
                            nextPageBySectionId = emptyMap(),
                            isAppendingBySectionId = emptyMap(),
                        )
                    }
                    _events.send(HomeEvent.ShowError(error.toUiText()))
                }
        }
    }

    private fun loadMoreForSection(sectionId: String) {
        val currentState = _state.value

        // Determine next page for this section; default to 2 if missing
        val nextPage = currentState.nextPageBySectionId[sectionId] ?: 2

        // Avoid overlapping loads for the same section
        if (currentState.isAppendingBySectionId[sectionId] == true) return

        viewModelScope.launch {
            // Mark this section as appending
            _state.update {
                it.copy(
                    isAppendingBySectionId = it.isAppendingBySectionId + (sectionId to true),
                )
            }

            homeRepository.getHomeSections(nextPage)
                .onSuccess { newSections ->
                    val newUiSections = newSections.map { it.toHomeSectionUi() }
                    val currentSections = _state.value.sections
                    val mergedSections: List<HomeSectionUi> = currentSections.map { existing ->
                        val more = newUiSections.find {
                            it.id == existing.id || it.title == existing.title
                        }
                        if (more != null && (existing.id == sectionId || existing.title == more.title)) {
                            existing.copy(items = existing.items + more.items)
                        } else {
                            existing
                        }
                    }

                    _state.update {
                        it.copy(
                            sections = mergedSections,
                            nextPageBySectionId = it.nextPageBySectionId + (sectionId to (nextPage + 1)),
                            isAppendingBySectionId = it.isAppendingBySectionId + (sectionId to false),
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                    _events.send(HomeEvent.ShowError(error.toUiText()))
                }
        }
    }

    private fun navigateToDetail(contentId: String) {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToDetail(contentId))
        }
    }

    private fun navigateToSearch() {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToSearch)
        }
    }

    private fun navigateToSettings() {
        viewModelScope.launch {
            _events.send(HomeEvent.NavigateToSettings)
        }
    }
}
