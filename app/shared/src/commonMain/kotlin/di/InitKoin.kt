package di

import com.dreamstream.core.data.di.coreDataModule
import com.dreamstream.core.data.di.platformCoreDataModule
import com.dreamstream.core.plugin.loader.PluginManager
import com.dreamstream.feature.details.data.di.detailsDataModule
import com.dreamstream.feature.details.presentation.di.detailsPresentationModule
import com.dreamstream.feature.home.data.di.homeDataModule
import com.dreamstream.feature.home.presentation.di.homePresentationModule
import com.dreamstream.feature.search.data.di.searchDataModule
import com.dreamstream.feature.search.presentation.di.searchPresentationModule
import com.dreamstream.feature.settings.data.di.settingsDataModule
import com.dreamstream.feature.settings.presentation.di.settingsPresentationModule
import com.dreamstream.plugin.movieboxonline.MovieBoxPlugin
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Starts the Koin dependency graph and registers all bundled plugins.
 *
 * Bundled plugins (Gradle-module plugins compiled into the app) are registered
 * directly via [PluginManager.registerBundledPlugin] after Koin is started.
 * This is safe to call on any thread; it performs only directory creation I/O.
 *
 * Dynamic plugins (loaded from user-installed JARs/APKs) are registered
 * separately via [PluginManager.loadInstalledPlugins] triggered from the
 * platform entry point after startup.
 *
 * @param config Optional Koin app declaration for platform-specific setup
 *   (e.g. `androidContext(this)` on Android).
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    val koin = startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            platformCoreDataModule,
            appModule,
            homeDataModule,
            homePresentationModule,
            detailsDataModule,
            detailsPresentationModule,
            searchDataModule,
            searchPresentationModule,
            settingsDataModule,
            settingsPresentationModule,
        )
    }.koin

    // Register bundled (compile-time Gradle module) plugins.
    // Add new bundled plugins here as the app grows.
    koin.get<PluginManager>().apply {
        registerBundledPlugin("movieboxonline", MovieBoxPlugin())
    }
}
