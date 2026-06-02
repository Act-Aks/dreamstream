package com.dreamstream.core.database

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.Dispatchers

fun createDreamstreamDatabase(context: Context): DreamstreamDatabase {
    val appContext = context.applicationContext

    return Room.databaseBuilder<DreamstreamDatabase>(
        name = appContext.getDatabasePath(DATABASE_FILE_NAME).absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
