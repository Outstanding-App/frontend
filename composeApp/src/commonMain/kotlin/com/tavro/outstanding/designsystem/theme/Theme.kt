package com.tavro.outstanding.designsystem.theme

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.tavro.outstanding.designsystem.indication.jadeRipple

/**
 * Top-level theme wrapper for the app. Applies [ApplyTheme] and bridges to [MaterialTheme] so
 * Material 3 components pick up Jade tokens. Also installs [jadeRipple] as the global indication.
 */
@Composable
fun JadeTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    ApplyTheme(useDarkTheme = useDarkTheme) {
        MaterialTheme(
            colorScheme = JadeTheme.colorScheme.toM3ColorScheme(),
            typography = JadeTheme.typography.toM3Typography(),
            shapes = JadeTheme.shapes.toM3Shapes(),
            content = {
                val ripple = jadeRipple(fallbackColorProvider = LocalContentColor)
                CompositionLocalProvider(LocalIndication provides ripple) {
                    content()
                }
            }
        )
    }
}
