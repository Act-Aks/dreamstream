package com.dreamstream.core.data.di

import com.dreamstream.core.data.preferences.UserPreferencesDataSource
import com.dreamstream.core.data.preferences.UserPreferencesDataSourceImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module providing preferences/DataStore data source.
 *
 * Provides:
 * - [Json] instance with `ignoreUnknownKeys = true` for flexible deserialization
 * - [UserPreferencesDataSource] for reading/writing user preferences
 *
 * The [DataStore] is provided by [platformCoreDataModule] (platform-specific).
 */
val coreDataPreferencesModule: Module = module {
    single { Json { ignoreUnknownKeys = true } }

    // DataStore<Preferences> will be provided in platformCoreDataModule
    single<UserPreferencesDataSource> { UserPreferencesDataSourceImpl(get(), get(), get()) }
}
