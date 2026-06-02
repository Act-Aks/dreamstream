package com.dreamstream.core.database

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.dreamstream.core.data.util.getAppStorageDir
import kotlinx.coroutines.Dispatchers
import java.io.File

fun createDreamstreamDatabase(): DreamstreamDatabase {
    val dbFile = File(getAppStorageDir().toString(), DATABASE_FILE_NAME)

    return Room.databaseBuilder<DreamstreamDatabase>(
        name = dbFile.absolutePath
    )
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .build()
}
