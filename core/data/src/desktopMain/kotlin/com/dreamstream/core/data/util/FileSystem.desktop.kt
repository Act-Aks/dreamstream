package com.dreamstream.core.data.util

import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

actual fun getAppFileSystem(): FileSystem = FileSystem.SYSTEM

actual fun getAppStorageDir(): Path {
    val userHome = System.getProperty("user.home") ?: "."
    val os = System.getProperty("os.name")?.lowercase() ?: ""
    val base = when {
        os.contains("win") -> "${System.getenv("APPDATA")}/DreamStream"
        os.contains("mac") -> "$userHome/Library/Application Support/DreamStream"
        else -> "$userHome/.config/dreamstream"
    }
    return base.toPath().also { FileSystem.SYSTEM.createDirectories(it) }
}

actual fun getCacheDir(): Path {
    val userHome = System.getProperty("user.home") ?: "."
    val os = System.getProperty("os.name")?.lowercase() ?: ""
    val base = when {
        os.contains("win") -> "${System.getenv("LOCALAPPDATA")}/DreamStream/Cache"
        os.contains("mac") -> "$userHome/Library/Caches/DreamStream"
        else -> "$userHome/.cache/dreamstream"
    }
    return base.toPath().also { FileSystem.SYSTEM.createDirectories(it) }
}

actual fun getPluginsDir(): Path =
    (getAppStorageDir().toString() + "/plugins").toPath().also {
        FileSystem.SYSTEM.createDirectories(it)
    }
