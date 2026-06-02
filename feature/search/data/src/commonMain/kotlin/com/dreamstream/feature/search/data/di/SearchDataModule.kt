package com.dreamstream.feature.search.data.di

import com.dreamstream.core.runtime.registry.PluginRegistry
import com.dreamstream.feature.search.data.repository.PluginSearchRepository
import com.dreamstream.feature.search.domain.repository.SearchRepository
import org.koin.dsl.module

val searchDataModule = module {
    single<SearchRepository> { PluginSearchRepository(registry = get<PluginRegistry>()) }
}
