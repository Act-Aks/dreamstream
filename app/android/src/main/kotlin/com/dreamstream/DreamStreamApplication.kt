package com.dreamstream

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.dreamstream.core.runtime.loader.ApkPluginLoader
import di.initKoin
import org.koin.android.ext.koin.androidContext

class DreamStreamApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Restore the saved app locale (required on API 26–32 where AppCompat
        // does not auto-restore the locale after process restart without an
        // explicit reapplication. On API 33+ this is handled by the system.)
        AppCompatDelegate.setApplicationLocales(AppCompatDelegate.getApplicationLocales())

        initKoin(
            additionalPluginLoaders = listOf(
                // APK-based plugin loading is scaffolded; returns empty list until
                // the DexClassLoader integration is completed in a future release.
                ApkPluginLoader(
                    pluginsDir = filesDir.resolve("plugins"),
                    context = this,
                ),
            ),
        ) {
            androidContext(this@DreamStreamApplication)
        }
    }
}
