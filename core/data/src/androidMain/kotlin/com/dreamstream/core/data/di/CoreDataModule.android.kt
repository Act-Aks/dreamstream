package com.dreamstream.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dreamstream.core.data.database.createDataStore
import com.dreamstream.core.database.DreamstreamDatabase
import com.dreamstream.core.database.createDreamstreamDatabase
import com.dreamstream.core.plugin.loader.PluginClassLoader
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Android-specific implementation of [platformCoreDataModule].
 *
 * Provides:
 * - [HttpClientEngine]: OkHttp engine configured for Android with:
 *   - IPv4 preference
 *   - HTTP/1.1 support
 *   - Chrome-compatible TLS configuration
 *   - Persistent cookie storage
 *
 * - [DataStore]: Android Preferences DataStore using `androidContext()`
 *
 * - [DreamstreamDatabase]: Room database created at:
 *   `context.getDatabasePath("dreamstream_db").absolutePath`
 *   with BundledSQLiteDriver and Dispatchers.IO query context.
 */
actual val platformCoreDataModule: Module = module {

    // ── HTTP engine ────────────────────────────────────────────────────────────
    single<HttpClientEngine> { OkHttp.create() }

    // ── DataStore ──────────────────────────────────────────────────────────────
    single<DataStore<Preferences>> {
        createDataStore(androidContext())
    }

    // ── Database ───────────────────────────────────────────────────────────────
    single<DreamstreamDatabase> {
        createDreamstreamDatabase(androidContext())
    }

    // ── PluginClassLoader ────────────────────────────────────────────────────
    single<PluginClassLoader> {
        PluginClassLoader(androidContext())
    }
}
