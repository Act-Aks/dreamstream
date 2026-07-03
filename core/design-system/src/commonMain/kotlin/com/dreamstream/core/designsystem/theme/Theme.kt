package com.dreamstream.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable


/**
 * Dark scheme — DreamStream's default appearance. The product is media-first
 * and intended to be calm against video content.
 */
private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = PurplePrimary,
    onPrimary = PurpleOnPrimary,
    primaryContainer = PurplePrimaryContainer,
    onPrimaryContainer = PurpleOnPrimaryContainer,
    secondary = CyanSecondary,
    onSecondary = CyanOnSecondary,
    secondaryContainer = CyanSecondaryContainer,
    onSecondaryContainer = CyanOnSecondaryContainer,
    tertiary = PinkTertiary,
    onTertiary = PinkOnTertiary,
    tertiaryContainer = PinkTertiaryContainer,
    onTertiaryContainer = PinkOnTertiaryContainer,
    background = BackgroundDark,
    onBackground = OnBackgroundDark,
    surface = SurfaceDark,
    onSurface = OnSurfaceDark,
    surfaceVariant = SurfaceVariantDark,
    onSurfaceVariant = OnSurfaceVariantDark,
    outline = OutlineDark,
    outlineVariant = OutlineVariantDark,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
)

/**
 * Light scheme — offered for completeness; the app's chrome should still
 * default to dark. Tune separately from the dark palette rather than mirroring.
 */
private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = PurplePrimary,
    onPrimary = PurpleOnPrimary,
    primaryContainer = PurplePrimaryContainer,
    onPrimaryContainer = PurpleOnPrimaryContainer,
    secondary = CyanSecondary,
    onSecondary = CyanOnSecondary,
    secondaryContainer = CyanSecondaryContainer,
    onSecondaryContainer = CyanOnSecondaryContainer,
    tertiary = PinkTertiary,
    onTertiary = PinkOnTertiary,
    tertiaryContainer = PinkTertiaryContainer,
    onTertiaryContainer = PinkOnTertiaryContainer,
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    surface = SurfaceLight,
    onSurface = OnSurfaceLight,
    surfaceVariant = SurfaceVariantLight,
    onSurfaceVariant = OnSurfaceVariantLight,
    outline = OutlineLight,
    outlineVariant = OutlineVariantLight,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
)

/**
 * Single shared instance of [DreamStreamExtendedColors] provided by the theme.
 */
private val DarkExtendedColors = DreamStreamExtendedColors(
    textPrimary = TextPrimaryDark,
    textSecondary = TextSecondaryDark,
    textTertiary = TextTertiaryDark,
    textDisabled = TextDisabledDark,
    textPlaceholder = TextPlaceholderDark,
    shimmerBase = ShimmerBaseDark,
    shimmerHighlight = ShimmerHighlightDark,
    qualityCam = TextTertiaryDark,
)

/**
 * Light-mode extended colors. These are tuned independently of the dark
 * palette and should feel softer, brighter, and less saturated.
 */
private val LightExtendedColors = DreamStreamExtendedColors(
    textPrimary = TextPrimaryLight,
    textSecondary = TextSecondaryLight,
    textTertiary = TextTertiaryLight,
    textDisabled = TextDisabledLight,
    textPlaceholder = TextPlaceholderLight,
    shimmerBase = ShimmerBaseLight,
    shimmerHighlight = ShimmerHighlightLight,
    qualityCam = TextTertiaryLight,
)

/**
 * Root theme entry point.
 *
 * Wrap every Compose hierarchy in [DreamStreamTheme] — including previews — so
 * tokens, typography, and shapes stay consistent. Avoid wrapping in
 * `MaterialTheme` directly; always go through this composable.
 *
 * Provides both [MaterialTheme] (colors, typography, shapes) and
 * [LocalDreamStreamColors] (extended tokens not covered by M3).
 * Access extended colors via [DreamStreamTheme.colors].
 *
 * @param darkTheme whether to render the dark color scheme (default: true,
 *   the product's intended look).
 */
@Composable
fun DreamStreamTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val extendedColors = if (darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(
        LocalDreamStreamMetrics provides DreamStreamMetrics(),
        LocalDreamStreamColors provides extendedColors,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = DreamStreamTypography,
            shapes = DreamStreamShapes,
            content = content,
        )
    }
}

/**
 * Convenient accessors for DreamStream theme values inside composables.
 *
 * Mirrors the pattern used by [MaterialTheme] in the Compose source — a
 * top-level function and a top-level object can share the same name in Kotlin.
 *
 * ```kotlin
 * // Extended (non-M3) tokens:
 * val colors = DreamStreamTheme.colors
 * Text(color = colors.textSecondary, ...)
 *
 * // Standard M3 tokens (same as MaterialTheme.colorScheme):
 * val scheme = DreamStreamTheme.materialColors
 * Text(color = scheme.primary, ...)
 * ```
 */
object DreamStreamTheme {
    /** DreamStream extended color tokens (text, nav, glass tints, semantic, shimmer). */
    val colors: DreamStreamExtendedColors
        @Composable @ReadOnlyComposable get() = LocalDreamStreamColors.current

    /** Local spacing. */
    val metrics: DreamStreamMetrics
        @Composable @ReadOnlyComposable get() = LocalDreamStreamMetrics.current

    /** Material3 color scheme — equivalent to [MaterialTheme.colorScheme]. */
    val materialColors: ColorScheme
        @Composable @ReadOnlyComposable get() = MaterialTheme.colorScheme

    /** Material3 shapes — equivalent to [MaterialTheme.shapes]. */
    val shapes: Shapes
        @Composable @ReadOnlyComposable get() = MaterialTheme.shapes

    /** Material3 typography — equivalent to [MaterialTheme.typography]. */
    val typography: Typography
        @Composable @ReadOnlyComposable get() = MaterialTheme.typography
}
