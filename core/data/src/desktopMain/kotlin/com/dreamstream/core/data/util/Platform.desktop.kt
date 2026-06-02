package com.dreamstream.core.data.util

import java.util.UUID

actual object Platform {
    actual val name: String
        get() = "Desktop"
    actual val isDebug: Boolean
        get() = System.getProperty("dreamstream.debug") == "true"
    actual val osVersion: String
        get() = "${System.getProperty("os.name")} ${System.getProperty("os.version")}"
    actual val appVersion: String
        get() = System.getProperty("dreamstream.version") ?: "unknown"
    actual val appVersionCode: Int
        get() = System.getProperty("dreamstream.versionCode")?.toIntOrNull() ?: 0
}

actual fun currentTimeMillis(): Long = System.currentTimeMillis()

actual fun generateUUID(): String = UUID.randomUUID().toString()
