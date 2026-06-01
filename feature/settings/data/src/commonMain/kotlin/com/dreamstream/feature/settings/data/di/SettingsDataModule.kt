package com.dreamstream.feature.settings.data.di

import org.koin.core.module.Module

/**
 * Returns the platform-specific module that binds [com.dreamstream.feature.settings.domain.repository.SettingsRepository].
 *
 * Each target provides its own implementation because locale and theme APIs
 * differ across platforms.
 */
expect val settingsDataModule : Module
