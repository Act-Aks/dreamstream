package com.dreamstream.feature.settings.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.dreamstream.feature.settings.domain.model.AppLanguage
import com.dreamstream.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val KEY_DARK_MODE     = booleanPreferencesKey("dark_mode")
private val KEY_NOTIFICATIONS = booleanPreferencesKey("notifications")

/**
 * Android implementation of [SettingsRepository].
 *
 * - **Language** — applied and persisted by AppCompat's per-app language API
 *   ([AppCompatDelegate.setApplicationLocales]). Language changes cause a config
 *   change that Compose Navigation handles automatically. Exposed as a
 *   [MutableStateFlow] updated on each write so the UI flow never reads from disk.
 *
 * - **Dark mode** — persisted in [DataStore] and applied immediately on the main
 *   thread via [AppCompatDelegate.setDefaultNightMode].
 *
 * - **Notifications** — persisted in [DataStore] only (no OS-level side effect here;
 *   integrate with NotificationManagerCompat when needed).
 */
class AndroidSettingsRepository(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    // ── Language ───────────────────────────────────────────────────────────────

    private val _language = MutableStateFlow(readCurrentLanguage())

    override fun languageFlow(): Flow<AppLanguage> = _language

    override suspend fun applyLanguage(language: AppLanguage) {
        val localeList = when (language) {
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            else               -> LocaleListCompat.forLanguageTags(language.tag)
        }
        withContext(Dispatchers.Main) {
            AppCompatDelegate.setApplicationLocales(localeList)
        }
        _language.value = language
    }

    // ── Dark mode ──────────────────────────────────────────────────────────────

    override fun darkModeFlow(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[KEY_DARK_MODE] ?: false }

    override suspend fun setDarkModeEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_DARK_MODE] = enabled }
        withContext(Dispatchers.Main) {
            AppCompatDelegate.setDefaultNightMode(
                if (enabled) AppCompatDelegate.MODE_NIGHT_YES
                else         AppCompatDelegate.MODE_NIGHT_NO,
            )
        }
    }

    // ── Notifications ──────────────────────────────────────────────────────────

    override fun notificationsFlow(): Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[KEY_NOTIFICATIONS] ?: true }

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_NOTIFICATIONS] = enabled }
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private fun readCurrentLanguage(): AppLanguage {
        val localeList = AppCompatDelegate.getApplicationLocales()
        val tag = if (localeList.isEmpty) "" else localeList.get(0)?.toLanguageTag() ?: ""
        return AppLanguage.fromTag(tag)
    }
}
