package com.tavro.outstanding.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Base navigation unit. [accept] is the result-delivery protocol, callers use [Navigator.deliver]
 * or [Navigator.popWithResult] to push a value into the active component. The default no-op means
 * components only opt in to results they care about.
 */
interface BaseComponent {
    fun accept(value: Any) {}
}

/** A [BaseComponent] that also knows how to render itself as a composable. */
interface Component : BaseComponent {
    @Composable
    fun Render(modifier: Modifier)
}
