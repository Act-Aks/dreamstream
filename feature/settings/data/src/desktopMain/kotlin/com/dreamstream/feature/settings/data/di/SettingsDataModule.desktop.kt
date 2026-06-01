package com.dreamstream.feature.settings.data.di

import com.dreamstream.feature.settings.data.repository.DesktopSettingsRepository
import com.dreamstream.feature.settings.domain.repository.SettingsRepository
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Desktop Koin module for settings.
 */
actual val settingsDataModule: Module = module {
    single<SettingsRepository> { DesktopSettingsRepository() }
}
