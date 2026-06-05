package com.dreamstream.core.data.di

import com.dreamstream.core.data.preferences.UserPreferencesDataSource
import com.dreamstream.core.data.preferences.UserPreferencesDataSourceImpl
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

val coreDataPreferencesModule: Module = module {
    single { Json { ignoreUnknownKeys = true } }

    // DataStore<Preferences> will be provided in platformCoreDataModule
    single<UserPreferencesDataSource> { UserPreferencesDataSourceImpl(get(), get(), get()) }
}
