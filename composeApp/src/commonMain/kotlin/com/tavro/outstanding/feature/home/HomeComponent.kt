package com.tavro.outstanding.feature.home

import com.arkivanov.decompose.ComponentContext
import com.tavro.outstanding.core.Configuration
import com.tavro.outstanding.navigation.Navigator

class HomeComponent(
    componentContext: ComponentContext,
    private val navigator: Navigator,
    configuration: Configuration,
) : ComponentContext by componentContext {

}
