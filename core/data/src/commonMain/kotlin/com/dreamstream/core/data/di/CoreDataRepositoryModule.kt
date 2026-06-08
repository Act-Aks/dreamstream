package com.dreamstream.core.data.di

import com.dreamstream.core.data.repository.BookmarkRepositoryImpl
import com.dreamstream.core.data.repository.ContentRepositoryImpl
import com.dreamstream.core.data.repository.PluginRepositoryImpl
import com.dreamstream.core.data.repository.PreferencesRepositoryImpl
import com.dreamstream.core.data.repository.WatchHistoryRepositoryImpl
import com.dreamstream.core.domain.repository.BookmarkRepository
import com.dreamstream.core.domain.repository.ContentRepository
import com.dreamstream.core.domain.repository.PluginRepository
import com.dreamstream.core.domain.repository.PreferencesRepository
import com.dreamstream.core.domain.repository.WatchHistoryRepository
import org.koin.dsl.module

/**
 * Koin module providing domain repositories with error handling and logging.
 *
 * These repositories sit at the domain layer boundary, providing:
 * - Error handling via [com.dreamstream.core.domain.util.Result] type
 * - Logging via [com.dreamstream.core.data.logging.KermitDreamLoggerFactory]
 * - Domain-specific business rules validation
 * - Clean API for use cases
 *
 * Provides:
 * - [BookmarkRepository]: Bookmark CRUD operations with error handling
 * - [WatchHistoryRepository]: Watch history tracking with progress updates
 * - [PluginRepository]: Plugin management (enable/disable, version updates)
 * - [PreferencesRepository]: User preferences with type-safe access
 * - [ContentRepository]: Content search, catalog, streaming (via plugins)
 *
 * @see coreDataDatabaseRepositoryModule for data repositories (entity conversion)
 */
val coreDataRepositoryModule = module {
    single<BookmarkRepository> {
        BookmarkRepositoryImpl(
            loggerFactory = get(),
            repository = get(),
        )
    }
    single<WatchHistoryRepository> {
        WatchHistoryRepositoryImpl(
            repository = get(),
            timeProvider = get(),
            loggerFactory = get(),
        )
    }
    single<PluginRepository> {
        PluginRepositoryImpl(
            pluginManager = get(),
            pluginRepository = get(),
            repositoryRepository = get(),
            storageProvider = get(),
            timeProvider = get(),
            loggerFactory = get(),
        )
    }
    single<PreferencesRepository> {
        PreferencesRepositoryImpl(
            preferencesDataSource = get(),
        )
    }
    single<ContentRepository> {
        ContentRepositoryImpl(
            pluginManager = get(),
            loggerFactory = get()
        )
    }
}
