package com.dreamstream.feature.search.data.di

import com.dreamstream.feature.search.data.repository.InMemorySearchRepository
import com.dreamstream.feature.search.domain.repository.SearchRepository
import org.koin.dsl.module

val searchDataModule = module {
    single<SearchRepository> { InMemorySearchRepository() }
}
