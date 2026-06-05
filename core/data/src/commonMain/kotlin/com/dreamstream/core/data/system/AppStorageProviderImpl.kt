package com.dreamstream.core.data.system

import com.dreamstream.core.data.util.getAppFileSystem
import com.dreamstream.core.data.util.getAppStorageDir
import com.dreamstream.core.data.util.getCacheDir
import com.dreamstream.core.data.util.getPluginsDir
import com.dreamstream.core.domain.system.AppStorageProvider
import okio.FileSystem
import okio.Path

class AppStorageProviderImpl : AppStorageProvider {
    override val fileSystem: FileSystem = getAppFileSystem()
    override val appStorageDir: Path = getAppStorageDir()
    override val cacheDir: Path = getCacheDir()
    override val pluginsDir: Path = getPluginsDir()
}
