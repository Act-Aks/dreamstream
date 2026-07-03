package com.dreamstream.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val zero: Dp = 0.dp,
    val xxs: Dp = 4.dp,
    val xs: Dp = 8.dp,
    val sm: Dp = 12.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 20.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 28.dp,
    val xxxl: Dp = 32.dp
)

@Immutable
data class Padding(
    val screenPadding: Dp = 16.dp,
    val cardPadding: Dp = 16.dp,
    val inlinePadding: Dp = 12.dp,
    val listPadding: Dp = 12.dp
)

@Immutable
data class Gap(
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp
)

@Immutable
data class Radius(
    val small: Dp = 8.dp,
    val medium: Dp = 16.dp,
    val large: Dp = 24.dp,
    val infinite: Dp = 999.dp
)

@Immutable
data class BorderWidth(
    val small: Dp = 0.5.dp,
    val medium: Dp = 1.dp,
    val large: Dp = 1.5.dp,
    val thick: Dp = 2.dp
)

@Immutable
data class DreamStreamMetrics(
    val borderWidth: BorderWidth = BorderWidth(),
    val gap: Gap = Gap(),
    val padding: Padding = Padding(),
    val radius: Radius = Radius(),
    val spacing: Spacing = Spacing()
)

/**
 * CompositionLocal that carries [DreamStreamMetrics] down the tree.
 * Provided by [DreamStreamTheme]; throws a clear error if accessed outside it.
 */
internal val LocalDreamStreamMetrics = staticCompositionLocalOf<DreamStreamMetrics> {
    error(
        "DreamStreamMetrics not provided. " + "Wrap your content in DreamStreamTheme { }.",
    )
}
