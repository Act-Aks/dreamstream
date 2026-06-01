package com.dreamstream.feature.settings.data.repository

import com.dreamstream.feature.settings.domain.model.AppLanguage
import com.dreamstream.feature.settings.domain.repository.SettingsRepository
import java.util.Locale

/**
 * Desktop implementation of [SettingsRepository].
 *
 * Locale changes update the JVM default locale so the app can render resources
 * and format dates, numbers, and text consistently on desktop.
 */
class DesktopSettingsRepository : SettingsRepository {

    /**
     * Returns the language currently applied to the desktop app.
     */
    override fun getCurrentLanguage(): AppLanguage {
        return AppLanguage.fromTag(Locale.getDefault().toLanguageTag())
    }

    /**
     * Applies the given [language] to the desktop app.
     */
    override fun applyLanguage(language: AppLanguage) {
        val locale = when (language) {
            AppLanguage.SYSTEM -> Locale.getDefault()
            else -> Locale.forLanguageTag(language.tag)
        }
        Locale.setDefault(locale)
    }

    /**
     * Returns whether dark mode is enabled.
     *
     * Replace with your own desktop theme state if you persist this setting.
     */
    override fun isDarkModeEnabled(): Boolean = false

    /**
     * Enables or disables dark mode for the desktop app.
     *
     * Replace with persistence or theme-state updates if needed.
     */
    override fun setDarkModeEnabled(enabled: Boolean) {
        // Store in desktop settings state if needed.
    }

    /**
     * Returns whether notifications are enabled.
     */
    override fun isNotificationsEnabled(): Boolean = true

    /**
     * Enables or disables notifications in desktop settings.
     */
    override fun setNotificationsEnabled(enabled: Boolean) {
        // Persist in desktop settings state if needed.
    }
}
