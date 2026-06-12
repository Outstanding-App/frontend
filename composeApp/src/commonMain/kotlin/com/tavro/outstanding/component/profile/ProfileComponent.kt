package com.tavro.outstanding.component.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ComponentContext
import com.tavro.outstanding.base.koin.OutstandingKoinComponent
import com.tavro.outstanding.navigation.Component
import com.tavro.outstanding.navigation.Navigator

class ProfileComponent(
    componentContext: ComponentContext,
    private val navigator: Navigator,
) : Component, ComponentContext by componentContext, OutstandingKoinComponent {

    @Composable
    override fun Render(modifier: Modifier) {
        ProfileScreen(modifier = modifier)
    }
}
