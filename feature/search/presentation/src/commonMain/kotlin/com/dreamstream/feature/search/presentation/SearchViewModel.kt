package com.dreamstream.feature.search.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamstream.core.domain.util.onFailure
import com.dreamstream.core.domain.util.onSuccess
import com.dreamstream.feature.search.domain.repository.SearchRepository
import com.dreamstream.feature.search.presentation.util.toSearchResultUi
import com.dreamstream.feature.search.presentation.util.toUiText
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel for the Search screen.
 *
 * The current [query][SearchState.query] is persisted in [SavedStateHandle] so
 * that typing state survives process death and configuration changes. Results
 * are re-fetched from the repository on restoration if the saved query is
 * non-blank.
 *
 * Each call to [performSearch] cancels any in-flight search so that only the
 * latest query is active at any time. Add call-site debounce (e.g. 300 ms)
 * before wiring to a network source that must not be hit on every keystroke.
 */
class SearchViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val searchRepository: SearchRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        SearchState(query = savedStateHandle.get<String>(KEY_QUERY) ?: ""),
    )
    val state = _state.asStateFlow()

    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    private var searchJob: Job? = null

    init {
        val savedQuery = _state.value.query
        if (savedQuery.isNotBlank()) {
            performSearch(savedQuery)
        }
    }

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnQueryChange -> onQueryChange(action.query)
            is SearchAction.OnClearQuery -> clearQuery()
            is SearchAction.OnResultClick -> navigateToDetail(action.contentId)
            is SearchAction.OnBackClick -> navigateBack()
        }
    }

    private fun onQueryChange(query: String) {
        savedStateHandle[KEY_QUERY] = query
        _state.update { it.copy(query = query, error = null) }
        if (query.isBlank()) {
            searchJob?.cancel()
            _state.update { it.copy(results = emptyList(), isLoading = false) }
        } else {
            performSearch(query)
        }
    }

    private fun clearQuery() {
        savedStateHandle[KEY_QUERY] = ""
        searchJob?.cancel()
        _state.update { it.copy(query = "", results = emptyList(), isLoading = false, error = null) }
    }

    private fun navigateToDetail(contentId: String) {
        viewModelScope.launch {
            _events.send(SearchEvent.NavigateToDetail(contentId))
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(SearchEvent.NavigateBack)
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            searchRepository.search(query)
                .onSuccess { results ->
                    _state.update {
                        it.copy(
                            results = results.map { r -> r.toSearchResultUi() },
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    private companion object {
        const val KEY_QUERY = "query"
    }
}
