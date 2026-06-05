package com.dreamstream.feature.home.data.di

import com.dreamstream.feature.home.data.repository.InMemoryHomeRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeDataModule = module {
    singleOf(::InMemoryHomeRepository)
}
