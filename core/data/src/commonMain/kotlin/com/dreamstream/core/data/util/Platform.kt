package com.dreamstream.core.data.util

expect object Platform {
    val name: String
    val isDebug: Boolean
    val osVersion: String
    val appVersion: String
    val appVersionCode: Int
}

expect fun currentTimeMillis(): Long

expect fun generateUUID(): String
