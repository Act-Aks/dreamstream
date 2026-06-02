package com.dreamstream.feature.settings.data.repository

import com.dreamstream.feature.settings.domain.model.AppLanguage
import com.dreamstream.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Locale

/**
 * Desktop implementation of [SettingsRepository].
 *
 * All settings are stored in-memory as [MutableStateFlow]s. There is no file-system
 * persistence on desktop yet — values reset when the process exits.
 *
 * Language changes update the JVM default [Locale] so the app formats dates,
 * numbers, and resources consistently.
 */
class DesktopSettingsRepository : SettingsRepository {

    private val _language      = MutableStateFlow(AppLanguage.fromTag(Locale.getDefault().toLanguageTag()))
    private val _darkMode      = MutableStateFlow(false)
    private val _notifications = MutableStateFlow(true)

    // ── Language ───────────────────────────────────────────────────────────────

    override fun languageFlow(): Flow<AppLanguage> = _language

    override suspend fun applyLanguage(language: AppLanguage) {
        val locale = when (language) {
            AppLanguage.SYSTEM -> Locale.getDefault()
            else               -> Locale.forLanguageTag(language.tag)
        }
        Locale.setDefault(locale)
        _language.value = language
    }

    // ── Dark mode ──────────────────────────────────────────────────────────────

    override fun darkModeFlow(): Flow<Boolean> = _darkMode

    override suspend fun setDarkModeEnabled(enabled: Boolean) {
        _darkMode.value = enabled
    }

    // ── Notifications ──────────────────────────────────────────────────────────

    override fun notificationsFlow(): Flow<Boolean> = _notifications

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        _notifications.value = enabled
    }
}
