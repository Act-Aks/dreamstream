package com.dreamstream.feature.details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamstream.core.domain.util.onFailure
import com.dreamstream.core.domain.util.onSuccess
import com.dreamstream.feature.details.domain.repository.DetailsRepository
import com.dreamstream.feature.details.presentation.util.toDetailContentUi
import com.dreamstream.feature.details.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val contentId: String,
    private val detailsRepository: DetailsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(DetailsState())
    val state = _state.asStateFlow()

    private val _events = Channel<DetailsEvent>()
    val events = _events.receiveAsFlow()

    init {
        loadContentDetail()
    }

    fun onAction(action: DetailsAction) {
        when (action) {
            is DetailsAction.OnBackClick -> navigateBack()
            is DetailsAction.OnRetry -> loadContentDetail()
        }
    }

    private fun loadContentDetail() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            detailsRepository.getContentDetail(contentId)
                .onSuccess { detail ->
                    _state.update {
                        it.copy(
                            content = detail.toDetailContentUi(),
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update { it.copy(isLoading = false, error = error.toUiText()) }
                }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(DetailsEvent.NavigateBack)
        }
    }
}
