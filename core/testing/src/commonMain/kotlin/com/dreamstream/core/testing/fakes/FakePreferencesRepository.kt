package com.dreamstream.core.testing.fakes

import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.catalog.ThemeMode
import com.dreamstream.core.domain.model.user.UserPreferences
import com.dreamstream.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakePreferencesRepository : PreferencesRepository {

    private val _preferences = MutableStateFlow(UserPreferences())

    override fun observeUserPreferences(): Flow<UserPreferences> = _preferences

    override fun observeThemeMode(): Flow<ThemeMode> = _preferences.map { it.theme }

    override fun observeDynamicColor(): Flow<Boolean> = _preferences.map { it.dynamicColor }

    override fun observeOnboardingComplete(): Flow<Boolean> = MutableStateFlow(false)

    override suspend fun setThemeMode(mode: ThemeMode) {
        _preferences.update { it.copy(theme = mode) }
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        _preferences.update { it.copy(dynamicColor = enabled) }
    }

    override suspend fun setLanguage(language: String) {
        _preferences.update { it.copy(language = language) }
    }

    override suspend fun setDefaultQuality(quality: Quality) {
        _preferences.update { it.copy(defaultQuality = quality) }
    }

    override suspend fun setAutoPlay(enabled: Boolean) {
        _preferences.update { it.copy(autoPlay = enabled) }
    }

    override suspend fun setAdultContentEnabled(enabled: Boolean) {
        _preferences.update { it.copy(adultContentEnabled = enabled) }
    }

    override suspend fun setOnboardingComplete(complete: Boolean) {}

    override suspend fun setPlayerGestures(enabled: Boolean) {
        _preferences.update { it.copy(playerGestures = enabled) }
    }

    override suspend fun setPipEnabled(enabled: Boolean) {
        _preferences.update { it.copy(pipEnabled = enabled) }
    }

    override suspend fun setPlaybackSpeed(speed: Float) {
        _preferences.update { it.copy(defaultPlaybackSpeed = speed) }
    }

    override suspend fun setGridColumns(columns: Int) {
        _preferences.update { it.copy(gridColumns = columns) }
    }

    override suspend fun clearAll() {
        _preferences.update { UserPreferences() }
    }
}
