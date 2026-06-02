package com.dreamstream.feature.settings.presentation

import androidx.compose.runtime.Stable
import com.dreamstream.feature.settings.domain.model.AppLanguage

@Stable
data class SettingsState(
    val currentLanguage: AppLanguage = AppLanguage.SYSTEM,
    val isDarkModeEnabled: Boolean = false,
    val isNotificationsEnabled: Boolean = true,
)

sealed interface SettingsAction {
    data class OnLanguageSelected(val language: AppLanguage) : SettingsAction
    data class OnDarkModeToggled(val enabled: Boolean) : SettingsAction
    data class OnNotificationsToggled(val enabled: Boolean) : SettingsAction
    data object OnBackClick : SettingsAction
}

sealed interface SettingsEvent {
    data object NavigateBack : SettingsEvent
}
