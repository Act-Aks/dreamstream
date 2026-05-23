package com.dreamstream.feature.details.data.di

import com.dreamstream.feature.details.data.repository.InMemoryDetailsRepository
import com.dreamstream.feature.details.domain.repository.DetailsRepository
import org.koin.dsl.module

val detailsDataModule = module {
    single<DetailsRepository> { InMemoryDetailsRepository() }
}
