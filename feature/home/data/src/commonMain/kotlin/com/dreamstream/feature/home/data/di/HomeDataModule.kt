package com.dreamstream.feature.home.data.di

import com.dreamstream.feature.home.data.repository.HomeRepositoryImpl
import com.dreamstream.feature.home.domain.repository.HomeRepository
import org.koin.dsl.module

val homeDataModule = module {
    single<HomeRepository> {
        HomeRepositoryImpl(
            contentRepository = get(),
            providerId = "movieboxonline",
        )
    }
}
