package com.tavro.outstanding.component.main

import com.arkivanov.decompose.ComponentContext
import com.tavro.outstanding.base.Configuration
import com.tavro.outstanding.navigation.Navigator

class HomeComponent(
    componentContext: ComponentContext,
    private val navigator: Navigator,
    configuration: Configuration,
) : ComponentContext by componentContext {

}