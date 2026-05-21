package com.dreamstream.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Dark scheme — DreamStream's default appearance. The product is media-first
 * and intended to be calm against video content.
 */
private val DarkColorScheme: ColorScheme = darkColorScheme(
    primary = DreamStreamPalette.Primary,
    onPrimary = DreamStreamPalette.OnPrimary,
    primaryContainer = DreamStreamPalette.PrimaryContainer,
    onPrimaryContainer = DreamStreamPalette.OnPrimaryContainer,
    secondary = DreamStreamPalette.Secondary,
    onSecondary = DreamStreamPalette.OnSecondary,
    secondaryContainer = DreamStreamPalette.SecondaryContainer,
    onSecondaryContainer = DreamStreamPalette.OnSecondaryContainer,
    tertiary = DreamStreamPalette.Tertiary,
    onTertiary = DreamStreamPalette.OnTertiary,
    tertiaryContainer = DreamStreamPalette.TertiaryContainer,
    onTertiaryContainer = DreamStreamPalette.OnTertiaryContainer,
    background = DreamStreamPalette.Background,
    onBackground = DreamStreamPalette.OnBackground,
    surface = DreamStreamPalette.Surface,
    onSurface = DreamStreamPalette.OnSurface,
    surfaceVariant = DreamStreamPalette.SurfaceVariant,
    onSurfaceVariant = DreamStreamPalette.OnSurfaceVariant,
    outline = DreamStreamPalette.Outline,
    outlineVariant = DreamStreamPalette.OutlineVariant,
    error = DreamStreamPalette.Error,
    onError = DreamStreamPalette.OnError,
    errorContainer = DreamStreamPalette.ErrorContainer,
    onErrorContainer = DreamStreamPalette.OnErrorContainer,
)

/**
 * Light scheme — offered for completeness; the app's chrome should still
 * default to dark. Tune separately from the dark palette rather than mirroring.
 */
private val LightColorScheme: ColorScheme = lightColorScheme(
    primary = DreamStreamPalette.Primary,
    secondary = DreamStreamPalette.Secondary,
    tertiary = DreamStreamPalette.Tertiary,
    background = DreamStreamPalette.LightBackground,
    onBackground = DreamStreamPalette.LightOnBackground,
    surface = DreamStreamPalette.LightSurface,
    onSurface = DreamStreamPalette.LightOnSurface,
    surfaceVariant = DreamStreamPalette.LightSurfaceVariant,
    onSurfaceVariant = DreamStreamPalette.LightOnSurfaceVariant,
)

/**
 * Root theme entry point.
 *
 * Wrap every Compose hierarchy in [DreamStreamTheme] — including previews — so
 * tokens, typography, and shapes stay consistent. Avoid wrapping in
 * `MaterialTheme` directly; always go through this composable.
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
    MaterialTheme(
        colorScheme = colorScheme,
        typography = DreamStreamTypography,
        shapes = DreamStreamShapes,
        content = content,
    )
}
