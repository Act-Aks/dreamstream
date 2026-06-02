package di

import com.dreamstream.core.data.di.coreDataModule
import com.dreamstream.core.data.di.platformCoreDataModule
import com.dreamstream.core.runtime.di.pluginRuntimeModule
import com.dreamstream.core.runtime.loader.BundledPluginLoader
import com.dreamstream.core.runtime.loader.PluginLoader
import com.dreamstream.feature.details.data.di.detailsDataModule
import com.dreamstream.feature.details.presentation.di.detailsPresentationModule
import com.dreamstream.feature.home.data.di.homeDataModule
import com.dreamstream.feature.home.presentation.di.homePresentationModule
import com.dreamstream.feature.search.data.di.searchDataModule
import com.dreamstream.feature.search.presentation.di.searchPresentationModule
import com.dreamstream.feature.settings.data.di.settingsDataModule
import com.dreamstream.feature.settings.presentation.di.settingsPresentationModule
import com.dreamstream.plugin.flixhq.FlixHqPlugin
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Starts the Koin dependency graph.
 *
 * Bundled plugins (Gradle-module plugins like FlixHQ) are always included.
 * Platform-specific loaders (e.g. [ApkPluginLoader] on Android) are passed via
 * [additionalPluginLoaders] from the platform's entry point so that
 * [app:shared] remains KMP-compatible.
 *
 * @param additionalPluginLoaders Extra [PluginLoader]s to add alongside the
 *   bundled defaults. On Android, pass [ApkPluginLoader] from [DreamStreamApplication].
 * @param config Optional Koin app declaration for platform-specific setup
 *   (e.g. `androidContext(this)` on Android).
 */
fun initKoin(
    additionalPluginLoaders: List<PluginLoader> = emptyList(),
    config: KoinAppDeclaration? = null,
) {
    val allLoaders: List<PluginLoader> =
        listOf(BundledPluginLoader(listOf(FlixHqPlugin()))) + additionalPluginLoaders

    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            platformCoreDataModule,
            pluginRuntimeModule(allLoaders),
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
    }
}
