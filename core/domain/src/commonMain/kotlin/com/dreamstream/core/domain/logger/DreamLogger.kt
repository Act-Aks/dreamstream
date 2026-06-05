package com.dreamstream.core.domain.logger

interface DreamLogger {
    fun debug(message: String)
    fun error(message: String, throwable: Throwable? = null)
    fun info(message: String)
    fun verbose(message: String)
    fun warn(message: String)
}
