package com.dreamstream.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dreamstream.core.data.database.createDataStore
import com.dreamstream.core.data.preferences.DataStoreThemePreferences
import com.dreamstream.core.domain.preferences.ThemePreferences
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformCoreDataModule: Module = module {
    single<HttpClientEngine> { OkHttp.create() }
    single<DataStore<Preferences>> { createDataStore() }
    singleOf(::DataStoreThemePreferences) bind ThemePreferences::class
}
