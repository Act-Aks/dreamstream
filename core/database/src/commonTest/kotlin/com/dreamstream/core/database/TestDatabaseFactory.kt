package com.dreamstream.core.database

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import java.io.File

object TestDatabaseHolder {
    @Volatile
    private var sharedDatabase: DreamstreamDatabase? = null

    @OptIn(ExperimentalCoroutinesApi::class)
    fun getSharedDatabase(): DreamstreamDatabase {
        return sharedDatabase ?: synchronized(this) {
            sharedDatabase ?: createDatabase().also { sharedDatabase = it }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun createDatabase(): DreamstreamDatabase {
        val tempFile = File.createTempFile("test-db-shared", ".db").apply {
            deleteOnExit()
        }

        return Room.databaseBuilder<DreamstreamDatabase>(
            name = tempFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(UnconfinedTestDispatcher())
            .fallbackToDestructiveMigration()
            .build()
    }

    fun closeSharedDatabase() {
        sharedDatabase?.close()
        sharedDatabase = null
    }
}
