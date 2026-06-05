package com.dreamstream.core.domain.model.user

import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.catalog.ThemeMode
import kotlinx.serialization.Serializable

/**
 * User preferences for DreamStream app behavior, UI, and content filtering.
 *
 * [UserPreferences] is the central settings model managed by ***`PreferenceManager`***
 * and persisted via DataStore/Room. It controls app-wide behavior including theme,
 * player defaults, content filtering, download settings, and plugin management.
 *
 * This class is organized into logical sections:
 * - **General**: [language], [theme], [dynamicColor], [accentColorArgb]
 * - **Player**: [defaultQuality], [autoPlay], [skipIntro], [playerGestures], etc.
 * - **Content**: [adultContentEnabled], [preferredContentType], [hiddenProviderIds]
 * - **Downloads**: [downloadPath], [downloadQuality], [downloadOverWifiOnly], etc.
 * - **Plugin**: [autoUpdatePlugins], [showPluginUpdateBadge]
 * - **UI**: [gridColumns], [compactMode], [showWatchProgress]
 *
 * ## Key Properties:
 * - [theme]: App theme mode (***optional, defaults to [ThemeMode.System]***)
 * - [dynamicColor]: Material You dynamic color (***optional, defaults to true***)
 * - [defaultQuality]: Player quality default (***optional, defaults to [Quality.Auto]***)
 * - [autoPlay]: Auto-play next episode (***optional, defaults to true***)
 * - [adultContentEnabled]: Parental control flag (***optional, defaults to false***)
 * - [gridColumns]: Grid columns in browse screens (***optional, defaults to 3***)
 *
 * ## General Settings:
 * | Property            | Default | Description                                  |
 * |---------------------|---------|----------------------------------------------|
 * | [language]          | `"en"`  | App UI language (BCP 47 tag)                 |
 * | [theme]             | `System`| Light/Dark/System theme                      |
 * | [dynamicColor]      | `true`  | Use Android 12+ Material You colors          |
 * | [accentColorArgb]   | `null`  | Custom accent color (ARGB) if overriding dynamic |
 *
 * ## Player Settings:
 * | Property                | Default | Description                                      |
 * |-------------------------|---------|--------------------------------------------------|
 * | [defaultQuality]        | `Auto`  | Best/HD/SD/4K/Auto for streams                   |
 * | [autoPlay]              | `true`  | Auto-play next episode in series                 |
 * | [skipIntro]             | `false` | Auto-skip intro button (requires intro detection)|
 * | [defaultSubtitleLanguage]| `null` | Preferred subtitle language (e.g., `"en"`)       |
 * | [playerGestures]        | `true`  | Swipe for brightness/volume/seek                 |
 * | [backgroundPlayback]    | `false` | Continue playing when app minimized              |
 * | [pipEnabled]            | `true`  | Picture-in-Picture mode support                  |
 * | [defaultPlaybackSpeed]  | `1.0`   | Playback speed (0.5x–2.0x)                       |
 * | [showNextEpisodeButton] | `true`  | Show "Next Episode" after completion             |
 *
 * ## Content Settings:
 * | Property               | Default | Description                                      |
 * |------------------------|---------|--------------------------------------------------|
 * | [adultContentEnabled]  | `false` | Show adult/18+ content (parental control)        |
 * | [preferredContentType] | `null`  | Filter by type (Anime/Movie/Series/Live)         |
 * | [hiddenProviderIds]    | `[]`    | Hide plugins from search/results                 |
 *
 * ## Download Settings:
 * | Property                 | Default | Description                                     |
 * |--------------------------|---------|-------------------------------------------------|
 * | [downloadPath]           | `null`  | Custom download directory (null = app default)  |
 * | [downloadQuality]        | `HD`    | Quality for downloaded episodes                 |
 * | [downloadOverWifiOnly]   | `true`  | Only download on Wi-Fi                          |
 * | [maxConcurrentDownloads] | `3`     | Parallel download limit                         |
 *
 * ## Plugin Settings:
 * | Property               | Default | Description                                      |
 * |------------------------|---------|--------------------------------------------------|
 * | [autoUpdatePlugins]    | `true`  | Auto-update plugins when available               |
 * | [showPluginUpdateBadge]| `true`  | Show "Update" badge in plugin store              |
 *
 * ## UI Settings:
 * | Property           | Default | Description                                      |
 * |--------------------|---------|--------------------------------------------------|
 * | [gridColumns]      | `3`     | Columns in grid view (2–6)                       |
 * | [compactMode]      | `false` | Compact list view (smaller cards)                |
 * | [showWatchProgress]| `true`  | Show progress bars on watched content            |
 *
 * ## Usage:
 * ```kotlin
 * // Load preferences
 * val prefs = preferenceManager.getPreferences()
 *
 * // Check adult content
 * if (!prefs.adultContentEnabled) showAdultContentWarning()
 *
 * // Apply theme
 * AppCompatDelegate.setDefaultNightMode(
 *     when (prefs.theme) {
 *         ThemeMode.Light -> AppCompatDelegate.MODE_NIGHT_NO
 *         ThemeMode.Dark -> AppCompatDelegate.MODE_NIGHT_YES
 *         ThemeMode.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
 *     }
 * )
 *
 * // Configure player
 * playerView.setQuality(prefs.defaultQuality)
 * playerView.setAutoPlay(prefs.autoPlay)
 * playerView.setPlaybackSpeed(prefs.defaultPlaybackSpeed)
 * ```
 *
 * ## Updating Preferences:
 * ```kotlin
 * // Update single property
 * preferenceManager.updatePreferences { copy(theme = ThemeMode.Dark) }
 *
 * // Update multiple properties
 * preferenceManager.updatePreferences {
 *     copy(
 *         autoPlay = false,
 *         defaultQuality = Quality.FHD,
 *         adultContentEnabled = true
 *     )
 * }
 *
 * // Reset to defaults
 * preferenceManager.resetPreferences()
 * ```
 *
 * ## Validation:
 * ```kotlin
 * fun validate(prefs: UserPreferences): Boolean {
 *     if (prefs.gridColumns !in 2..6) return false
 *     if (prefs.defaultPlaybackSpeed !in 0.5f..2.0f) return false
 *     if (prefs.maxConcurrentDownloads !in 1..10) return false
 *     return true
 * }
 * ```
 *
 * ## UI Screens Affected:
 * - **Settings Screen**: All preferences editable here
 * - **Player Screen**: [defaultQuality], [autoPlay], [skipIntro], [playerGestures], etc.
 * - **Browse/Home**: [preferredContentType], [hiddenProviderIds], [gridColumns], [compactMode]
 * - **Downloads**: [downloadPath], [downloadQuality], [downloadOverWifiOnly]
 * - **Plugin Store**: [autoUpdatePlugins], [showPluginUpdateBadge]
 * - **Content Filters**: [adultContentEnabled] (hides 18+ plugins/content)
 *
 * ## Related:
 * - Manager: ***`PreferenceManager`***
 * - Theme mode: [ThemeMode]
 * - Quality: [Quality]
 * - Content type: [ContentType]
 * - Bookmarks: [Bookmark]
 * - Categories: [BookmarkCategory]
 *
 * @since 1.0.0
 * @author
 * DreamStream Team
 */
