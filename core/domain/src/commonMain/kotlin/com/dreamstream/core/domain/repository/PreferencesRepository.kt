package com.dreamstream.core.domain.repository

import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.catalog.ThemeMode
import com.dreamstream.core.domain.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun observeUserPreferences(): Flow<UserPreferences>
    fun observeThemeMode(): Flow<ThemeMode>
    fun observeDynamicColor(): Flow<Boolean>
    fun observeOnboardingComplete(): Flow<Boolean>

    suspend fun setThemeMode(mode: ThemeMode)
    suspend fun setDynamicColor(enabled: Boolean)
    suspend fun setLanguage(language: String)
    suspend fun setDefaultQuality(quality: Quality)
    suspend fun setAutoPlay(enabled: Boolean)
    suspend fun setAdultContentEnabled(enabled: Boolean)
    suspend fun setOnboardingComplete(complete: Boolean)
    suspend fun setPlayerGestures(enabled: Boolean)
    suspend fun setPipEnabled(enabled: Boolean)
    suspend fun setPlaybackSpeed(speed: Float)
    suspend fun setGridColumns(columns: Int)
    suspend fun clearAll()
}
