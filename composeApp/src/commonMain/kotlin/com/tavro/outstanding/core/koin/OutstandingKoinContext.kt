package com.tavro.outstanding.core.koin

import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinApplication

/**
 * Singleton holder for the app's isolated Koin instance. Using an isolated context (rather than
 * Koin's global `startKoin`) means the library's DI graph does not bleed into host apps.
 *
 * Call [initialize] once at startup and [clear] when tearing down.
 */
object OutstandingKoinContext {
    private var _application: KoinApplication? = null

    val application: KoinApplication
        get() = _application ?: error(
            "OutstandingKoinContext is not initialized. Call initialize() first."
        )

    val koin: Koin
        get() = application.koin

    fun initialize(configuration: KoinAppDeclaration): KoinApplication {
        if (_application != null) {
            error("OutstandingKoinContext is already initialized. Call clear() first if you needs to reinitialize.")
        }

        val app = koinApplication {
            configuration()
        }
        _application = app
        return app
    }

    fun clear() {
        _application?.close()
        _application = null
    }
}
