package com.dreamstream.core.data.database

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

internal fun createDataStore(producePath: () -> String): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath {
        producePath().toPath()
    }
}

internal const val DATA_STORE_FILE_NAME = "dreamstream_prefs.preferences_pb"
