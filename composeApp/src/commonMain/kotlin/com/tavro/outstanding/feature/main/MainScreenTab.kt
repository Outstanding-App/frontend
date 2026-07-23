package com.tavro.outstanding.feature.main

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Feed
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.ui.graphics.vector.ImageVector
import com.tavro.outstanding.navigation.Config

/**
 * @property neverLeavesComposition When `true` the tab's content stays in the composition tree
 * while another tab is active, preserving scroll position and state. Set to `false`
 * for tabs whose content is cheap to recreate or must always start fresh.
 */
enum class MainScreenTab(
    val icons: TabIcons,
    val title: String,
    val neverLeavesComposition: Boolean = false,
) {
    Home(
        icons = TabIcons(normal = Icons.AutoMirrored.Default.Feed, filled = Icons.AutoMirrored.Filled.Feed),
        title = "Home",
        neverLeavesComposition = true
    ),
    Profile(
        icons = TabIcons(normal = Icons.AutoMirrored.Default.Help, filled = Icons.AutoMirrored.Filled.Help),
        title = "Profile",
        neverLeavesComposition = false
    );

    data class TabIcons(val normal: ImageVector, val filled: ImageVector)
}

val Config.Main.Tab.mainScreenTab: MainScreenTab
    get() = when(this) {
        Config.Main.Tab.Home -> MainScreenTab.Home
        Config.Main.Tab.Profile -> MainScreenTab.Profile
    }
