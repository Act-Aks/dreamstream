package com.dreamstream.feature.settings.data.repository

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.dreamstream.feature.settings.domain.model.AppLanguage
import com.dreamstream.feature.settings.domain.repository.LanguageRepository

/**
 * [LanguageRepository] implementation backed by [AppCompatDelegate].
 *
 * - Works on all API levels supported by AppCompat 1.6+ (API 14+).
 * - On API 33+: delegates to the system [android.app.LocaleManager]
 * - On API 26–32: stores the locale in AppCompat's internal SharedPreferences
 *   and reapplies it on Activity configuration change.
 */
class AppCompatLanguageRepository : LanguageRepository {

    override fun getCurrentLanguage(): AppLanguage {
        val localeList = AppCompatDelegate.getApplicationLocales()
        val tag = if (localeList.isEmpty) "" else localeList.get(0)?.toLanguageTag() ?: ""
        return AppLanguage.fromTag(tag)
    }

    override fun applyLanguage(language: AppLanguage) {
        val localeList = when (language) {
            AppLanguage.SYSTEM -> LocaleListCompat.getEmptyLocaleList()
            else -> LocaleListCompat.forLanguageTags(language.tag)
        }
        AppCompatDelegate.setApplicationLocales(localeList)
    }
}
