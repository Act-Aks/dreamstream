package com.dreamstream.core.database

import androidx.room3.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.dreamstream.core.domain.system.AppStorageProvider
import kotlinx.coroutines.Dispatchers
import java.io.File

fun createDreamstreamDatabase(
    storageProvider: AppStorageProvider
): DreamstreamDatabase {
    val dbFile = File(storageProvider.appStorageDir.toString(), DATABASE_FILE_NAME)

    return Room.databaseBuilder<DreamstreamDatabase>(
        name = dbFile.absolutePath
    ).setDriver(BundledSQLiteDriver()).setQueryCoroutineContext(Dispatchers.IO).build()
}
