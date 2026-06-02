package com.dreamstream.core.data.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.dreamstream.core.data.util.appDataDirectory
import java.io.File

internal fun createDataStore(): DataStore<Preferences> = createDataStore {
    val directory = appDataDirectory

    if (!directory.exists()) {
        directory.mkdirs()
    }

    File(directory, DATA_STORE_FILE_NAME).absolutePath
}
