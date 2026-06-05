package com.dreamstream.feature.search.data.di

import com.dreamstream.feature.search.data.repository.InMemorySearchRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val searchDataModule = module {
    singleOf(::InMemorySearchRepository)
}
