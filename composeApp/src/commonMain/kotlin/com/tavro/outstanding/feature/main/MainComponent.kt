package com.tavro.outstanding.feature.main

import com.arkivanov.decompose.ComponentContext
import com.tavro.outstanding.core.koin.OutstandingKoinComponent
import com.tavro.outstanding.data.account.AccountRepository
import com.tavro.outstanding.feature.login.componentScope
import com.tavro.outstanding.navigation.Config
import com.tavro.outstanding.navigation.Navigator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainComponent(
    componentContext: ComponentContext,
    config: Config.Main,
    private val navigator: Navigator,
    private val accountRepository: AccountRepository,
) : ComponentContext by componentContext, OutstandingKoinComponent {
    private val _state = MutableStateFlow(MainScreenState(activeTab = config.tab.mainScreenTab))
    val state = _state.asStateFlow()

    fun onTabClick(tab: MainScreenTab) {
        if (_state.value.activeTab == tab) return
        when (tab) {
            MainScreenTab.Home -> Unit
            MainScreenTab.Profile -> Unit
        }
        _state.update { it.copy(activeTab = tab) }
    }

    fun activeTab(tab: MainScreenTab) = onTabClick(tab)

    fun onLogout() {
        componentScope.launch { accountRepository.clear() }
    }
}
