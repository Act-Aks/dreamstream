package com.dreamstream.feature.home.data.di

import com.dreamstream.core.runtime.registry.PluginRegistry
import com.dreamstream.feature.home.data.repository.PluginHomeRepository
import com.dreamstream.feature.home.domain.repository.HomeRepository
import org.koin.dsl.module

val homeDataModule = module {
    single<HomeRepository> { PluginHomeRepository(registry = get<PluginRegistry>()) }
}
