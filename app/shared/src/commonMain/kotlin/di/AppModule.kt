package di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.dsl.module

internal val appModule = module {
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}
