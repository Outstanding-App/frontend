package com.tavro.outstanding.designsystem.components

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.tavro.outstanding.designsystem.theme.LocalJadeContentColor
import com.tavro.outstanding.designsystem.theme.LocalJadeTextStyle

/**
 * Jade text component built on [BasicText]. Resolves color from [color] -> ambient
 * [LocalJadeTextStyle] -> [LocalJadeContentColor] and merges all style overrides into the ambient
 * text style so callers only need to specify what differs.
 */
@Composable
fun JadeText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    fontStyle: FontStyle? = null,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1,
    onTextLayout: ((TextLayoutResult) -> Unit)? = null,
    style: TextStyle = LocalJadeTextStyle.current,
    autoSizeText: Boolean = false,
    minFontSize: TextUnit = TextUnit.Unspecified,
) {
    val textColor = color.takeOrElse { style.color.takeOrElse { LocalJadeContentColor.current } }
    // TODO(010): autoSizeText

    BasicText(
        text = text,
        modifier = modifier,
        style = style.merge(
            color = textColor,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign ?: TextAlign.Unspecified,
            lineHeight = lineHeight,
            fontFamily = fontFamily,
            textDecoration = textDecoration,
            fontStyle = fontStyle,
            letterSpacing = letterSpacing
        ),
        onTextLayout = onTextLayout,
        overflow = resolveTextOverflow(
            textOverflow = overflow,
            autoSizeText = autoSizeText
        ),
        softWrap = softWrap,
        maxLines = maxLines,
        minLines = minLines,
        // TODO(010): autoSize
    )
}

// TODO(011): Add AnnotatedString/inlineContent version of JadeText

internal expect fun resolveTextOverflow(
    textOverflow: TextOverflow,
    autoSizeText: Boolean,
): TextOverflow
