package com.dreamstream.core.data.di

import com.dreamstream.core.data.logging.KermitDreamLogger
import com.dreamstream.core.data.logging.KermitDreamLoggerFactory
import com.dreamstream.core.domain.logger.DreamLogger
import com.dreamstream.core.domain.logger.LoggerFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val coreDataLoggerModule: Module = module {
    single<DreamLogger> { KermitDreamLogger.Default }
    single<LoggerFactory> { KermitDreamLoggerFactory() }
}
