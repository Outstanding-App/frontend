package com.tavro.outstanding.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.theme.contentColorFor

@Composable
fun MapScreen(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Map",
            style = JadeTheme.typography.headlineMedium,
            color = contentColorFor(JadeTheme.colorScheme.background)
        )
    }
}
