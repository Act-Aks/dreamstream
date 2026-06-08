package com.dreamstream.core.data.di

import com.dreamstream.core.data.system.AppStorageProviderImpl
import com.dreamstream.core.data.system.PlatformInfoImpl
import com.dreamstream.core.data.system.TimeProviderImpl
import com.dreamstream.core.data.system.UuidProviderImpl
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.core.domain.system.PlatformInfo
import com.dreamstream.core.domain.system.TimeProvider
import com.dreamstream.core.domain.system.UuidProvider
import org.koin.core.module.Module
import org.koin.dsl.module

val coreDataSystemModule: Module = module {

    // AppStorageProvider
    single<AppStorageProvider> { AppStorageProviderImpl() }

    // PlatformInfo
    single<PlatformInfo> { PlatformInfoImpl() }

    // TimeProvider
    single<TimeProvider> { TimeProviderImpl() }

    // UuidProvider
    single<UuidProvider> { UuidProviderImpl() }
}
