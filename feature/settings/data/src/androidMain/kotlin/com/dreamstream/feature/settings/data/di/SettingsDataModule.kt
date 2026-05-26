package com.dreamstream.feature.settings.data.di

import com.dreamstream.feature.settings.data.repository.AppCompatLanguageRepository
import com.dreamstream.feature.settings.domain.repository.LanguageRepository
import org.koin.dsl.module

val settingsDataModule = module {
    single<LanguageRepository> { AppCompatLanguageRepository() }
}
