package com.dreamstream.core.data.util

import android.content.Context
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

private lateinit var appContext: Context

fun initPlatformFileSystem(context: Context) {
    appContext = context.applicationContext
}

actual fun getAppFileSystem(): FileSystem = FileSystem.SYSTEM

actual fun getAppStorageDir(): Path =
    appContext.filesDir.absolutePath.toPath()

actual fun getCacheDir(): Path =
    appContext.cacheDir.absolutePath.toPath()

actual fun getPluginsDir(): Path =
    (appContext.filesDir.absolutePath + "/plugins").toPath().also {
        getAppFileSystem().createDirectories(it)
    }
