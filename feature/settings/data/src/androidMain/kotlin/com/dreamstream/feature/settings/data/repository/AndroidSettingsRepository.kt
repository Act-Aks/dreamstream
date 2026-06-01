package com.dreamstream.feature.settings.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.dreamstream.feature.settings.domain.model.AppLanguage
import com.dreamstream.feature.settings.domain.repository.SettingsRepository

/**
 * Android implementation of [SettingsRepository].
 *
 * Locale is applied using AppCompat's per-app language API, which persists the
 * selection and lets the framework handle the resulting configuration change.
 * Theme is applied using AppCompat's default night mode API.
 */
class AndroidSettingsRepository : SettingsRepository {

    /**
     * Returns the language currently applied to the app on Android.
     */
    override fun getCurrentLanguage(): AppLanguage {
        val localeList = AppCompatDelegate.getApplicationLocales()
        val tag = if (localeList.isEmpty) "" else localeList.get(0)?.toLanguageTag() ?: ""
        return AppLanguage.fromTag(tag)
    }

    /**
     * Applies the given [language] to the app on Android.
     */
    override fun applyLanguage(language: AppLanguage) {
        val localeList = when (language) {
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            else -> LocaleListCompat.forLanguageTags(language.tag)
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    /**
     * Returns whether dark mode is currently enabled.
     */
    override fun isDarkModeEnabled(): Boolean {
        return AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    /**
     * Enables or disables dark mode for the app.
     */
    override fun setDarkModeEnabled(enabled: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }

    /**
     * Returns whether notifications are enabled.
     *
     * Replace the placeholder implementation with stored user preference if needed.
     */
    override fun isNotificationsEnabled(): Boolean = true

    /**
     * Enables or disables notifications in Android settings.
     *
     * Replace the placeholder implementation with persistence and any platform
     * notification integration you need.
     */
    override fun setNotificationsEnabled(enabled: Boolean) {
        // Persist and/or integrate with Android notification settings here.
    }
}
