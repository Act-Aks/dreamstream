package com.dreamstream.core.data.logging

import co.touchlab.kermit.Logger
import com.dreamstream.core.domain.logger.DreamLogger

/**
 * [DreamLogger] implementation backed by Kermit's multiplatform [Logger].
 *
 * Kermit routes log output to the platform-appropriate sink automatically:
 * - **Android** → Logcat
 * - **Desktop** → stdout
 *
 * Bound to [DreamLogger] as a singleton in [com.dreamstream.core.data.di.coreDataModule].
 * Callers depend only on [DreamLogger] — Kermit is an implementation detail of
 * `:core:data` and never leaks into domain or feature modules.
 */
class KermitDreamLogger private constructor(tag: String) : DreamLogger {
    private val logger = Logger.withTag(tag)

    override fun debug(message: String) = logger.d { message }
    override fun error(message: String, throwable: Throwable?) = logger.e(throwable) { message }
    override fun info(message: String) = logger.i { message }
    override fun verbose(message: String) = logger.v { message }
    override fun warn(message: String) = logger.w { message }

    companion object {
        val Default: DreamLogger = KermitDreamLogger("DreamStreamLogger")
        operator fun invoke(tag: String): DreamLogger = KermitDreamLogger(tag)
    }
}
