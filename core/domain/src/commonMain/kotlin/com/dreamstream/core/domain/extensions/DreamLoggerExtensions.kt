package com.dreamstream.core.domain.extensions

import com.dreamstream.core.domain.logger.DreamLogger

inline fun DreamLogger.debug(message: () -> String) {
    debug(message())
}

inline fun DreamLogger.info(message: () -> String) {
    info(message())
}

inline fun DreamLogger.warn(message: () -> String) {
    warn(message())
}

inline fun DreamLogger.verbose(message: () -> String) {
    verbose(message())
}

inline fun DreamLogger.error(throwable: Throwable? = null, message: () -> String) {
    error(message(), throwable)
}
