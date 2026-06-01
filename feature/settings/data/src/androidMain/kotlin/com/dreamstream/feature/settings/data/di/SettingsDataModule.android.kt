package com.dreamstream.feature.settings.data.di

import com.dreamstream.feature.settings.data.repository.AndroidSettingsRepository
import com.dreamstream.feature.settings.domain.repository.SettingsRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android Koin module for settings.
 */
actual val settingsDataModule: Module = module {
    single<SettingsRepository> { AndroidSettingsRepository() }
}
