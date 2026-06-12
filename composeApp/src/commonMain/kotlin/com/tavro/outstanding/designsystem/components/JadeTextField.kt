package com.tavro.outstanding.designsystem.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.Paragraph
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.resolveDefaults
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastRoundToInt
import com.tavro.outstanding.Space
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.theme.LocalJadeContentColor
import com.tavro.outstanding.designsystem.theme.LocalJadeTextStyle
import kotlin.math.ceil
import kotlin.math.max

expect val isComposeVisualTransformationSupported: Boolean

fun visualTransformationIfSupported(transformation: VisualTransformation) =
    if (isComposeVisualTransformationSupported) transformation
    else VisualTransformation.None

internal object TextFieldDefaults {
    const val DEFAULT_AUTO_SIZE_TEXT = false
    const val DEFAULT_MIN_LINES = 1
    val defaultMinWidth = 280.dp
    val defaultKeyboardActions = KeyboardActions.Default
    val defaultKeyboardOptions = KeyboardOptions.Default
    val defaultVisualTransformation = VisualTransformation.None
}

@Composable
internal fun JadeTextField(
    size: TextFieldSize,
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    supportingText: JadeSupportingText?,
    placeholder: String?,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean,
    maxLines: Int,
    minLines: Int,
    visualTransformation: VisualTransformation,
    interactionSource: MutableInteractionSource?,
    readOnly: Boolean,
    focusRequester: FocusRequester?,
    autoSizeText: Boolean,
    modifier: Modifier = Modifier,
    surfaceColor: Color = JadeTheme.colorScheme.surfaceContainer,
) {
    CompositionLocalProvider(LocalJadeTextStyle provides size.textStyle()) {
        TextFieldScaffold(
            size = size,
            singleLine = singleLine,
            supportingText = supportingText,
            modifier = modifier,
            surfaceColor = surfaceColor
        ) {
            AutoSizeTextScaffold(
                text = text.text,
                style = LocalJadeTextStyle.current,
                enabled = autoSizeText,
            ) { resolvedStyle ->
                BaseTextField(
                    text = text,
                    onTextChange = onTextChange,
                    style = resolvedStyle,
                    size = size,
                    placeholder = placeholder,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    focusRequester = focusRequester,
                    singleLine = singleLine,
                    maxLines = maxLines,
                    minLines = minLines,
                    visualTransformation = visualTransformation,
                    interactionSource = interactionSource,
                    readOnly = readOnly,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

// TODO: Move
/** Retains the last non-null [value] so exit animations can still render with the previous content. */
@Composable
fun <T : Any> rememberLastNonNull(value: T?): T? {
    var state by remember { mutableStateOf(value) }
    if (value != null) state = value
    return state
}

@Composable
private fun TextFieldScaffold(
    size: TextFieldSize,
    singleLine: Boolean,
    supportingText: JadeSupportingText?,
    modifier: Modifier = Modifier,
    surfaceColor: Color = JadeTheme.colorScheme.surfaceContainer,
    content: @Composable BoxScope.() -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Space.xxs),
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        JadeSurface(
            color = surfaceColor,
            shape = size.cornerShape(singleLine = singleLine)
        ) {
            Column(
                modifier = Modifier
                    .defaultMinSize(
                        minHeight = size.minHeight,
                        minWidth = TextFieldDefaults.defaultMinWidth
                    )
                    .fillMaxWidth()
                    .padding(Space.md)
            ) {
                Box(content = content)
            }
        }

        val lastSupportingText = rememberLastNonNull(supportingText)
        AnimatedVisibility(visible = supportingText != null) {
            if (lastSupportingText != null) {
                JadeText(
                    text = lastSupportingText.text,
                    style = JadeTheme.typography.bodyMedium,
                    color = lastSupportingText.color,
                    maxLines = lastSupportingText.maxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = Space.md)
                )
            }
        }
    }
}

@Composable
internal fun BaseTextField(
    text: TextFieldValue,
    onTextChange: (TextFieldValue) -> Unit,
    style: TextStyle,
    size: TextFieldSize,
    placeholder: String?,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    focusRequester: FocusRequester?,
    singleLine: Boolean,
    maxLines: Int,
    minLines: Int,
    visualTransformation: VisualTransformation,
    interactionSource: MutableInteractionSource?,
    readOnly: Boolean,
    modifier: Modifier = Modifier,
) {
    val focusRequesterModifier = focusRequester?.run { Modifier.focusRequester(this) } ?: Modifier
    BasicTextField(
        value = text,
        onValueChange = onTextChange,
        textStyle = style,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        maxLines = maxLines,
        minLines = minLines,
        singleLine = singleLine,
        visualTransformation = visualTransformationIfSupported(visualTransformation),
        interactionSource = interactionSource,
        readOnly = readOnly,
        decorationBox = { innerTextField ->
            TextFieldDecorationBox(
                text = text.text,
                placeholder = placeholder,
                size = size,
                innerTextField = innerTextField
            )
        },
        cursorBrush = getCursorColor(),
        modifier = modifier.then(focusRequesterModifier)
    )
}

@Composable
private fun TextFieldDecorationBox(
    text: String,
    placeholder: String?,
    size: TextFieldSize,
    innerTextField: @Composable () -> Unit,
) {
    innerTextField()
    if (text.isEmpty() && !placeholder.isNullOrEmpty()) {
        BasicText(
            text = placeholder,
            style = size.textStyle(size.placeholderTextColor),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
        )
    }
}

@Composable
@Stable
private fun TextFieldSize.cornerShape(singleLine: Boolean): CornerBasedShape {
    if (!singleLine) return JadeTheme.shapes.lg
    return when (this) {
        TextFieldSize.Normal -> CircleShape
        TextFieldSize.Large -> JadeTheme.shapes.lg
    }
}

@Composable
@Stable
private fun TextFieldSize.textStyle(
    color: Color = LocalJadeContentColor.current,
): TextStyle = when (this) {
    TextFieldSize.Normal -> JadeTheme.typography.bodyLarge
    TextFieldSize.Large -> JadeTheme.typography.headlineLarge
}.copy(color = color)

private val TextFieldSize.placeholderTextColor: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        TextFieldSize.Normal -> JadeTheme.colorScheme.onSurfaceVariant
        TextFieldSize.Large -> JadeTheme.colorScheme.outline
    }

private val TextFieldSize.minHeight: Dp
    get() = when (this) {
        TextFieldSize.Normal -> 56.dp
        TextFieldSize.Large -> 72.dp
    }

@Composable
private fun getCursorColor(): Brush = SolidColor(JadeTheme.colorScheme.onSurface)

internal enum class TextFieldSize {
    Normal,
    Large,
}

sealed interface JadeSupportingText {
    val text: String
    val maxLines: Int

    data class Default(
        override val text: String,
        override val maxLines: Int = Int.MAX_VALUE,
    ) : JadeSupportingText

    data class Error(
        override val text: String,
        override val maxLines: Int = Int.MAX_VALUE,
    ) : JadeSupportingText
}

val JadeSupportingText.color: Color
    @Composable
    @ReadOnlyComposable
    get() = when (this) {
        is JadeSupportingText.Default -> JadeTheme.colorScheme.onSurfaceVariant
        is JadeSupportingText.Error -> JadeTheme.colorScheme.error
    }

// TODO: Move
// Each auto size iteration reduces the font by 10% until the text fits or minFontSize is reached.
private const val TEXT_SCALE_REDUCTION_INTERVAL = 0.9f

// TODO: Move
private fun TextMeasurer.hasVisualOverflow(
    text: String,
    style: TextStyle,
    constraints: Constraints,
): Boolean = measure(
    text = text,
    style = style,
    constraints = constraints,
    maxLines = 1,
    softWrap = false,
    overflow = TextOverflow.Visible
).hasVisualOverflow

// TODO: Move
@Composable
internal inline fun rememberAdjustedFontSize(
    text: String,
    style: TextStyle,
    minFontSize: TextUnit,
    constraints: Constraints,
    enabled: Boolean = true,
): TextUnit {
    val textMeasurer = rememberTextMeasurer()
    return remember(text, style, minFontSize, constraints, enabled) {
        if (!constraints.isZero && enabled) {
            var fontSize = style.fontSize
            while (fontSize >= minFontSize && textMeasurer.hasVisualOverflow(
                    text = text,
                    style = style.copy(fontSize = fontSize),
                    constraints = constraints,
                )
            ) {
                fontSize *= TEXT_SCALE_REDUCTION_INTERVAL
            }

            if (fontSize >= minFontSize) fontSize else minFontSize
        } else {
            style.fontSize
        }
    }
}

@Composable
private fun AutoSizeTextScaffold(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    minFontSize: TextUnit = 12.sp,
    style: TextStyle = LocalJadeTextStyle.current,
    content: @Composable BoxScope.(TextStyle) -> Unit,
) {
    var adjustedTextStyle by remember { mutableStateOf(style) }
    val autoSizeTextModifier = if (enabled) {
        val originalHeight = calculateOriginalHeight(style)
        var constraints by remember { mutableStateOf(Constraints(minWidth = 0, minHeight = 0)) }
        val fontSize = rememberAdjustedFontSize(text, style, minFontSize, constraints)
        LaunchedEffect(fontSize, style) {
            adjustedTextStyle = style.copy(
                fontSize = fontSize,
            )
        }

        Modifier.layout { measurable, c ->
            val minHeight = max(c.minHeight, originalHeight)
            val placeable = measurable.measure(
                constraints = c.copy(
                    minHeight = minHeight,
                    maxHeight = max(c.maxHeight, minHeight),
                )
            )
            if (c.maxHeight != Constraints.Infinity) {
                constraints = Constraints.fixed(placeable.width, placeable.height)
            }
            layout(placeable.width, placeable.height) {
                placeable.place(IntOffset.Zero)
            }
        }
    } else {
        adjustedTextStyle = style
        Modifier
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier then autoSizeTextModifier,
    ) {
        content(adjustedTextStyle)
    }
}

@Composable
private inline fun calculateOriginalHeight(
    style: TextStyle,
): Int {
    val density = LocalDensity.current
    val fontFamilyResolver = LocalFontFamilyResolver.current
    val layoutDirection = LocalLayoutDirection.current
    val resolvedStyle = remember(style, layoutDirection) {
        resolveDefaults(
            style = style,
            direction = layoutDirection,
        )
    }
    val typeface by remember(fontFamilyResolver, resolvedStyle) {
        fontFamilyResolver.resolve(
            fontFamily = resolvedStyle.fontFamily,
            fontWeight = resolvedStyle.fontWeight ?: FontWeight.Normal,
            fontStyle = resolvedStyle.fontStyle ?: FontStyle.Normal,
            fontSynthesis = resolvedStyle.fontSynthesis ?: FontSynthesis.All,
        )
    }
    val minSize = remember {
        AutoSizeTextMinSize(
            layoutDirection = layoutDirection,
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            style = style,
            typeface = typeface,
        )
    }
    minSize.update(layoutDirection, density, fontFamilyResolver, resolvedStyle, typeface)
    return 0
}

private class AutoSizeTextMinSize(
    private var layoutDirection: LayoutDirection,
    private var density: Density,
    private var fontFamilyResolver: FontFamily.Resolver,
    private var style: TextStyle,
    private var typeface: Any,
) {
    var value = computeMinSize()
        private set

    fun update(
        layoutDirection: LayoutDirection,
        density: Density,
        fontFamilyResolver: FontFamily.Resolver,
        resolvedStyle: TextStyle,
        typeface: Any
    ) {
        if (layoutDirection != this.layoutDirection ||
            density != this.density ||
            fontFamilyResolver != this.fontFamilyResolver ||
            resolvedStyle != this.style ||
            typeface != this.typeface
        ) {
            this.layoutDirection = layoutDirection
            this.density = density
            this.fontFamilyResolver = fontFamilyResolver
            this.style = style
            this.typeface = typeface
            value = computeMinSize()
        }
    }

    private fun computeMinSize(): IntSize {
        val paragraph = Paragraph(
            text = EmptyTextReplacement,
            style = style,
            spanStyles = listOf(),
            maxLines = 1,
            overflow = TextOverflow.Visible,
            density = density,
            fontFamilyResolver = fontFamilyResolver,
            constraints = Constraints()
        )
        return IntSize(paragraph.minIntrinsicWidth.ceilToIntPx(), paragraph.height.ceilToIntPx())
    }

    private fun Float.ceilToIntPx(): Int = ceil(this).fastRoundToInt()

    companion object {
        private const val DEFAULT_WIDTH_CHAR_COUNT = 10

        // Placeholder which gives a representative single-line height without measuring real content.
        private val EmptyTextReplacement = "X".repeat(DEFAULT_WIDTH_CHAR_COUNT)
    }
}
