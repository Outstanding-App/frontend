package com.tavro.outstanding

import androidx.compose.runtime.Composable

@Composable
expect fun SystemBarsController(theme: SystemBarTheme = SystemBarTheme.AUTO)

enum class SystemBarTheme {
    AUTO,
    LIGHT,
    DARK,
}
