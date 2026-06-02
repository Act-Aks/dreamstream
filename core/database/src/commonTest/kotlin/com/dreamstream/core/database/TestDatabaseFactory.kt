package com.dreamstream.core.database

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

object TestDatabaseFactory {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun createTestDatabase() = Room.databaseBuilder<DreamstreamDatabase>(
        name = ":memory:"
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(UnconfinedTestDispatcher())
        .fallbackToDestructiveMigration()
        .build()
}
