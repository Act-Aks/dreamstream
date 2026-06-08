package com.dreamstream.core.data.di

import com.dreamstream.core.data.logging.KermitDreamLogger
import com.dreamstream.core.data.logging.KermitDreamLoggerFactory
import com.dreamstream.core.domain.logger.DreamLogger
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataLoggerModule: Module = module {
    single<DreamLogger> { KermitDreamLogger.Default }
    singleOf(::KermitDreamLoggerFactory)
}
