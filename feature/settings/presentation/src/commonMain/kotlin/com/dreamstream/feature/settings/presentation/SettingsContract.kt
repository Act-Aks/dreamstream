package com.dreamstream.feature.settings.presentation

import androidx.compose.runtime.Stable
import com.dreamstream.feature.settings.domain.model.AppLanguage

@Stable
data class SettingsState(
    val currentLanguage: AppLanguage = AppLanguage.SYSTEM,
)

sealed interface SettingsAction {
    data class OnLanguageSelected(val language: AppLanguage) : SettingsAction
    data object OnBackClick : SettingsAction
}

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
}
