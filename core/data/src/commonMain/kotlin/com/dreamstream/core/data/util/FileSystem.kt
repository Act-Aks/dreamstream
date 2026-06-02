package com.dreamstream.core.data.util

import okio.FileSystem
import okio.Path

expect fun getAppFileSystem(): FileSystem

expect fun getAppStorageDir(): Path

expect fun getCacheDir(): Path

expect fun getPluginsDir(): Path
