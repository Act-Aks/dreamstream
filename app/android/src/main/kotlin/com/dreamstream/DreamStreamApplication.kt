package com.dreamstream

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dreamstream.feature.details.data.di.detailsDataModule
import com.dreamstream.feature.details.presentation.di.detailsPresentationModule
import com.dreamstream.feature.home.data.di.homeDataModule
import com.dreamstream.feature.home.presentation.di.homePresentationModule
import com.dreamstream.feature.search.data.di.searchDataModule
import com.dreamstream.feature.search.presentation.di.searchPresentationModule
import com.dreamstream.feature.settings.data.di.settingsDataModule
import com.dreamstream.feature.settings.presentation.di.settingsPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class DreamStreamApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Restore the saved app locale (required on API 26–32 where AppCompat
        // does not auto-restore the locale after process restart without an
        // explicit reapplication. On API 33+ this is handled by the system.)
        AppCompatDelegate.setApplicationLocales(AppCompatDelegate.getApplicationLocales())

        startKoin {
            androidContext(this@DreamStreamApplication)
            modules(
                homeDataModule,
                homePresentationModule,
                detailsDataModule,
                detailsPresentationModule,
                searchDataModule,
                searchPresentationModule,
                settingsDataModule,
                settingsPresentationModule,
            )
        }
    }
}
