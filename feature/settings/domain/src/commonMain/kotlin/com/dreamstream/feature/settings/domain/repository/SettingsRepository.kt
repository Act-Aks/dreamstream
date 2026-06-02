package com.dreamstream.feature.settings.domain.repository

import com.dreamstream.feature.settings.domain.model.AppLanguage
import kotlinx.coroutines.flow.Flow

/**
 * Feature-level contract for app settings.
 *
 * All read operations are [Flow]-based so the UI can react to settings changes
 * without polling. Write operations are `suspend` so callers can sequence them
 * with other coroutine work.
 *
 * Platform implementations are responsible for persisting values and applying
 * them with the appropriate native APIs (AppCompat, DataStore, etc.).
 */
interface SettingsRepository {

    /**
     * Emits the language currently applied to the app, and any subsequent changes.
     */
    fun languageFlow(): Flow<AppLanguage>

    /**
     * Applies [language] to the app and persists the choice.
     */
    suspend fun applyLanguage(language: AppLanguage)

    /**
     * Emits whether dark mode is enabled, and any subsequent changes.
     */
    fun darkModeFlow(): Flow<Boolean>

    /**
     * Enables or disables dark mode and persists the choice.
     */
    suspend fun setDarkModeEnabled(enabled: Boolean)

    /**
     * Emits whether notifications are enabled, and any subsequent changes.
     */
    fun notificationsFlow(): Flow<Boolean>

    /**
     * Enables or disables notifications and persists the choice.
     */
    suspend fun setNotificationsEnabled(enabled: Boolean)
}
