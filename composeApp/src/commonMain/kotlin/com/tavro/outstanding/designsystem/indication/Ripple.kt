package com.tavro.outstanding.designsystem.indication

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.createRippleModifierNode
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorProducer
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DelegatingNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.unit.Dp
import com.tavro.outstanding.designsystem.theme.LocalJadeContentColor

/**
 * Creates a [IndicationNodeFactory] that draws a ripple on interaction.
 *
 * Color resolution order: [color] -> [LocalJadeContentColor] -> [fallbackColorProvider].
 *
 * @param fallbackColorProvider Last-resort color source when neither [color] nor [LocalJadeContentColor] is specified.
 */
@Stable
fun jadeRipple(
    bounded: Boolean = true,
    radius: Dp = Dp.Unspecified,
    color: Color = Color.Unspecified,
    fallbackColorProvider: ProvidableCompositionLocal<Color>
): IndicationNodeFactory {
    return RippleNodeFactory(bounded, radius, color, fallbackColorProvider)
}

@Stable
private data class RippleNodeFactory(
    private val bounded: Boolean,
    private val radius: Dp,
    private val color: Color,
    val fallbackColorProvider: ProvidableCompositionLocal<Color>,
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return DelegatingThemeAwareRippleNode(
            interactionSource,
            bounded,
            radius,
            color,
            fallbackColorProvider
        )
    }
}

private class DelegatingThemeAwareRippleNode(
    private val interactionSource: InteractionSource,
    private val bounded: Boolean,
    private val radius: Dp,
    private val color: Color,
    private val fallbackColorProvider: ProvidableCompositionLocal<Color>,
) : DelegatingNode(), CompositionLocalConsumerModifierNode {
    private var rippleNode: DelegatableNode? = null

    override fun onAttach() {
        if (rippleNode == null) attachNewRipple()
    }

    override fun onDetach() {
        rippleNode?.let { undelegate(it) }
    }

    private fun attachNewRipple() {
        val calculateColor = ColorProducer {
            color
                .takeOrElse { currentValueOf(LocalJadeContentColor) }
                .takeOrElse { currentValueOf(fallbackColorProvider) }
        }

        rippleNode =
            delegate(
                createRippleModifierNode(
                    interactionSource,
                    bounded,
                    radius,
                    calculateColor
                ) { RippleAlphaDefault }
            )
    }
}

/**
 * Material 3 state layer alpha values:
 * https://m3.material.io/foundations/interaction/states/applying-states
 */
val RippleAlphaDefault = RippleAlpha(
    draggedAlpha = 0.16f,
    focusedAlpha = 0.1f,
    hoveredAlpha = 0.08f,
    pressedAlpha = 0.1f
)
