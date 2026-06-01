package di

import com.dreamstream.feature.details.data.di.detailsDataModule
import com.dreamstream.feature.details.presentation.di.detailsPresentationModule
import com.dreamstream.feature.home.data.di.homeDataModule
import com.dreamstream.feature.home.presentation.di.homePresentationModule
import com.dreamstream.feature.search.data.di.searchDataModule
import com.dreamstream.feature.search.presentation.di.searchPresentationModule
import com.dreamstream.feature.settings.data.di.settingsDataModule
import com.dreamstream.feature.settings.presentation.di.settingsPresentationModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
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
