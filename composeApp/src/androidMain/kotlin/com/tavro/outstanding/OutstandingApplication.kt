package com.tavro.outstanding

import android.app.Application
import com.tavro.outstanding.base.Configuration
import com.tavro.outstanding.base.koin.OutstandingKoinComponent
import com.tavro.outstanding.di.OutstandingDependencies
import com.tavro.outstanding.di.setupOutstanding
import com.tavro.outstanding.model.database.AndroidOutstandingDatabaseFactory
import com.tavro.outstanding.model.database.OutstandingDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.get
import org.koin.dsl.bind
import org.koin.dsl.module

class OutstandingApplication : Application(), OutstandingKoinComponent {

    override fun onCreate() {
        super.onCreate()
        val exports = setupOutstanding(
            dependencies = object : OutstandingDependencies {
                override val configuration = Configuration(isPublicBuild = false)
            }
        ) {
            androidContext(this@OutstandingApplication)
            modules(module {
                single { AndroidOutstandingDatabaseFactory(get()) } bind OutstandingDatabase.Factory::class
            })
        }
        get<CoroutineScope>().launch {
            yield()
            exports.initializer.value.initialize()
        }
    }
}
