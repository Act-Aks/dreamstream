package com.dreamstream.core.data.system

import com.dreamstream.core.data.util.Platform
import com.dreamstream.core.domain.system.PlatformInfo

class PlatformInfoImpl : PlatformInfo {
    override val name: String get() = Platform.name
    override val isDebug: Boolean get() = Platform.isDebug
    override val osVersion: String get() = Platform.osVersion
    override val appVersion: String get() = Platform.appVersion
    override val appVersionCode: Int get() = Platform.appVersionCode
}
