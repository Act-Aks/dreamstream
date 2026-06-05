package com.dreamstream.core.data.di

import com.dreamstream.core.data.logging.KermitDreamLogger
import com.dreamstream.core.data.logging.KermitDreamLoggerFactory
import com.dreamstream.core.data.network.DreamHttpClientFactory
import com.dreamstream.core.domain.logger.DreamLogger
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Koin module providing the shared [HttpClient] singleton and logging.
 *
 * The [HttpClientEngine] is resolved from [platformCoreDataModule], which
 * is an `actual` declaration in each source set:
 * - `androidMain` → OkHttp engine (IPv4 preference, HTTP/1.1, Chrome TLS)
 * - `desktopMain` → Java engine
 *
 * Include both modules in `:app`:
 * ```
 * startKoin { modules(networkModule, platformCoreDataModule, ...) }
 * ```
 */
val coreDataModule: Module = module {
    includes(platformCoreDataModule)
    single<DreamLogger> { KermitDreamLogger.Default }
    singleOf(::KermitDreamLoggerFactory)
    includes(coreDataPreferencesModule)
    singleOf(::DreamHttpClientFactory)
    single<HttpClient> { get<DreamHttpClientFactory>().create(get()) }
}

/**
 * Platform-specific Koin module that provides an [io.ktor.client.engine.HttpClientEngine]
 * and any other platform-bound singletons (e.g. DataStore on Android).
 *
 * Defined as `actual val` in `androidMain` and `desktopMain`.
 */
expect val platformCoreDataModule: Module
