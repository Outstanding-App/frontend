package com.tavro.outstanding.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import com.tavro.outstanding.designsystem.components.JadeFilledButton
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.theme.contentColorFor
import com.tavro.outstanding.designsystem.tokens.Space

@Composable
fun ProfileScreen(onLogout: () -> Unit, modifier: Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Space.md),
        ) {
            Text(
                text = "Profile",
                style = JadeTheme.typography.headlineMedium,
                color = contentColorFor(JadeTheme.colorScheme.background)
            )
            JadeFilledButton(
                text = "Log out",
                onClick = onLogout,
                enabled = true,
                minFontSize = TextUnit.Unspecified,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
