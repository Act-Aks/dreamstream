package com.dreamstream.feature.settings.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dreamstream.feature.settings.data.repository.AndroidSettingsRepository
import com.dreamstream.feature.settings.domain.repository.SettingsRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android Koin module for settings data.
 *
 * [DataStore]<[Preferences]> is resolved from [platformCoreDataModule] in `:core:data`,
 * which registers it as a singleton bound to the Android application context.
 */
actual val settingsDataModule: Module = module {
    single<SettingsRepository> { AndroidSettingsRepository(dataStore = get()) }
}
