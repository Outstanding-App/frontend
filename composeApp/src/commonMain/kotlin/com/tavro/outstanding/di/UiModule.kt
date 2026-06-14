package com.tavro.outstanding.di

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.StackNavigation
import com.tavro.outstanding.RootComponent
import com.tavro.outstanding.feature.home.HomeComponent
import com.tavro.outstanding.feature.login.LoginComponent
import com.tavro.outstanding.feature.main.MainComponent
import com.tavro.outstanding.navigation.Config
import com.tavro.outstanding.navigation.Navigator
import com.tavro.outstanding.navigation.bindConfig
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val uiModule = module {
    single { Navigator(StackNavigation(), get(), get()) }
    factory { (ctx: ComponentContext) ->
        RootComponent(ctx, get(), get(), get(), get())
    }
    factory { (ctx: ComponentContext, config: Config.Main) ->
        MainComponent(ctx, config, get(), get())
    }
    factory { (ctx: ComponentContext) ->
        HomeComponent(ctx, get(), get())
    }
    factoryOf(::LoginComponent) bindConfig Config.Onboarding.Login::class
}
