package com.dreamstream.core.domain.system

import okio.FileSystem
import okio.Path

interface AppStorageProvider {
    val fileSystem: FileSystem
    val appStorageDir: Path
    val cacheDir: Path
    val pluginsDir: Path
}
