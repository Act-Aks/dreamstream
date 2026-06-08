package com.dreamstream.core.data.di

import com.dreamstream.core.database.repository.BookmarkRepository
import com.dreamstream.core.database.repository.PluginRepository
import com.dreamstream.core.database.repository.RepositoryRepository
import com.dreamstream.core.database.repository.WatchHistoryRepository
import org.koin.dsl.module

/**
 * Koin module providing data repositories (entity ↔ domain conversion layer).
 *
 * These repositories sit between DAOs and domain repositories, handling:
 * - Entity to domain model conversion ([toDomain()])
 * - Domain to entity conversion ([toEntity()])
 * - Flow mapping for observable data
 *
 * Provides:
 * - [BookmarkRepository]: Bookmark entity ↔ domain conversion
 * - [PluginRepository]: Installed plugin entity ↔ domain conversion
 * - [RepositoryRepository]: Plugin repository entity ↔ domain conversion
 * - [WatchHistoryRepository]: Watch history entity ↔ domain conversion
 *
 * @see coreDataRepositoryModule for domain repositories with error handling
 */
val coreDataDatabaseRepositoryModule = module {
    single<BookmarkRepository> { BookmarkRepository(get()) }
    single<PluginRepository> { PluginRepository(get()) }
    single<RepositoryRepository> { RepositoryRepository(get()) }
    single<WatchHistoryRepository> { WatchHistoryRepository(get()) }
}
