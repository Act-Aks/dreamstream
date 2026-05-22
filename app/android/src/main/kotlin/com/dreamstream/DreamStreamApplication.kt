package com.dreamstream

import android.app.Application
import com.dreamstream.feature.home.data.di.homeDataModule
import com.dreamstream.feature.home.presentation.di.homePresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DreamStreamApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@DreamStreamApplication)
            modules(
                homeDataModule,
                homePresentationModule,
            )
        }
    }
}
