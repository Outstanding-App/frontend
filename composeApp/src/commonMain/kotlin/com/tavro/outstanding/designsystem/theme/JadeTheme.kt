package com.tavro.outstanding.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.structuralEqualityPolicy
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Jade design system color scheme. Inspired by Material 3 color role structure. */
@Immutable
data class JadeColorScheme(
    val isDark: Boolean,
    val primary: Color,
    val onPrimary: Color,
    val primaryContainer: Color,
    val onPrimaryContainer: Color,
    val secondary: Color,
    val onSecondary: Color,
    val secondaryContainer: Color,
    val onSecondaryContainer: Color,
    val background: Color,
    val onBackground: Color,
    val surface: Color,
    val onSurface: Color,
    val surfaceVariant: Color,
    val onSurfaceVariant: Color,
    val error: Color,
    val onError: Color,
    val errorContainer: Color,
    val onErrorContainer: Color,
    val outline: Color,
    val outlineVariant: Color,
    val surfaceContainer: Color,
    val surfaceContainerHigh: Color,
    val surfaceContainerHighest: Color,
    val surfaceContainerLow: Color,
    val surfaceContainerLowest: Color,
    val disabledSurface: Color,
    val onDisabledSurface: Color,
)

internal var LightColors = JadeColorScheme(
    isDark = false,
    primary = Color(0xFFE03E1A),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDAD4),
    onPrimaryContainer = Color(0xFF3B0600),
    secondary = Color(0xFF415F91),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD6E3FF),
    onSecondaryContainer = Color(0xFF001B47),
    background = Color(0xFFFFF8F6),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFFF8F6),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF3D9D6),
    onSurfaceVariant = Color(0xFF52433D),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    outline = Color(0xFF857370),
    outlineVariant = Color(0xFFD6BFBB),
    surfaceContainer = Color(0xFFF0DEDA),
    surfaceContainerHigh = Color(0xFFEAD8D3),
    surfaceContainerHighest = Color(0xFFE4D2CD),
    surfaceContainerLow = Color(0xFFF9E9E6),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    disabledSurface = Color(0xFFE8DCDA),
    onDisabledSurface = Color(0xFF9B8C88),
)

internal var DarkColors = JadeColorScheme(
    isDark = true,
    primary = Color(0xFFFFB4A4),
    onPrimary = Color(0xFF5C1100),
    primaryContainer = Color(0xFF8C1D00),
    onPrimaryContainer = Color(0xFFFFDAD4),
    secondary = Color(0xFFAAC7FF),
    onSecondary = Color(0xFF0A2F60),
    secondaryContainer = Color(0xFF284777),
    onSecondaryContainer = Color(0xFFD6E3FF),
    background = Color(0xFF171210),
    onBackground = Color(0xFFEDE0DB),
    surface = Color(0xFF171210),
    onSurface = Color(0xFFEDE0DB),
    surfaceVariant = Color(0xFF52433D),
    onSurfaceVariant = Color(0xFFD6C3BE),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    outline = Color(0xFF9F8E8A),
    outlineVariant = Color(0xFF52433D),
    surfaceContainer = Color(0xFF271E1A),
    surfaceContainerHigh = Color(0xFF312520),
    surfaceContainerHighest = Color(0xFF3C302A),
    surfaceContainerLow = Color(0xFF201714),
    surfaceContainerLowest = Color(0xFF120E0C),
    disabledSurface = Color(0xFF2A2220),
    onDisabledSurface = Color(0xFF6B5B57),
)

internal var LocalColorScheme = staticCompositionLocalOf { LightColors }
val LocalJadeContentColor = compositionLocalOf { Color.Unspecified }

/** Returns the on-color for [backgroundColor], or [Color.Unspecified] when there is no mapping. */
@Stable
fun JadeColorScheme.contentColorFor(backgroundColor: Color): Color =
    when (backgroundColor) {
        primary -> onPrimary
        secondary -> onSecondary
        background -> onBackground
        error -> onError
        primaryContainer -> onPrimaryContainer
        secondaryContainer -> onSecondaryContainer
        errorContainer -> onErrorContainer
        surface -> onSurface
        surfaceVariant -> onSurfaceVariant
        surfaceContainer -> onSurface
        surfaceContainerHigh -> onSurface
        surfaceContainerHighest -> onSurface
        surfaceContainerLow -> onSurface
        surfaceContainerLowest -> onSurface
        disabledSurface -> onDisabledSurface

        else -> Color.Unspecified
    }

/** Composable variant that falls back to [LocalJadeContentColor] when there is no color mapping. */
@Composable
@ReadOnlyComposable
fun contentColorFor(backgroundColor: Color) =
    JadeTheme.colorScheme.contentColorFor(backgroundColor).takeOrElse {
        LocalJadeContentColor.current
    }

