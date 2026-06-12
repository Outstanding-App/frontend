package com.tavro.outstanding.base.util

import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

fun Modifier.letIf(condition: Boolean, block: Modifier.() -> Modifier) = if (condition) block() else this

/** Hides the composable without removing it from layout. The occupied space is preserved. */
fun Modifier.visibility(visible: Boolean) = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            if (visible) {
                placeable.place(0, 0)
            }
        }
    }
)
