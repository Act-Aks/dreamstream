package com.dreamstream.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dreamstream.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    init {
        settingsRepository.languageFlow()
            .onEach { lang -> _state.update { it.copy(currentLanguage = lang) } }
            .launchIn(viewModelScope)

        settingsRepository.darkModeFlow()
            .onEach { enabled -> _state.update { it.copy(isDarkModeEnabled = enabled) } }
            .launchIn(viewModelScope)

        settingsRepository.notificationsFlow()
            .onEach { enabled -> _state.update { it.copy(isNotificationsEnabled = enabled) } }
            .launchIn(viewModelScope)
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnLanguageSelected    -> applyLanguage(action.language)
            is SettingsAction.OnDarkModeToggled     -> setDarkMode(action.enabled)
            is SettingsAction.OnNotificationsToggled -> setNotifications(action.enabled)
            is SettingsAction.OnBackClick           -> navigateBack()
        }
    }

    private fun applyLanguage(language: com.dreamstream.feature.settings.domain.model.AppLanguage) {
        viewModelScope.launch { settingsRepository.applyLanguage(language) }
    }

    private fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setDarkModeEnabled(enabled) }
    }

    private fun setNotifications(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setNotificationsEnabled(enabled) }
    }

    private fun navigateBack() {
        viewModelScope.launch { _events.send(SettingsEvent.NavigateBack) }
    }
}
