package com.tavro.outstanding.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.ui.graphics.vector.ImageVector
import com.tavro.outstanding.navigation.Config
import org.jetbrains.compose.resources.StringResource
import outstanding.composeapp.generated.resources.Res
import outstanding.composeapp.generated.resources.tab_label_home
import outstanding.composeapp.generated.resources.tab_label_profile

/**
 * @property neverLeavesComposition When `true` the tab's content stays in the composition tree
 * while another tab is active, preserving scroll position and state. Set to `false`
 * for tabs whose content is cheap to recreate or must always start fresh.
 */
enum class MainScreenTab(
    val icons: TabIcons,
    val title: StringResource,
    val neverLeavesComposition: Boolean = false,
) {
    Home(
        icons = TabIcons(normal = Icons.AutoMirrored.Default.Feed, filled = Icons.AutoMirrored.Filled.Feed),
        title = Res.string.tab_label_home,
        neverLeavesComposition = true
    ),
    Profile(
        icons = TabIcons(normal = Icons.AutoMirrored.Default.Help, filled = Icons.AutoMirrored.Filled.Help),
        title = Res.string.tab_label_profile,
        neverLeavesComposition = false
    );

    data class TabIcons(val normal: ImageVector, val filled: ImageVector)
}

val Config.Main.Tab.mainScreenTab: MainScreenTab
    get() = when(this) {
        Config.Main.Tab.Home -> MainScreenTab.Home
        Config.Main.Tab.Profile -> MainScreenTab.Profile
    }
