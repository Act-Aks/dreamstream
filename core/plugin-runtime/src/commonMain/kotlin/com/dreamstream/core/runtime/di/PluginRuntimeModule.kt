package com.dreamstream.core.runtime.di

import com.dreamstream.core.runtime.context.PluginContextImpl
import com.dreamstream.core.runtime.loader.PluginLoader
import com.dreamstream.core.runtime.registry.PluginRegistry
import io.ktor.client.HttpClient
import org.koin.dsl.module

/**
 * Koin module for the plugin runtime.
 *
 * Requires [HttpClient] to be provided by `networkModule` (declared in `:core:data`).
 *
 * @param loaders All [PluginLoader] instances to use during startup.
 *   - Pass [com.dreamstream.core.runtime.loader.BundledPluginLoader] for Gradle-module plugins.
 *   - On Android, also pass `ApkPluginLoader(filesDir.resolve("plugins"), context)` for APK plugins.
 */
fun pluginRuntimeModule(loaders: List<PluginLoader>) = module {
    single<PluginContextImpl> {
        PluginContextImpl(httpClient = get<HttpClient>())
    }
    single<PluginRegistry> {
        PluginRegistry(loaders = loaders, context = get<PluginContextImpl>())
    }
}
