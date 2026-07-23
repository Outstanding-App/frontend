package com.tavro.outstanding.core.ui

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import com.arkivanov.decompose.router.stack.pushNew
import com.tavro.outstanding.navigation.Config
import com.tavro.outstanding.navigation.Navigator

@Composable
fun scaffoldContentWindowInsets(): WindowInsets = WindowInsets.navigationBars

sealed interface UserMessage {
    val text: String
    data class Info(override val text: String, val action: SnackbarData.Action? = null) : UserMessage
    data class Error(override val text: String, val cause: Throwable? = null) : UserMessage
}

@Immutable
data class SnackbarData(
    val message: String?,
    val action: Action? = null,
    val onShown: () -> Unit,
) {
    data class Action(val label: String, val onClick: () -> Unit)
}

@Composable
fun bindDataToSnackbarHost(data: SnackbarData?): SnackbarHostState {
    val snackbarHostState = remember { SnackbarHostState() }
    val message = data?.message
    if (message != null) {
        val action = data.action
        LaunchedEffect(message) {
            val res = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = action?.label,
                duration = if (action != null) SnackbarDuration.Long else SnackbarDuration.Short
            )
            when (res) {
                SnackbarResult.Dismissed -> Unit
                SnackbarResult.ActionPerformed -> {
                    action?.onClick?.invoke()
                }
            }
            data.onShown()
        }
    }
    return snackbarHostState
}

typealias OnErrorDetailsClick = (error: UserMessage.Error) -> Unit

fun Navigator.navigateToErrorDetails(error: UserMessage.Error) {
    val stacktrace = error.cause?.stackTraceToString()
    pushNew(
        Config.Error(
            message = error.cause?.message ?: error.text,
            stacktrace = stacktrace
        )
    )
}

@Composable
fun bindMessageToSnackbarHostState(
    message: UserMessage?,
    onMessageShown: () -> Unit,
    onErrorDetailsClick: OnErrorDetailsClick?
): SnackbarHostState {
    val messageText = message?.text
    if (messageText != null && message is UserMessage.Error) {
        LaunchedEffect(messageText) {
            println("Message=" + messageText + " Throwable=" + message.cause)
        }
    }
    val action = if (message is UserMessage.Error)
        null
    else if (message is UserMessage.Error && onErrorDetailsClick != null) SnackbarData.Action(
        label = "Details",
        onClick = { onErrorDetailsClick(message) }
    ) else if (message is UserMessage.Info && message.action != null) SnackbarData.Action(
        label = message.action.label,
        onClick = message.action.onClick
    ) else null
    return bindDataToSnackbarHost(
        data = SnackbarData(
            message = messageText,
            onShown = onMessageShown,
            action = action
        )
    )
}

@Composable
fun bindErrorToSnackbarHostState(
    error: UserMessage.Error?,
    onErrorShown: () -> Unit,
    onErrorDetailsClick: OnErrorDetailsClick?
): SnackbarHostState = bindMessageToSnackbarHostState(
    message = error,
    onMessageShown = onErrorShown,
    onErrorDetailsClick = onErrorDetailsClick
)
