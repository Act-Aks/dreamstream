package com.dreamstream

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import di.initKoin
import org.koin.android.ext.koin.androidContext

class DreamStreamApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Restore the saved app locale (required on API 26–32 where AppCompat
        // does not auto-restore the locale after process restart without an
        // explicit reapplication. On API 33+ this is handled by the system.)
        AppCompatDelegate.setApplicationLocales(AppCompatDelegate.getApplicationLocales())

        initKoin {
            androidContext(this@DreamStreamApplication)
        }
    }
}
