package com.dreamstream.core.data.di

import com.dreamstream.core.database.DreamstreamDatabase
import com.dreamstream.core.database.dao.BookmarkDao
import com.dreamstream.core.database.dao.PluginDao
import com.dreamstream.core.database.dao.RepositoryDao
import com.dreamstream.core.database.dao.WatchHistoryDao
import org.koin.dsl.module

/**
 * Koin module providing Room3 database and DAOs.
 *
 * Provides:
 * - [DreamstreamDatabase]: Room database instance (from [platformCoreDataModule])
 * - [WatchHistoryDao]: Watch history operations
 * - [BookmarkDao]: Bookmark operations
 * - [PluginDao]: Installed plugin operations
 * - [RepositoryDao]: Plugin repository operations
 *
 * DAOs are automatically configured for coroutine execution via:
 * `.setQueryCoroutineContext(Dispatchers.IO)` in [createDreamstreamDatabase].
 *
 * The database is created by platform-specific functions:
 * - Android: [createDreamstreamDatabase(Context)]
 * - Desktop: [createDreamstreamDatabase(AppStorageProvider)]
 */
val coreDataDatabaseModule = module {
    single<WatchHistoryDao> { get<DreamstreamDatabase>().watchHistoryDao() }
    single<BookmarkDao> { get<DreamstreamDatabase>().bookmarkDao() }
    single<PluginDao> { get<DreamstreamDatabase>().pluginDao() }
    single<RepositoryDao> { get<DreamstreamDatabase>().repositoryDao() }
}
