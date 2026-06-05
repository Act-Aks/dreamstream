package com.dreamstream.core.data.preferences

import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.catalog.ThemeMode
import com.dreamstream.core.domain.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction over user preference persistence.
 *
 * Exposes [UserPreferences] as a reactive flow and provides
 * fine-grained read/write operations for individual properties.
 * Implementations are responsible for mapping to storage (DataStore).
 */
interface UserPreferencesDataSource {

    /**
     * Observe the full [UserPreferences] snapshot.
     *
     * Emits new values whenever underlying storage changes.
     */
    fun observeUserPreferences(): Flow<UserPreferences>

    // General
    fun observeLanguage(): Flow<String>
    fun observeThemeMode(): Flow<ThemeMode>
    fun observeDynamicColor(): Flow<Boolean>
    fun observeAccentColorArgb(): Flow<Long?>

    // Player
    fun observeDefaultQuality(): Flow<Quality>
    fun observeAutoPlay(): Flow<Boolean>
    fun observeSkipIntro(): Flow<Boolean>
    fun observeDefaultSubtitleLanguage(): Flow<String?>
    fun observePlayerGestures(): Flow<Boolean>
    fun observeBackgroundPlayback(): Flow<Boolean>
    fun observePipEnabled(): Flow<Boolean>
    fun observeDefaultPlaybackSpeed(): Flow<Float>
    fun observeShowNextEpisodeButton(): Flow<Boolean>

    // Content
    fun observeAdultContentEnabled(): Flow<Boolean>
    fun observePreferredContentType(): Flow<ContentType?>
    fun observeHiddenProviderIds(): Flow<List<String>>

    // Downloads
    fun observeDownloadPath(): Flow<String?>
    fun observeDownloadQuality(): Flow<Quality>
    fun observeDownloadOverWifiOnly(): Flow<Boolean>
    fun observeMaxConcurrentDownloads(): Flow<Int>

    // Plugin
    fun observeAutoUpdatePlugins(): Flow<Boolean>
    fun observeShowPluginUpdateBadge(): Flow<Boolean>

    // UI
    fun observeGridColumns(): Flow<Int>
    fun observeCompactMode(): Flow<Boolean>
    fun observeShowWatchProgress(): Flow<Boolean>

    // Onboarding & First launch
    fun observeOnboardingComplete(): Flow<Boolean>
    fun observeFirstLaunch(): Flow<Boolean>

    // Writes – general
    suspend fun setLanguage(language: String)
    suspend fun setThemeMode(themeMode: ThemeMode)
    suspend fun setDynamicColor(enabled: Boolean)
    suspend fun setAccentColorArgb(argb: Long?)

    // Writes – player
    suspend fun setDefaultQuality(quality: Quality)
    suspend fun setAutoPlay(enabled: Boolean)
    suspend fun setSkipIntro(enabled: Boolean)
    suspend fun setDefaultSubtitleLanguage(language: String?)
    suspend fun setPlayerGestures(enabled: Boolean)
    suspend fun setBackgroundPlayback(enabled: Boolean)
    suspend fun setPipEnabled(enabled: Boolean)
    suspend fun setDefaultPlaybackSpeed(speed: Float)
    suspend fun setShowNextEpisodeButton(enabled: Boolean)

    // Writes – content
    suspend fun setAdultContentEnabled(enabled: Boolean)
    suspend fun setPreferredContentType(type: ContentType?)
    suspend fun setHiddenProviderIds(ids: List<String>)

    // Writes – downloads
    suspend fun setDownloadPath(path: String?)
    suspend fun setDownloadQuality(quality: Quality)
    suspend fun setDownloadOverWifiOnly(enabled: Boolean)
    suspend fun setMaxConcurrentDownloads(max: Int)

    // Writes – plugin
    suspend fun setAutoUpdatePlugins(enabled: Boolean)
    suspend fun setShowPluginUpdateBadge(enabled: Boolean)

    // Writes – UI
    suspend fun setGridColumns(columns: Int)
    suspend fun setCompactMode(enabled: Boolean)
    suspend fun setShowWatchProgress(enabled: Boolean)

    // Writes – Onboarding
    suspend fun setOnboardingComplete(enabled: Boolean)
    suspend fun setFirstLaunch(enabled: Boolean)

    /**
     * Clear all stored preference values for this user.
     */
    suspend fun clearAll()
}
