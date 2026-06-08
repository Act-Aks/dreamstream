package com.dreamstream.core.data.di


import androidx.datastore.core.DataStore
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.module.Module

/**
 * Platform-specific Koin module providing platform-bound dependencies.
 *
 * This `expect` declaration is implemented separately for each platform:
 *
 * - **Android** (`androidMain`):
 *   - [HttpClientEngine]: OkHttp engine with IPv4 preference, HTTP/1.1, Chrome TLS config
 *   - [DataStore]: Android [Context]-based Preferences DataStore
 *   - [com.dreamstream.core.database.DreamstreamDatabase]: Room database using Android application context
 *
 * - **Desktop** (`desktopMain`):
 *   - [HttpClientEngine]: Java HTTP engine
 *   - [DataStore]: File-based Preferences DataStore using [com.dreamstream.core.domain.system.AppStorageProvider]
 *   - [com.dreamstream.core.database.DreamstreamDatabase]: Room database using desktop file system
 *
 * Include this module in [coreDataModule] via `includes(platformCoreDataModule)`.
 */
expect val platformCoreDataModule: Module
