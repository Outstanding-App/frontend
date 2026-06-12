package com.tavro.outstanding

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.remember

/**
 * A synchronous side effect that re-runs whenever [key1] changes. Unlike LaunchedEffect it runs
 * during composition with no coroutine overhead. Only use for non-suspending side effects.
 */
@Composable
@NonRestartableComposable
fun SimpleEffect(
    key1: Any?,
    calculation: () -> Unit,
): Unit = remember(
    key1 = key1,
    calculation = calculation,
)
