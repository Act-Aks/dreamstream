package com.dreamstream.core.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * DataStore keys for persisting [com.dreamstream.core.domain.model.user.UserPreferences] fields.
 *
 * These keys are internal to the data layer and map 1:1 to properties
 * of [com.dreamstream.core.domain.model.user.UserPreferences].
 */
internal object UserPreferencesKeys {

    // General
    val LANGUAGE = stringPreferencesKey("language")
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color")
    val ACCENT_COLOR = longPreferencesKey("accent_color")

    // Player
    val DEFAULT_QUALITY = stringPreferencesKey("default_quality")
    val AUTO_PLAY = booleanPreferencesKey("auto_play")
    val SKIP_INTRO = booleanPreferencesKey("skip_intro")
    val DEFAULT_SUBTITLE_LANGUAGE = stringPreferencesKey("default_subtitle_language")
    val PLAYER_GESTURES = booleanPreferencesKey("player_gestures")
    val BACKGROUND_PLAYBACK = booleanPreferencesKey("background_playback")
    val PIP_ENABLED = booleanPreferencesKey("pip_enabled")
    val DEFAULT_PLAYBACK_SPEED = floatPreferencesKey("default_playback_speed")
    val SHOW_NEXT_EPISODE_BUTTON = booleanPreferencesKey("show_next_episode_button")

    // Content
    val ADULT_CONTENT_ENABLED = booleanPreferencesKey("adult_content_enabled")
    val PREFERRED_CONTENT_TYPE = stringPreferencesKey("preferred_content_type")
    val HIDDEN_PROVIDER_IDS = stringPreferencesKey("hidden_provider_ids")

    // Downloads
    val DOWNLOAD_PATH = stringPreferencesKey("download_path")
    val DOWNLOAD_QUALITY = stringPreferencesKey("download_quality")
    val DOWNLOAD_OVER_WIFI_ONLY = booleanPreferencesKey("download_over_wifi_only")
    val MAX_CONCURRENT_DOWNLOADS = intPreferencesKey("max_concurrent_downloads")

    // Plugin
    val AUTO_UPDATE_PLUGINS = booleanPreferencesKey("auto_update_plugins")
    val SHOW_PLUGIN_UPDATE_BADGE = booleanPreferencesKey("show_plugin_update_badge")

    // UI
    val GRID_COLUMNS = intPreferencesKey("grid_columns")
    val COMPACT_MODE = booleanPreferencesKey("compact_mode")
    val SHOW_WATCH_PROGRESS = booleanPreferencesKey("show_watch_progress")

    // Onboarding (not currently in UserPreferences, but available if needed)
    val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    val FIRST_LAUNCH = booleanPreferencesKey("first_launch")
}
