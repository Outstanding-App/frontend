package com.tavro.outstanding.core.ui

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun HapticOnPress(interactionSource: MutableInteractionSource, type: HapticFeedbackType) {
    val hapticFeedback = LocalHapticFeedback.current
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect {
            if (it is PressInteraction.Press) hapticFeedback.performHapticFeedback(type)
        }
    }
}
