package com.dreamstream.core.data.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Main Koin module aggregator for the `:core:data` module.
 *
 * This module aggregates all data layer dependencies including:
 * - Platform-specific bindings (HTTP engine, DataStore, Database)
 * - Logging infrastructure
 * - Preferences/DataStore data source
 * - HTTP client with cookie persistence
 * - Room3 database and DAOs
 * - Data repositories (entity ↔ domain conversion)
 * - Plugin system (loader, installer, manager)
 * - Domain repositories with error handling
 * - Use cases (business logic)
 * - App initializer for startup state loading
 *
 * Usage in your app:
 * ```kotlin
 * startKoin {
 *     // Platform-specific module (Android or Desktop)
 *     androidModule(context) // or desktopModule
 *
 *     // Core data module (aggregates all data modules)
 *     modules(coreDataModule)
 *
 *     // Initialize platform-specific dependencies
 *     initAndroidDependencies(context) // or initDesktopDependencies()
 * }
 *
 * // Initialize app state after Koin is started
 * val appInitializer: AppInitializer = koinApp.resolve()
 * appInitializer.initialize(isDebug = BuildConfig.DEBUG)
 * ```
 *
 * @see coreDataLoggerModule
 * @see platformCoreDataModule
 * @see coreDataPreferencesModule
 * @see coreDataNetworkModule
 * @see coreDataSystemModule
 * @see coreDataDatabaseModule
 * @see coreDataDatabaseRepositoryModule
 * @see coreDataPluginModule
 * @see coreDataRepositoryModule
 */
val coreDataModule: Module = module {

    // Logging infrastructure
    includes(coreDataLoggerModule)

    // Platform-specific: HTTP engine, DataStore, Database, PluginClassLoader
    includes(platformCoreDataModule)

    // Preferences/DataStore data source
    includes(coreDataPreferencesModule)

    // HTTP client with persistent cookies
    includes(coreDataNetworkModule)

    // System-specific: AppStorageProvider, PlatformInfo, TimeProvider, UuidProvider
    includes(coreDataSystemModule)

    // Database and DAOs
    includes(coreDataDatabaseModule)

    // Data repositories (DAO → entity/domain conversion layer)
    includes(coreDataDatabaseRepositoryModule)

    // Plugin
    includes(coreDataPluginModule)

    // Domain repositories with error handling and logging
    includes(coreDataRepositoryModule)
}
