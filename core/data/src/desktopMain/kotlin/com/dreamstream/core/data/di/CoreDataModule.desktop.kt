package com.dreamstream.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dreamstream.core.data.database.createDataStore
import com.dreamstream.core.database.DreamstreamDatabase
import com.dreamstream.core.database.createDreamstreamDatabase
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.core.plugin.loader.PluginClassLoader
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Desktop-specific implementation of [platformCoreDataModule].
 *
 * Provides:
 * - [HttpClientEngine]: Java HTTP engine for desktop
 *
 * - [DataStore]: File-based Preferences DataStore using [com.dreamstream.core.domain.system.AppStorageProvider]
 *
 * - [DreamstreamDatabase]: Room database created at:
 *   `File(storageProvider.appStorageDir, "dreamstream_db")`
 *   with BundledSQLiteDriver and Dispatchers.IO query context.
 *
 * @note [com.dreamstream.core.domain.system.AppStorageProvider] must be provided in [desktopModule] or before starting Koin.
 */
actual val platformCoreDataModule: Module = module {

    // ── HTTP engine ────────────────────────────────────────────────────────────
    single<HttpClientEngine> { OkHttp.create() }

    // ── DataStore ──────────────────────────────────────────────────────────────
    single<DataStore<Preferences>> { createDataStore() }

    // ── Database ───────────────────────────────────────────────────────────────
    single<DreamstreamDatabase> {
        createDreamstreamDatabase(get<AppStorageProvider>())
    }

    // ── Plugin class loader ────────────────────────────────────────────────────
    single<PluginClassLoader> { PluginClassLoader() }
}
