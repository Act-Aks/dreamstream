package com.dreamstream.feature.settings.domain.repository

import com.dreamstream.feature.settings.domain.model.AppLanguage

/**
 * Controls the app's active language at runtime.
 *
 * All operations are synchronous because [AppCompatDelegate.setApplicationLocales]
 * is synchronous — the change triggers an Activity recreation that is handled by
 * the framework, not by this repository.
 */
interface LanguageRepository {

    /** Returns the language currently applied to the app. */
    fun getCurrentLanguage(): AppLanguage

    /**
     * Applies [language] immediately and persists the choice.
     * Triggers Activity recreation to pick up the new locale.
     */
    fun applyLanguage(language: AppLanguage)
}
