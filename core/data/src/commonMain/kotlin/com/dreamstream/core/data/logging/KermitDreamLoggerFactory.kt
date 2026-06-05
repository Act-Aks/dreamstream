package com.dreamstream.core.data.logging

import com.dreamstream.core.domain.logger.DreamLogger
import com.dreamstream.core.domain.logger.LoggerFactory

class KermitDreamLoggerFactory : LoggerFactory {
    override fun get(tag: String): DreamLogger = KermitDreamLogger(tag)
}
