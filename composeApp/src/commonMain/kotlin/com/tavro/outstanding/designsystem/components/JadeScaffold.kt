package com.tavro.outstanding.designsystem.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.MutableWindowInsets
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.onConsumedWindowInsetsChanged
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.offset
import androidx.compose.ui.util.fastForEach
import androidx.compose.ui.util.fastMap
import androidx.compose.ui.util.fastMaxBy
import com.tavro.outstanding.designsystem.theme.JadeTheme
import com.tavro.outstanding.designsystem.theme.contentColorFor

/**
 * Jade scaffold that slots a [topBar], [bottomBar], [snackbarHost] and main [content].
 * Layout logic mirrors the Material 3 Scaffold.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JadeScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    containerColor: Color = JadeTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    val safeInsets = remember(contentWindowInsets) { MutableWindowInsets(contentWindowInsets) }
    JadeSurface(
        modifier = modifier
            .onConsumedWindowInsetsChanged { consumedWindowInsets ->
                safeInsets.insets = contentWindowInsets.exclude(consumedWindowInsets)
            },
        color = containerColor,
        contentColor = contentColor
    ) {
        ScaffoldLayout(
            topBar = topBar,
            bottomBar = bottomBar,
            content = content,
            snackbar = snackbarHost,
            contentWindowInsets = safeInsets,
        )
    }
}

@Composable
private fun ScaffoldLayout(
    topBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit,
    snackbar: @Composable () -> Unit,
    contentWindowInsets: WindowInsets,
    bottomBar: @Composable () -> Unit,
) {
    SubcomposeLayout { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val topBarPlaceables =
            subcompose(ScaffoldLayoutContent.TopBar, topBar).fastMap {
                it.measure(looseConstraints)
            }
        val topBarHeight = topBarPlaceables.fastMaxBy { it.height }?.height ?: 0

        val snackbarPlaceables =
            subcompose(ScaffoldLayoutContent.Snackbar, snackbar).fastMap {
                val leftInset = contentWindowInsets.getLeft(this@SubcomposeLayout, layoutDirection)
                val rightInset =
                    contentWindowInsets.getRight(this@SubcomposeLayout, layoutDirection)
                val bottomInsets = contentWindowInsets.getBottom(this@SubcomposeLayout)
                it.measure(looseConstraints.offset(-leftInset - rightInset, -bottomInsets))
            }
        val snackbarHeight = snackbarPlaceables.fastMaxBy { it.height }?.height ?: 0
        val snackbarWidth = snackbarPlaceables.fastMaxBy { it.width }?.width ?: 0

        val bottomBarPlaceables = subcompose(ScaffoldLayoutContent.BottomBar) {
            Box(
                modifier = Modifier
                    .windowInsetsPadding(contentWindowInsets.only(WindowInsetsSides.Bottom))
            ) {
                bottomBar()
            }
        }.fastMap { it.measure(looseConstraints) }
        val bottomBarHeight = bottomBarPlaceables.fastMaxBy { it.height }?.height

        val snackbarOffsetFromBottom = if (snackbarHeight != 0) {
            snackbarHeight + (bottomBarHeight ?: contentWindowInsets.getBottom(this@SubcomposeLayout))
        } else {
            0
        }

        val bodyContentPlaceables = subcompose(ScaffoldLayoutContent.MainContent) {
            val insets = contentWindowInsets.asPaddingValues(this@SubcomposeLayout)
            val innerPadding =
                PaddingValues(
                    top = if (topBarPlaceables.isEmpty()) {
                        insets.calculateTopPadding()
                    } else {
                        topBarHeight.toDp()
                    },
                    bottom = if (bottomBarPlaceables.isEmpty() || bottomBarHeight == null) {
                        insets.calculateBottomPadding()
                    } else {
                        bottomBarHeight.toDp()
                    },
                    start = insets.calculateStartPadding(
                        (this@SubcomposeLayout).layoutDirection
                    ),
                    end = insets.calculateEndPadding((this@SubcomposeLayout).layoutDirection),
                )
            content(innerPadding)
        }.fastMap { it.measure(looseConstraints) }

        layout(layoutWidth, layoutHeight) {
            bodyContentPlaceables.fastForEach { it.place(0, 0) }
            topBarPlaceables.fastForEach { it.place(0, 0) }
            val leftInset = contentWindowInsets.getLeft(this@SubcomposeLayout, layoutDirection)
            snackbarPlaceables.fastForEach {
                it.place(
                    (layoutWidth - snackbarWidth) / 2 + leftInset,
                    layoutHeight - snackbarOffsetFromBottom
                )
            }
            bottomBarPlaceables.fastForEach { it.place(0, layoutHeight - (bottomBarHeight ?: 0)) }
        }
    }
}

object ScaffoldDefaults {
    val contentWindowInsets: WindowInsets
        @Composable get() = WindowInsets.navigationBars
}

private enum class ScaffoldLayoutContent {
    TopBar,
    MainContent,
    Snackbar,
    BottomBar,
}
