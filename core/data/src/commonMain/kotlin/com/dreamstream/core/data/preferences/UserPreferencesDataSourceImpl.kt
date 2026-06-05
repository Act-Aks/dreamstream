package com.dreamstream.core.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.catalog.ThemeMode
import com.dreamstream.core.domain.model.user.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json


/**
 * [UserPreferencesDataSource] implementation backed by DataStore.
 *
 * Maps between strongly-typed [UserPreferences] and flat key-value
 * entries defined in [UserPreferencesKeys].
 */
class UserPreferencesDataSourceImpl(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
    loggerFactory: LoggerFactory
) : UserPreferencesDataSource {

    private val logger = loggerFactory.get("UserPreferences")

    /**
     * Base flow of preferences with error handling.
     */
    private fun prefsFlow(): Flow<Preferences> = dataStore.data.catch { throwable ->
        logger.error(throwable) { "Error reading preferences" }
        emit(emptyPreferences())
    }

    override fun observeUserPreferences(): Flow<UserPreferences> = prefsFlow().map { prefs ->
        UserPreferences(
            language = prefs[UserPreferencesKeys.LANGUAGE] ?: "en",
            theme = prefs[UserPreferencesKeys.THEME_MODE]?.let {
                runCatching {
                    ThemeMode.valueOf(it)
                }.getOrNull()
            } ?: ThemeMode.System,
            dynamicColor = prefs[UserPreferencesKeys.DYNAMIC_COLOR] ?: true,
            accentColorArgb = prefs[UserPreferencesKeys.ACCENT_COLOR],

            defaultQuality = prefs[UserPreferencesKeys.DEFAULT_QUALITY]?.let {
                runCatching {
                    Quality.valueOf(
                        it
                    )
                }.getOrNull()
            } ?: Quality.Auto,
            autoPlay = prefs[UserPreferencesKeys.AUTO_PLAY] ?: true,
            skipIntro = prefs[UserPreferencesKeys.SKIP_INTRO] ?: false,
            defaultSubtitleLanguage = prefs[UserPreferencesKeys.DEFAULT_SUBTITLE_LANGUAGE],
            playerGestures = prefs[UserPreferencesKeys.PLAYER_GESTURES] ?: true,
            backgroundPlayback = prefs[UserPreferencesKeys.BACKGROUND_PLAYBACK] ?: false,
            pipEnabled = prefs[UserPreferencesKeys.PIP_ENABLED] ?: true,
            defaultPlaybackSpeed = prefs[UserPreferencesKeys.DEFAULT_PLAYBACK_SPEED] ?: 1.0f,
            showNextEpisodeButton = prefs[UserPreferencesKeys.SHOW_NEXT_EPISODE_BUTTON] ?: true,

            adultContentEnabled = prefs[UserPreferencesKeys.ADULT_CONTENT_ENABLED] ?: false,
            preferredContentType = prefs[UserPreferencesKeys.PREFERRED_CONTENT_TYPE]?.let {
                runCatching {
                    ContentType.valueOf(it)
                }.getOrNull()
            },
            hiddenProviderIds = prefs[UserPreferencesKeys.HIDDEN_PROVIDER_IDS]?.let { raw ->
                runCatching { json.decodeFromString<List<String>>(raw) }.getOrDefault(
                    emptyList()
                )
            } ?: emptyList(),

            downloadPath = prefs[UserPreferencesKeys.DOWNLOAD_PATH],
            downloadQuality = prefs[UserPreferencesKeys.DOWNLOAD_QUALITY]?.let {
                runCatching {
                    Quality.valueOf(it)
                }.getOrNull()
            } ?: Quality.HD,
            downloadOverWifiOnly = prefs[UserPreferencesKeys.DOWNLOAD_OVER_WIFI_ONLY] ?: true,
            maxConcurrentDownloads = prefs[UserPreferencesKeys.MAX_CONCURRENT_DOWNLOADS] ?: 3,

            autoUpdatePlugins = prefs[UserPreferencesKeys.AUTO_UPDATE_PLUGINS] ?: true,
            showPluginUpdateBadge = prefs[UserPreferencesKeys.SHOW_PLUGIN_UPDATE_BADGE] ?: true,

            gridColumns = prefs[UserPreferencesKeys.GRID_COLUMNS] ?: 3,
            compactMode = prefs[UserPreferencesKeys.COMPACT_MODE] ?: false,
            showWatchProgress = prefs[UserPreferencesKeys.SHOW_WATCH_PROGRESS] ?: true,

            onboardingComplete = prefs[UserPreferencesKeys.ONBOARDING_COMPLETE] ?: true,
            firstLaunch = prefs[UserPreferencesKeys.FIRST_LAUNCH] ?: false,
        )
    }.catch { throwable ->
        logger.error(throwable) { "Error mapping UserPreferences" }
        emit(UserPreferences())
    }

    // General reads

    override fun observeLanguage(): Flow<String> =
        prefsFlow().map { it[UserPreferencesKeys.LANGUAGE] ?: "en" }

    override fun observeThemeMode(): Flow<ThemeMode> = prefsFlow().map {
        it[UserPreferencesKeys.THEME_MODE]?.let { name -> runCatching { ThemeMode.valueOf(name) }.getOrNull() }
            ?: ThemeMode.System
    }

    override fun observeDynamicColor(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.DYNAMIC_COLOR] ?: true }

    override fun observeAccentColorArgb(): Flow<Long?> =
        prefsFlow().map { it[UserPreferencesKeys.ACCENT_COLOR] }

    // Player reads

    override fun observeDefaultQuality(): Flow<Quality> = prefsFlow().map {
        it[UserPreferencesKeys.DEFAULT_QUALITY]?.let { name -> runCatching { Quality.valueOf(name) }.getOrNull() }
            ?: Quality.Auto
    }

    override fun observeAutoPlay(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.AUTO_PLAY] ?: true }

    override fun observeSkipIntro(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.SKIP_INTRO] ?: false }

    override fun observeDefaultSubtitleLanguage(): Flow<String?> =
        prefsFlow().map { it[UserPreferencesKeys.DEFAULT_SUBTITLE_LANGUAGE] }

    override fun observePlayerGestures(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.PLAYER_GESTURES] ?: true }

    override fun observeBackgroundPlayback(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.BACKGROUND_PLAYBACK] ?: false }

    override fun observePipEnabled(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.PIP_ENABLED] ?: true }

    override fun observeDefaultPlaybackSpeed(): Flow<Float> =
        prefsFlow().map { it[UserPreferencesKeys.DEFAULT_PLAYBACK_SPEED] ?: 1.0f }

    override fun observeShowNextEpisodeButton(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.SHOW_NEXT_EPISODE_BUTTON] ?: true }

    // Content reads

    override fun observeAdultContentEnabled(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.ADULT_CONTENT_ENABLED] ?: false }

    override fun observePreferredContentType(): Flow<ContentType?> = prefsFlow().map {
        it[UserPreferencesKeys.PREFERRED_CONTENT_TYPE]?.let { name ->
            runCatching {
                ContentType.valueOf(
                    name
                )
            }.getOrNull()
        }
    }

    override fun observeHiddenProviderIds(): Flow<List<String>> = prefsFlow().map {
        val raw = it[UserPreferencesKeys.HIDDEN_PROVIDER_IDS] ?: "[]"
        runCatching { json.decodeFromString<List<String>>(raw) }.getOrDefault(emptyList())
    }

    // Download reads

    override fun observeDownloadPath(): Flow<String?> =
        prefsFlow().map { it[UserPreferencesKeys.DOWNLOAD_PATH] }

    override fun observeDownloadQuality(): Flow<Quality> = prefsFlow().map {
        it[UserPreferencesKeys.DOWNLOAD_QUALITY]?.let { name -> runCatching { Quality.valueOf(name) }.getOrNull() }
            ?: Quality.HD
    }

    override fun observeDownloadOverWifiOnly(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.DOWNLOAD_OVER_WIFI_ONLY] ?: true }

    override fun observeMaxConcurrentDownloads(): Flow<Int> =
        prefsFlow().map { it[UserPreferencesKeys.MAX_CONCURRENT_DOWNLOADS] ?: 3 }

    // Plugin reads

    override fun observeAutoUpdatePlugins(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.AUTO_UPDATE_PLUGINS] ?: true }

    override fun observeShowPluginUpdateBadge(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.SHOW_PLUGIN_UPDATE_BADGE] ?: true }

    // UI reads

    override fun observeGridColumns(): Flow<Int> =
        prefsFlow().map { it[UserPreferencesKeys.GRID_COLUMNS] ?: 3 }

    override fun observeCompactMode(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.COMPACT_MODE] ?: false }

    override fun observeShowWatchProgress(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.SHOW_WATCH_PROGRESS] ?: true }

    // Onboarding & First launch reads

    override fun observeOnboardingComplete(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.ONBOARDING_COMPLETE] ?: true }

    override fun observeFirstLaunch(): Flow<Boolean> =
        prefsFlow().map { it[UserPreferencesKeys.FIRST_LAUNCH] ?: false }

    // General writes

    override suspend fun setLanguage(language: String) {
        dataStore.edit { it[UserPreferencesKeys.LANGUAGE] = language }
    }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { it[UserPreferencesKeys.THEME_MODE] = themeMode.name }
    }

    override suspend fun setDynamicColor(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.DYNAMIC_COLOR] = enabled }
    }

    override suspend fun setAccentColorArgb(argb: Long?) {
        dataStore.edit { prefs ->
            if (argb == null) prefs.remove(UserPreferencesKeys.ACCENT_COLOR)
            else prefs[UserPreferencesKeys.ACCENT_COLOR] = argb
        }
    }

    // Player writes

    override suspend fun setDefaultQuality(quality: Quality) {
        dataStore.edit { it[UserPreferencesKeys.DEFAULT_QUALITY] = quality.name }
    }

    override suspend fun setAutoPlay(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.AUTO_PLAY] = enabled }
    }

    override suspend fun setSkipIntro(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.SKIP_INTRO] = enabled }
    }

    override suspend fun setDefaultSubtitleLanguage(language: String?) {
        dataStore.edit { prefs ->
            if (language == null) prefs.remove(UserPreferencesKeys.DEFAULT_SUBTITLE_LANGUAGE)
            else prefs[UserPreferencesKeys.DEFAULT_SUBTITLE_LANGUAGE] = language
        }
    }

    override suspend fun setPlayerGestures(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.PLAYER_GESTURES] = enabled }
    }

    override suspend fun setBackgroundPlayback(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.BACKGROUND_PLAYBACK] = enabled }
    }

    override suspend fun setPipEnabled(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.PIP_ENABLED] = enabled }
    }

    override suspend fun setDefaultPlaybackSpeed(speed: Float) {
        dataStore.edit { it[UserPreferencesKeys.DEFAULT_PLAYBACK_SPEED] = speed }
    }

    override suspend fun setShowNextEpisodeButton(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.SHOW_NEXT_EPISODE_BUTTON] = enabled }
    }

    // Content writes

    override suspend fun setAdultContentEnabled(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.ADULT_CONTENT_ENABLED] = enabled }
    }

    override suspend fun setPreferredContentType(type: ContentType?) {
        dataStore.edit { prefs ->
            if (type == null) prefs.remove(UserPreferencesKeys.PREFERRED_CONTENT_TYPE)
            else prefs[UserPreferencesKeys.PREFERRED_CONTENT_TYPE] = type.name
        }
    }

    override suspend fun setHiddenProviderIds(ids: List<String>) {
        val encoded = json.encodeToString(ids)
        dataStore.edit { it[UserPreferencesKeys.HIDDEN_PROVIDER_IDS] = encoded }
    }

    // Download writes

    override suspend fun setDownloadPath(path: String?) {
        dataStore.edit { prefs ->
            if (path == null) prefs.remove(UserPreferencesKeys.DOWNLOAD_PATH)
            else prefs[UserPreferencesKeys.DOWNLOAD_PATH] = path
        }
    }

    override suspend fun setDownloadQuality(quality: Quality) {
        dataStore.edit { it[UserPreferencesKeys.DOWNLOAD_QUALITY] = quality.name }
    }

    override suspend fun setDownloadOverWifiOnly(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.DOWNLOAD_OVER_WIFI_ONLY] = enabled }
    }

    override suspend fun setMaxConcurrentDownloads(max: Int) {
        dataStore.edit { it[UserPreferencesKeys.MAX_CONCURRENT_DOWNLOADS] = max }
    }

    // Plugin writes

    override suspend fun setAutoUpdatePlugins(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.AUTO_UPDATE_PLUGINS] = enabled }
    }

    override suspend fun setShowPluginUpdateBadge(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.SHOW_PLUGIN_UPDATE_BADGE] = enabled }
    }

    // UI writes

    override suspend fun setGridColumns(columns: Int) {
        dataStore.edit { it[UserPreferencesKeys.GRID_COLUMNS] = columns }
    }

    override suspend fun setCompactMode(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.COMPACT_MODE] = enabled }
    }

    override suspend fun setShowWatchProgress(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.SHOW_WATCH_PROGRESS] = enabled }
    }

    // Onboarding & First launch

    override suspend fun setOnboardingComplete(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.ONBOARDING_COMPLETE] = enabled }
    }

    override suspend fun setFirstLaunch(enabled: Boolean) {
        dataStore.edit { it[UserPreferencesKeys.FIRST_LAUNCH] = enabled }
    }

    override suspend fun clearAll() {
        dataStore.edit { it.clear() }
    }
}
