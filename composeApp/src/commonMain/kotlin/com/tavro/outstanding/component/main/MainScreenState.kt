package com.tavro.outstanding.component.main

import com.tavro.outstanding.component.login.UserMessage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

/**
 * @property tabsFinalized `false` while the tab list is being determined.
 * The bottom bar is hidden until this is `true` to avoid a flash of incorrect tabs.
 */
data class MainScreenState(
    val tabs: ImmutableList<MainScreenTab> = MainScreenTab.entries.toImmutableList(),
    val tabsFinalized: Boolean = true,
    val activeTab: MainScreenTab = MainScreenTab.Home,
    val error: UserMessage.Error? = null,
)
