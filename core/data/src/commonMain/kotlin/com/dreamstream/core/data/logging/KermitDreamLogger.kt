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
object KermitDreamLogger : DreamLogger {
    override fun debug(message: String) = Logger.d(message)
    override fun info(message: String) = Logger.i(message)
    override fun warn(message: String) = Logger.w(message)
    override fun error(message: String, throwable: Throwable?) = Logger.e(message, throwable)
}
