package com.dreamstream.core.data.system

import com.dreamstream.core.domain.system.UuidProvider

class UuidProviderImpl : UuidProvider {
    override fun generateUUID(): String = generateUUID()
}
