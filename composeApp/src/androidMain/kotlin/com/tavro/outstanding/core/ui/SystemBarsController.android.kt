package com.tavro.outstanding.core.ui

import android.content.res.Configuration
import android.content.res.Resources
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.tavro.outstanding.findComponentActivity

@Composable
actual fun SystemBarsController(theme: SystemBarTheme) {
    if (LocalInspectionMode.current) return
    val context = LocalContext.current
    val activity = remember(context) { context.findComponentActivity() }
    var shouldResetOnDispose = false

    DisposableEffect(activity, theme, isSystemInDarkTheme()) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    activity.updateSystemBarStyles(theme)
                    shouldResetOnDispose = true
                }

                Lifecycle.Event.ON_STOP -> {
                    activity.resetSystemBarStyles()
                    shouldResetOnDispose = false
                }

                else -> Unit
            }
        }

        activity.lifecycle.addObserver(observer)
        onDispose {
            activity.lifecycle.removeObserver(observer)
            if (shouldResetOnDispose) {
                shouldResetOnDispose = false
                activity.resetSystemBarStyles()
            }
        }
    }
}

private fun ComponentActivity.updateSystemBarStyles(theme: SystemBarTheme) {
    enableEdgeToEdge(
        statusBarStyle = SystemBarStyle.auto(
            lightScrim = DefaultStatusBarColor,
            darkScrim = DefaultStatusBarColor,
            detectDarkMode = { resources ->
                detectDarkMode(theme, resources)
            },
        ),
        navigationBarStyle = SystemBarStyle.auto(
            lightScrim = DefaultLightScrim,
            darkScrim = DefaultDarkScrim,
            detectDarkMode = { resources ->
                detectDarkMode(theme, resources)
            },
        )
    )
}

private fun ComponentActivity.resetSystemBarStyles() = updateSystemBarStyles(SystemBarTheme.AUTO)

private fun detectDarkMode(theme: SystemBarTheme, resources: Resources): Boolean = when (theme) {
    SystemBarTheme.AUTO -> {
        val nightModeConfig = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        nightModeConfig == Configuration.UI_MODE_NIGHT_YES
    }

    SystemBarTheme.LIGHT -> false

    SystemBarTheme.DARK -> true
}

private val DefaultStatusBarColor = Color.Transparent.toArgb()

// Standard Android scrim values (API 29+).
private val DefaultLightScrim = Color(0xe6FFFFFF).toArgb()
private val DefaultDarkScrim = Color(0x801b1b1b).toArgb()
