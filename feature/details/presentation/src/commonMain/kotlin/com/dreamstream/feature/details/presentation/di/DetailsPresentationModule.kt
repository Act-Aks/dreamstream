package com.dreamstream.feature.details.presentation.di

import com.dreamstream.feature.details.presentation.DetailsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val detailsPresentationModule = module {
    viewModel { params -> DetailsViewModel(params.get(), get()) }
}
