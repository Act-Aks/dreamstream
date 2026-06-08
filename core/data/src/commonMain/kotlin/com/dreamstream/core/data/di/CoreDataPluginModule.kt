package com.dreamstream.core.data.di

import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.core.plugin.loader.PluginClassLoader
import com.dreamstream.core.plugin.loader.PluginContextFactory
import com.dreamstream.core.plugin.loader.PluginInstaller
import com.dreamstream.core.plugin.loader.PluginManager
import com.dreamstream.core.plugin.loader.PluginRegistry
import com.dreamstream.core.plugin.loader.PluginVerifier
import com.dreamstream.plugin.api.plugin.PluginApiVersion
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin module providing the plugin system.
 *
 * The plugin system enables dynamic loading of content providers via:
 * - Plugin verification (signature, API version)
 * - Plugin installation (download, extract, verify)
 * - Plugin class loading (isolated class loaders)
 * - Plugin context creation (HTTP client, file system)
 * - Plugin registry (tracking loaded plugins)
 * - Plugin manager (orchestrating all plugin operations)
 *
 * Provides:
 * - File system abstraction ([getAppFileSystem()])
 * - Plugins directory path ([getPluginsDir()])
 * - [PluginVerifier]: Verifies plugin signatures and API compatibility
 * - [PluginInstaller]: Downloads and installs plugins
 * - [PluginContextFactory]: Creates plugin execution context
 * - [PluginRegistry]: Tracks registered plugins
 * - [PluginClassLoader]: Platform-specific class loader ([expect/actual])
 * - [PluginManager]: Main plugin orchestration interface
 *
 * @see createPluginClassLoader for platform-specific implementation
 */
val coreDataPluginModule: Module = module {

    // Plugin verification
    single<PluginVerifier> {
        PluginVerifier(get<AppStorageProvider>(), get<LoggerFactory>())
    }

    // Plugin installation
    single {
        PluginInstaller(
            httpClient = get(),
            json = get(),
            loggerFactory = get(),
            storageProvider = get(),
            verifier = get(),
        )
    }

    // Plugin context factory
    single<PluginContextFactory> {
        PluginContextFactory(
            apiVersion = PluginApiVersion.CURRENT,
            httpClient = get(),
            loggerFactory = get(),
            storageProvider = get(),
        )
    }

    // Plugin registry
    single<PluginRegistry> { PluginRegistry(get()) }

    // Plugin manager
    single {
        PluginManager(
            pluginClassLoader = get(),
            pluginInstaller = get(),
            pluginRegistry = get(),
            httpClient = get(),
            json = get(),
            pluginContextFactory = get(),
            loggerFactory = get(),
            storageProvider = get(),
        )
    }
}
