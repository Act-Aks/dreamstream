package com.dreamstream.core.data.repository

import com.dreamstream.core.data.preferences.UserPreferencesDataSource
import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.catalog.ThemeMode
import com.dreamstream.core.domain.model.user.UserPreferences
import com.dreamstream.core.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow

class PreferencesRepositoryImpl(
    private val preferencesDataSource: UserPreferencesDataSource,
) : PreferencesRepository {

    override fun observeUserPreferences(): Flow<UserPreferences> =
        preferencesDataSource.observeUserPreferences()

    override fun observeThemeMode(): Flow<ThemeMode> = preferencesDataSource.observeThemeMode()

    override fun observeDynamicColor(): Flow<Boolean> = preferencesDataSource.observeDynamicColor()

    override fun observeOnboardingComplete(): Flow<Boolean> =
        preferencesDataSource.observeOnboardingComplete()

    override suspend fun setThemeMode(mode: ThemeMode) = preferencesDataSource.setThemeMode(mode)

    override suspend fun setDynamicColor(enabled: Boolean) =
        preferencesDataSource.setDynamicColor(enabled)

    override suspend fun setLanguage(language: String) = preferencesDataSource.setLanguage(language)

    override suspend fun setDefaultQuality(quality: Quality) =
        preferencesDataSource.setDefaultQuality(quality)

    override suspend fun setAutoPlay(enabled: Boolean) = preferencesDataSource.setAutoPlay(enabled)

    override suspend fun setAdultContentEnabled(enabled: Boolean) =
        preferencesDataSource.setAdultContentEnabled(enabled)

    override suspend fun setOnboardingComplete(complete: Boolean) =
        preferencesDataSource.setOnboardingComplete(complete)

    override suspend fun setPlayerGestures(enabled: Boolean) =
        preferencesDataSource.setPlayerGestures(enabled)

    override suspend fun setPipEnabled(enabled: Boolean) =
        preferencesDataSource.setPipEnabled(enabled)

    override suspend fun setPlaybackSpeed(speed: Float) =
        preferencesDataSource.setDefaultPlaybackSpeed(speed)

    override suspend fun setGridColumns(columns: Int) =
        preferencesDataSource.setGridColumns(columns)

    override suspend fun clearAll() = preferencesDataSource.clearAll()
}
