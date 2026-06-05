package com.dreamstream.core.domain.logger

interface LoggerFactory {
    fun get(tag: String): DreamLogger
}
