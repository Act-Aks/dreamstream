package com.dreamstream.core.data.di

import com.dreamstream.core.data.network.DreamHttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataNetworkModule: Module = module {
    singleOf(::DreamHttpClientFactory)
    single<HttpClient> { get<DreamHttpClientFactory>().create(get()) }
}
