package com.tavro.outstanding.feature.main

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp
import com.tavro.outstanding.core.ui.HapticOnPress
import com.tavro.outstanding.core.ui.letIf
import com.tavro.outstanding.core.ui.visibility
import com.tavro.outstanding.designsystem.components.JadeScaffold
import com.tavro.outstanding.designsystem.components.JadeSurface
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.theme.LocalJadeContentColor
import com.tavro.outstanding.feature.map.MapScreen
import com.tavro.outstanding.feature.profile.ProfileScreen
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MainScreen(
    component: MainComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.state.collectAsState()

    MainScreenContent(
        tabs = state.tabs,
        tabsFinalized = state.tabsFinalized,
        selectedTab = state.activeTab,
        onTabClick = component::onTabClick,
        content = {
            for (tab in state.tabs) {
                val isActiveTab = state.activeTab == tab
                if (!isActiveTab && !tab.neverLeavesComposition) continue
                val tabModifier = Modifier
                    .fillMaxSize()
                    .letIf(tab.neverLeavesComposition) {
                        visibility(visible = isActiveTab)
                    }
                when (tab) {
                    MainScreenTab.Home -> MapScreen(modifier = tabModifier)
                    MainScreenTab.Profile -> ProfileScreen(onLogout = component::onLogout, modifier = tabModifier)
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
internal fun MainScreenContent(
    tabs: ImmutableList<MainScreenTab>,
    tabsFinalized: Boolean,
    selectedTab: MainScreenTab,
    onTabClick: (MainScreenTab) -> Unit,
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    JadeScaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            BottomBar(
                tabs = tabs,
                tabsFinalized = tabsFinalized,
                selectedTab = selectedTab,
                onTabClick = onTabClick,
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .padding(contentPadding)
                .consumeWindowInsets(WindowInsets.navigationBars)
        ) {
            content()
        }
    }
}

@Composable
private fun BottomBar(
    tabs: ImmutableList<MainScreenTab>,
    tabsFinalized: Boolean,
    selectedTab: MainScreenTab,
    onTabClick: (MainScreenTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bottomBarShape = RectangleShape

    JadeSurface(
        shape = bottomBarShape,
        modifier = modifier,
        color = JadeTheme.colorScheme.surfaceContainerLowest
    ) {
        val tabsAlpha = if (tabsFinalized) 1f else 0f
        Row(
            modifier = Modifier.fillMaxWidth()
                .alpha(tabsAlpha)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .selectableGroup(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, alignment = Alignment.CenterHorizontally),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab -> BottomBarItem(tab, selectedTab == tab, onTabClick) }
        }
    }
}

@Stable
private fun NavigationBarItemColors.iconColor(selected: Boolean, enabled: Boolean): Color = when {
    !enabled -> disabledIconColor
    selected -> selectedIconColor
    else -> unselectedIconColor
}

@Composable
private fun RowScope.BottomBarItem(
    tab: MainScreenTab,
    selected: Boolean,
    onTabClick: (MainScreenTab) -> Unit
) {
    val colors = NavigationBarItemDefaults.colors().copy(
        selectedIconColor = JadeTheme.colorScheme.primary,
        unselectedIconColor = JadeTheme.colorScheme.onSurfaceVariant,
        selectedTextColor = JadeTheme.colorScheme.onSurface,
        unselectedTextColor = JadeTheme.colorScheme.onSurfaceVariant,
    )

    val interactionSource = remember { MutableInteractionSource() }
    HapticOnPress(interactionSource, HapticFeedbackType.LongPress)
    Box(
        Modifier
            .selectable(
                selected = selected,
                onClick = {
                    onTabClick(tab)
                },
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = null,
            )
            .height(height = 48.dp)
            .weight(1f)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.matchParentSize(),
        ) {
            BottomBarItemIcon(selected, colors, tab)
        }
    }
}

@Composable
private fun BottomBarItemIcon(
    selected: Boolean,
    colors: NavigationBarItemColors,
    tab: MainScreenTab
) {
    val title = tab.title
    val iconColor = colors.iconColor(selected = selected, enabled = true)
    CompositionLocalProvider(LocalJadeContentColor provides iconColor) {
        Box(modifier = Modifier.clearAndSetSemantics {}) {
            Icon(
                painter = rememberVectorPainter(tab.icons.filled),
                contentDescription = title,
                tint = iconColor,
            )
            Icon(
                painter = rememberVectorPainter(tab.icons.normal),
                contentDescription = title,
                tint = iconColor,
            )
        }
    }
}
