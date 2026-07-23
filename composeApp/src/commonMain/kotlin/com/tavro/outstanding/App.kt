package com.tavro.outstanding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tavro.outstanding.core.koin.OutstandingKoinContext
import com.tavro.outstanding.designsystem.theme.JadeTheme
import org.koin.compose.KoinIsolatedContext

/**
 * Root composable. Scopes Koin to the isolated [OutstandingKoinContext] so that compose extensions
 * like `getKoin()` resolve against the app's own graph rather than any global Koin instance.
 */
@Composable
fun App(component: RootComponent, modifier: Modifier = Modifier) {
    KoinIsolatedContext(context = OutstandingKoinContext.application) {
        JadeTheme {
            Box(
                modifier = modifier.background(JadeTheme.colorScheme.background)
            ) {
                RootContent(component = component)
            }
        }
    }
}
