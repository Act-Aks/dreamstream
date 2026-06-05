package com.dreamstream.core.database

import androidx.room3.ConstructedBy
import androidx.room3.Database
import androidx.room3.RoomDatabase
import androidx.room3.RoomDatabaseConstructor
import androidx.room3.TypeConverters
import com.dreamstream.core.database.adapters.ContentTypeConverter
import com.dreamstream.core.database.adapters.StringListConverter
import com.dreamstream.core.database.dao.BookmarkDao
import com.dreamstream.core.database.dao.PluginDao
import com.dreamstream.core.database.dao.RepositoryDao
import com.dreamstream.core.database.dao.WatchHistoryDao
import com.dreamstream.core.database.entity.BookmarkEntity
import com.dreamstream.core.database.entity.InstalledPluginEntity
import com.dreamstream.core.database.entity.RepositoryEntity
import com.dreamstream.core.database.entity.WatchHistoryEntity

@Database(
    entities = [
        WatchHistoryEntity::class,
        BookmarkEntity::class,
        InstalledPluginEntity::class,
        RepositoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(
    ContentTypeConverter::class,
    StringListConverter::class
)
@ConstructedBy(DreamstreamDatabaseConstructor::class)
abstract class DreamstreamDatabase : RoomDatabase() {
    abstract fun watchHistoryDao(): WatchHistoryDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun pluginDao(): PluginDao
    abstract fun repositoryDao(): RepositoryDao
}

expect object DreamstreamDatabaseConstructor : RoomDatabaseConstructor<DreamstreamDatabase> {
    override fun initialize(): DreamstreamDatabase
}

internal const val DATABASE_FILE_NAME = "dreamstream_db"
