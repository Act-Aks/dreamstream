package com.dreamstream.core.data.system

import com.dreamstream.core.domain.system.TimeProvider

class TimeProviderImpl : TimeProvider {
    override fun currentTimeMillis(): Long = currentTimeMillis()
}
