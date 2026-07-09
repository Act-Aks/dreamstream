package com.dreamstream.core.data.system

import com.dreamstream.core.data.util.getAppFileSystem
import com.dreamstream.core.data.util.getAppStorageDir
import com.dreamstream.core.data.util.getCacheDir
import com.dreamstream.core.data.util.getPluginsDir
import com.dreamstream.core.domain.system.AppStorageProvider
import okio.FileSystem
import okio.Path

class AppStorageProviderImpl : AppStorageProvider {
    override val fileSystem: FileSystem by lazy { getAppFileSystem() }
    override val appStorageDir: Path by lazy { getAppStorageDir() }
    override val cacheDir: Path by lazy { getCacheDir() }
    override val pluginsDir: Path by lazy { getPluginsDir() }
}
