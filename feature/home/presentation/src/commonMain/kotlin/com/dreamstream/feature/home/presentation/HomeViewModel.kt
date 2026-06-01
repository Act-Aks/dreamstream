package com.dreamstream.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamstream.core.domain.extensions.onFailure
import com.dreamstream.core.domain.extensions.onSuccess
import com.dreamstream.feature.home.domain.repository.HomeRepository
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
        }
    }

    private fun loadHomeSections() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            homeRepository.getHomeSections()
                .onSuccess { sections ->
                    _state.update {
                        it.copy(
                            sections = sections.map { section -> section.toHomeSectionUi() },
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false) }
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
