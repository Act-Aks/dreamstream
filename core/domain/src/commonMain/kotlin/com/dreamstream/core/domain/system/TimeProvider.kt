package com.dreamstream.core.domain.system

interface TimeProvider {
    fun currentTimeMillis(): Long
}
