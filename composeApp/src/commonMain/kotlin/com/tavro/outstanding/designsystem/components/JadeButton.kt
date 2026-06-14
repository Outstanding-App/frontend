package com.tavro.outstanding.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.tokens.Space
import com.tavro.outstanding.designsystem.theme.ProvideContentColorTextStyle
import com.tavro.outstanding.designsystem.theme.contentColorFor

/** Controls the height, padding, text style and corner radius of a Jade button. */
enum class JadeButtonSize {
    Large,
    Medium,
    Small,
}

private val JadeButtonSize.minHeight: Dp
    get() = when (this) {
        JadeButtonSize.Large -> 52.dp
        JadeButtonSize.Medium -> 36.dp
        JadeButtonSize.Small -> 28.dp
    }

private data class Metrics(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val textStyle: TextStyle,
    val cornerShape: Shape,
    val minHeight: Dp,
    val borderWidth: Dp = 0.dp,
)

private data class ButtonColors(
    val background: Color,
    val content: Color,
    val border: Color? = null,
)

private enum class ButtonType {
    Filled,
    Outlined
}

@Composable
private fun resolveMetrics(size: JadeButtonSize, type: ButtonType): Metrics {
    return when (type) {
        ButtonType.Outlined -> {
            when (size) {
                JadeButtonSize.Large -> Metrics(
                    horizontalPadding = Space.lg,
                    verticalPadding = Space.md,
                    textStyle = JadeTheme.typography.labelLarge,
                    cornerShape = JadeTheme.shapes.lg,
                    minHeight = size.minHeight,
                    borderWidth = 1.dp,
                )

                JadeButtonSize.Medium -> Metrics(
                    horizontalPadding = Space.md,
                    verticalPadding = Space.xs,
                    textStyle = JadeTheme.typography.labelMedium,
                    cornerShape = JadeTheme.shapes.md,
                    minHeight = size.minHeight,
                    borderWidth = 1.dp,
                )

                JadeButtonSize.Small -> Metrics(
                    horizontalPadding = Space.xs,
                    verticalPadding = Space.xxs,
                    textStyle = JadeTheme.typography.labelSmall,
                    cornerShape = CircleShape,
                    minHeight = size.minHeight,
                    borderWidth = 1.dp,
                )
            }
        }

        ButtonType.Filled -> {
            when (size) {
                JadeButtonSize.Large -> Metrics(
                    horizontalPadding = Space.lg,
                    verticalPadding = Space.md,
                    textStyle = JadeTheme.typography.labelLarge,
                    cornerShape = JadeTheme.shapes.lg,
                    minHeight = size.minHeight,
                )

                JadeButtonSize.Medium -> Metrics(
                    horizontalPadding = Space.md,
                    verticalPadding = Space.xs,
                    textStyle = JadeTheme.typography.labelMedium,
                    cornerShape = JadeTheme.shapes.md,
                    minHeight = size.minHeight,
                )

                JadeButtonSize.Small -> Metrics(
                    horizontalPadding = Space.xs,
                    verticalPadding = Space.xxs,
                    textStyle = JadeTheme.typography.labelSmall,
                    cornerShape = CircleShape,
                    minHeight = size.minHeight,
                )
            }
        }
    }
}

@Composable
private fun ButtonBase(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean,
    size: JadeButtonSize,
    type: ButtonType,
    enabledColors: ButtonColors,
    disabledColors: ButtonColors,
    minFontSize: TextUnit,
    modifier: Modifier = Modifier,
    interactionSource: MutableInteractionSource? = null,
) {
    @Suppress
    val interactionSource = interactionSource ?: remember { MutableInteractionSource() }
    val metrics = resolveMetrics(size = size, type = type)

    val colors = if (enabled) enabledColors else disabledColors
    JadeSurface(
        onClick = onClick,
        interactionSource = interactionSource,
        enabled = enabled,
        modifier = modifier
            .heightIn(min = metrics.minHeight)
            .semantics{ role = Role.Button },
        color = colors.background,
        contentColor = colors.content,
        shape = metrics.cornerShape,
        border = if (colors.border != null) BorderStroke(
            width = metrics.borderWidth,
            color = colors.border
        ) else null,
    ) {
        ProvideContentColorTextStyle(
            contentColor = colors.content,
            textStyle = metrics.textStyle,
        ) {
            Box(contentAlignment = Alignment.Center) {
                JadeText(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    // TODO(010): autoSize
                    minFontSize = minFontSize
                )
            }
        }
    }
}

/** Color set for [JadeFilledButton]. Use [default] to get theme-derived colors. */
data class JadeFilledButtonColors(
    val background: Color,
    val content: Color,
    val disabledBackground: Color,
    val disabledContent: Color,
) {
    companion object {
        @Composable
        fun default(
            background: Color = JadeTheme.colorScheme.secondary,
            content: Color = contentColorFor(background),
            disabledBackground: Color = JadeTheme.colorScheme.disabledSurface,
            disabledContent: Color = contentColorFor(disabledBackground)
        ) = JadeFilledButtonColors(
            background = background,
            content = content,
            disabledBackground = disabledBackground,
            disabledContent = disabledContent
        )
    }
}

/**
 * A filled button with a solid background. Content auto-shrinks down to [minFontSize] when the
 * label is too wide to fit at the default size.
 */
@Composable
fun JadeFilledButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: JadeButtonSize = JadeButtonSize.Large,
    colors: JadeFilledButtonColors = JadeFilledButtonColors.default(),
    enabled: Boolean,
    minFontSize: TextUnit,
    interactionSource: MutableInteractionSource? = null,
) {
    ButtonBase(
        text = text,
        onClick = onClick,
        enabled = enabled,
        size = size,
        type = ButtonType.Filled,
        enabledColors = ButtonColors(
            background = colors.background,
            content = colors.content,
            border = null
        ),
        disabledColors = ButtonColors(
            background = colors.disabledBackground,
            content = colors.disabledContent,
            border = null
        ),
        minFontSize = minFontSize,
        modifier = modifier,
        interactionSource = interactionSource
    )
}
