package com.tavro.outstanding.feature.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.theme.contentColorFor

@Composable
fun ProfileScreen(modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Profile",
            style = JadeTheme.typography.headlineMedium,
            color = contentColorFor(JadeTheme.colorScheme.background)
        )
    }
}
