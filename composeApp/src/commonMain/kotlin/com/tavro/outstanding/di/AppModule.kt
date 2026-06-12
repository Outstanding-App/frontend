package com.tavro.outstanding.di

import com.tavro.outstanding.UiInitializer
import com.tavro.outstanding.base.Configuration
import com.tavro.outstanding.base.koin.OutstandingKoinContext
import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinApplication
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Initializes the app's Koin graph and returns [OutstandingExports] for the host to use.
 *
 * @param koinInit Optional block for the host to add extra modules.
 */
fun setupOutstanding(
    dependencies: OutstandingDependencies,
    koinInit: KoinApplication.() -> Unit = {}
): OutstandingExports {
    val app = OutstandingKoinContext.initialize {
        modules(buildAppModule(dependencies))
        koinInit()
    }
    return app.koin.get<OutstandingExports>()
}

fun buildAppModule(dependencies: OutstandingDependencies) = module {
    includes(modelModule, uiModule)
    val lazyScope = lazy { kotlinx.coroutines.MainScope() }
    single<CoroutineScope> { lazyScope.value }
    single<Configuration> { dependencies.configuration }
    single { OutstandingInitializer(getAll<InitializationListener>()) } bind UiInitializer::class
    single { UiInitializer.Chain(getAll<UiInitializer>()) }
    single { OutstandingExports(lazy { get<OutstandingInitializer>() }) }
}