@Serializable
data class UserPreferences(
    // ==================== GENERAL ====================

    /**
     * App UI language.
     *
     * ***Optional***. Defaults to `"en"` (English). BCP 47 tag.
     * Example: `"en"`, `"es"`, `"ja"`, `"pt-BR"`.
     * Requires app restart to take effect.
     */
    val language: String = "en",

    /**
     * App theme mode.
     *
     * ***Optional***. Defaults to [ThemeMode.System]. Controls light/dark theme.
     * [ThemeMode.System] follows device setting.
     */
    val theme: ThemeMode = ThemeMode.System,

    /**
     * Use dynamic color (Material You).
     *
     * ***Optional***. Defaults to `true`. Enables Android 12+ dynamic theming
     * based on wallpaper. Ignored on Android < 12.
     */
    val dynamicColor: Boolean = true,

    /**
     * Custom accent color (ARGB).
     *
     * ***Optional***. Null if using dynamic color or system default.
     * Override dynamic color with custom accent. Example: `0xFF6200EE` (purple).
     */
    val accentColorArgb: Long? = null,

    // ==================== PLAYER ====================

    /**
     * Default stream quality.
     *
     * ***Optional***. Defaults to [Quality.Auto]. Selected when starting a stream.
     * [Quality.Auto] picks best available. Other options: [Quality.FullHd], [Quality.HD], [Quality.Medium], [Quality.FourK].
     */
    val defaultQuality: Quality = Quality.Auto,

    /**
     * Auto-play next episode.
     *
     * ***Optional***. Defaults to `true`. When `true`, automatically starts
     * next episode in series after current one ends.
     */
    val autoPlay: Boolean = true,

    /**
     * Auto-skip intro.
     *
     * ***Optional***. Defaults to `false`. When `true` and intro timestamps
     * are available, automatically skip intro on episode start.
     */
    val skipIntro: Boolean = false,

    /**
     * Default subtitle language.
     *
     * ***Optional***. Null if no preference. Subtitle track auto-selected
     * if available (e.g., `"en"`, `"es"`, `"ja"`).
     */
    val defaultSubtitleLanguage: String? = null,

    /**
     * Enable player gestures.
     *
     * ***Optional***. Defaults to `true`. Swipe vertically for brightness/volume,
     * horizontally for seek. Disable for accessibility.
     */
    val playerGestures: Boolean = true,

    /**
     * Background playback.
     *
     * ***Optional***. Defaults to `false`. When `true`, continue playing
     * when app is minimized or screen is off (audio-only).
     */
    val backgroundPlayback: Boolean = false,

    /**
     * Picture-in-Picture enabled.
     *
     * ***Optional***. Defaults to `true`. Allow PiP mode when home button
     * pressed during playback (Android 26+).
     */
    val pipEnabled: Boolean = true,

    /**
     * Default playback speed.
     *
     * ***Optional***. Defaults to `1.0f` (normal). Range: `0.5f`–`2.0f`.
     * Example: `1.25f` (1.25x), `1.5f` (1.5x), `2.0f` (2x).
     */
    val defaultPlaybackSpeed: Float = 1.0f,

    /**
     * Show next episode button.
     *
     * ***Optional***. Defaults to `true`. Show "Next Episode" card/button
     * after current episode completes.
     */
    val showNextEpisodeButton: Boolean = true,

    // ==================== CONTENT ====================

    /**
     * Enable adult/18+ content.
     *
     * ***Optional***. Defaults to `false`. Parental control flag.
     * When `false`, hide plugins/content marked with [com.dreamstream.core.model.plugin.PluginManifest.isAdult].
     * Show confirmation dialog when enabling.
     */
    val adultContentEnabled: Boolean = false,

    /**
     * Preferred content type filter.
     *
     * ***Optional***. Null for no filter. Filter home/browse to show only
     * this type (e.g., [ContentType.Anime], [ContentType.Movie]).
     */
    val preferredContentType: ContentType? = null,

    /**
     * Hidden provider/plugin IDs.
     *
     * ***Optional***. Defaults to empty list. Plugins in this list are
     * excluded from search results and home sections.
     * Example: `listOf("broken-provider", "unwanted-plugin")`
     */
    val hiddenProviderIds: List<String> = emptyList(),

    // ==================== DOWNLOADS ====================

    /**
     * Custom download path.
     *
     * ***Optional***. Null for app default directory.
     * Custom path for downloaded episodes (external storage).
     * Example: `/storage/emulated/0/Download/DreamStream`
     */
    val downloadPath: String? = null,

    /**
     * Download quality.
     *
     * ***Optional***. Defaults to [Quality.HD]. Quality for downloaded episodes.
     * [Quality.FullHd], [Quality.HD], [Quality.Medium], [Quality.FourK], [Quality.Auto].
     */
    val downloadQuality: Quality = Quality.HD,

    /**
     * Download over Wi-Fi only.
     *
     * ***Optional***. Defaults to `true`. Prevent downloads on cellular data.
     * Show warning if user tries to download on cellular.
     */
    val downloadOverWifiOnly: Boolean = true,

    /**
     * Max concurrent downloads.
     *
     * ***Optional***. Defaults to `3`. Parallel download limit.
     * Range: `1`–`10`.
     */
    val maxConcurrentDownloads: Int = 3,

    // ==================== PLUGIN ====================

    /**
     * Auto-update plugins.
     *
     * ***Optional***. Defaults to `true`. Automatically check and install
     * plugin updates when available.
     */
    val autoUpdatePlugins: Boolean = true,

    /**
     * Show plugin update badge.
     *
     * ***Optional***. Defaults to `true`. Show "Update" badge on plugin
     * cards in plugin store when update available.
     */
    val showPluginUpdateBadge: Boolean = true,

    // ==================== UI ====================

    /**
     * Grid columns in browse screens.
     *
     * ***Optional***. Defaults to `3`. Number of columns in grid view
     * (home, search, browse). Range: `2`–`6`.
     * Example: `2` (large cards), `3` (default), `4` (compact), `6` (dense).
     */
    val gridColumns: Int = 3,

    /**
     * Compact mode.
     *
     * ***Optional***. Defaults to `false`. When `true`, use compact list view
     * with smaller cards and less spacing. Better for dense information.
     */
    val compactMode: Boolean = false,

    /**
     * Show watch progress.
     *
     * ***Optional***. Defaults to `true`. Show progress bars/percentage on
     * watched content in home/browse/bookmarks. Disable for clean UI.
     */
    val showWatchProgress: Boolean = true,

    /**
     * Onboarding completed.
     *
     * ***Optional***. Defaults to `false`.
     * Marks if onboarding is completed.
     */
    val onboardingComplete: Boolean = false,

    /**
     * First launch.
     *
     * ***Optional***. Defaults to `true`.
     * Check if the app is launched for the first time.
     */
    val firstLaunch: Boolean = true,
)