// TODO(012): Add unused token for all colors that are currently marked as Color(0xFF)
fun JadeColorScheme.toM3ColorScheme(): androidx.compose.material3.ColorScheme = androidx.compose.material3.ColorScheme(
    primary = primary,
    onPrimary = onPrimary,
    primaryContainer = primaryContainer,
    onPrimaryContainer = onPrimaryContainer,
    inversePrimary = Color(0xFF),
    secondary = secondary,
    onSecondary = onSecondary,
    secondaryContainer = secondaryContainer,
    onSecondaryContainer = onSecondaryContainer,
    tertiary = Color(0xFF),
    onTertiary = Color(0xFF),
    tertiaryContainer = Color(0xFF),
    onTertiaryContainer = Color(0xFF),
    background = background,
    onBackground = onBackground,
    surface = surface,
    onSurface = onSurface,
    surfaceVariant = surfaceVariant,
    onSurfaceVariant = onSurfaceVariant,
    surfaceTint = Color(0xFF),
    inverseSurface = Color(0xFF),
    inverseOnSurface = Color(0xFF),
    error = error,
    onError = onError,
    errorContainer = errorContainer,
    onErrorContainer = onErrorContainer,
    outline = outline,
    outlineVariant = outlineVariant,
    scrim = Color(0xFF),
    surfaceBright = Color(0xFF),
    surfaceDim = Color(0xFF),
    surfaceContainer = surfaceContainer,
    surfaceContainerHigh = surfaceContainerHigh,
    surfaceContainerHighest = surfaceContainerHighest,
    surfaceContainerLow = surfaceContainerLow,
    surfaceContainerLowest = surfaceContainerLowest
)

@Immutable
data class JadeShapes(
    val xs: CornerBasedShape,
    val sm: CornerBasedShape,
    val md: CornerBasedShape,
    val lg: CornerBasedShape,
    val xl: CornerBasedShape,
    val xxl: CornerBasedShape,
    val full: CornerBasedShape,
    val circle: CornerBasedShape,
)

private val shapes = JadeShapes(
    xs = RoundedCornerShape(4.dp),
    sm = RoundedCornerShape(8.dp),
    md = RoundedCornerShape(12.dp),
    lg = RoundedCornerShape(16.dp),
    xl = RoundedCornerShape(24.dp),
    xxl = RoundedCornerShape(28.dp),
    full = RoundedCornerShape(100.dp),
    circle = CircleShape,
)

internal val LocalShapes = staticCompositionLocalOf { shapes }

fun JadeShapes.toM3Shapes() = androidx.compose.material3.Shapes(
    extraSmall = xs,
    small = sm,
    medium = md,
    large = lg,
    extraLarge = xxl
)

/**
 * Center alignment + Trim.None ensures consistent single line height across platforms and prevents
 * text from being clipped at the top or bottom when a custom line height is applied.
 */
internal val defaultTextStyle = TextStyle.Default.copy(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

/**
 * Jade type scale. Inspired by the Material 3 type scale:
 * https://m3.material.io/styles/typography/type-scale-tokens
 */
@Immutable
data class JadeTypography(
    val displayExtraLarge: TextStyle,
    val displayLarge: TextStyle,
    val displayMedium: TextStyle,
    val displaySmall: TextStyle,
    val headlineLarge: TextStyle,
    val headlineMedium: TextStyle,
    val headlineSmall: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle,
    val bodyExtraSmall: TextStyle,
    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle,
)

private val typography = JadeTypography(
    displayExtraLarge = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 76.sp,
        lineHeight = 88.sp,
    ),
    displayLarge = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 52.sp,
        lineHeight = 62.sp,
    ),
    displayMedium = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 43.sp,
        lineHeight = 52.sp,
    ),
    displaySmall = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
    ),
    headlineLarge = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    ),
    headlineMedium = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    ),
    headlineSmall = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    titleSmall = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 14.sp,
    ),
    bodyLarge = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    bodySmall = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    bodyExtraSmall = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 14.sp,
    ),
    labelLarge = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    ),
    labelMedium = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = defaultTextStyle.copy(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)

internal val LocalTypography = staticCompositionLocalOf { typography }
val LocalJadeTextStyle = compositionLocalOf(structuralEqualityPolicy()) { defaultTextStyle }

fun JadeTypography.toM3Typography() = androidx.compose.material3.Typography(
    displayLarge = displayLarge,
    displayMedium = displayMedium,
    displaySmall = displaySmall,
    headlineLarge = headlineLarge,
    headlineMedium = headlineMedium,
    headlineSmall = headlineSmall,
    titleLarge = titleLarge,
    titleMedium = titleMedium,
    titleSmall = titleSmall,
    bodyLarge = bodyLarge,
    bodyMedium = bodyMedium,
    bodySmall = bodySmall,
    labelLarge = labelLarge,
    labelMedium = labelMedium,
    labelSmall = labelSmall
)

object JadeTheme {
    val colorScheme: JadeColorScheme
        @Composable @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: JadeTypography
        @Composable @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: JadeShapes
        @Composable @ReadOnlyComposable
        get() = LocalShapes.current
}

@Composable
fun ApplyTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    ApplyTheme(
        colorScheme = if (useDarkTheme) DarkColors else LightColors,
        content = content,
    )
}

@Composable
fun ApplyTheme(
    colorScheme: JadeColorScheme = JadeTheme.colorScheme,
    shapes: JadeShapes = JadeTheme.shapes,
    typography: JadeTypography = JadeTheme.typography,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalColorScheme provides colorScheme,
        LocalShapes provides shapes,
        LocalTypography provides typography
    ) {
        content()
    }
}
