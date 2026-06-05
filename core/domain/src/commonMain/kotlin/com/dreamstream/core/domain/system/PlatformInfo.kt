package com.dreamstream.core.domain.system

interface PlatformInfo {
    val name: String
    val isDebug: Boolean
    val osVersion: String
    val appVersion: String
    val appVersionCode: Int
}
