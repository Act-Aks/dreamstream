package com.dreamstream.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamstream.feature.settings.domain.model.AppLanguage
import com.dreamstream.feature.settings.domain.repository.LanguageRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val languageRepository: LanguageRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(currentLanguage = languageRepository.getCurrentLanguage()),
    )
    val state = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnLanguageSelected -> applyLanguage(action.language)
            is SettingsAction.OnBackClick -> navigateBack()
        }
    }

    private fun applyLanguage(language: AppLanguage) {
        _state.update { it.copy(currentLanguage = language) }
        languageRepository.applyLanguage(language)
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(SettingsEvent.NavigateBack)
        }
    }
}
