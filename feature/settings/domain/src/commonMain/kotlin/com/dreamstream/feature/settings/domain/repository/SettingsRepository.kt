package com.dreamstream.feature.settings.domain.repository

import com.dreamstream.feature.settings.domain.model.AppLanguage

/**
 * Feature-level contract for app settings.
 *
 * This repository exposes user-facing settings that affect app behavior,
 * including locale, theme, and other persistent preferences.
 *
 * Platform implementations are responsible for applying values with the
 * appropriate native APIs.
 */
interface SettingsRepository {

    /**
     * Returns the language currently applied to the app.
     *
     * This reflects the effective app locale, not necessarily the device locale.
     */
    fun getCurrentLanguage(): AppLanguage

    /**
     * Applies the selected [language] to the app.
     *
     * Implementations may persist the choice and trigger any required UI
     * refresh or configuration change.
     */
    fun applyLanguage(language: AppLanguage)

    /**
     * Returns whether dark mode is enabled for the app.
     */
    fun isDarkModeEnabled(): Boolean

    /**
     * Enables or disables dark mode for the app.
     */
    fun setDarkModeEnabled(enabled: Boolean)

    /**
     * Returns whether notifications are enabled in the app settings.
     */
    fun isNotificationsEnabled(): Boolean

    /**
     * Enables or disables notifications in the app settings.
     */
    fun setNotificationsEnabled(enabled: Boolean)
}
