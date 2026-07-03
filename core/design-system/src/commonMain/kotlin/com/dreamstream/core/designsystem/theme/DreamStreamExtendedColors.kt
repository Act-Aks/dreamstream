package com.dreamstream.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * DreamStream-specific colors that have no slot in the Material3 color scheme.
 *
 * Access via [DreamStreamTheme.colors] inside any composable that is wrapped
 * in [DreamStreamTheme]. Never instantiate this class directly in production
 * code — use the singleton wired up by the theme.
 *
 * Groups:
 * - **Glass tints** — translucent fills for non-Haze surfaces (nav bar, chips,
 *   overlays) where backdrop blur is not required.
 * - **Text hierarchy** — semantic names for the five text emphasis levels;
 *   prefer these over raw [Color.White] or [ColorScheme.onSurface].
 * - **Semantic** — success, warning, and info states. Error is already covered
 *   by [ColorScheme.error].
 * - **Shimmer** — skeleton loading colors for placeholder surfaces.
 */
@Immutable
data class DreamStreamExtendedColors(
    // ── Glass tints ───────────────────────────────────────────────────────────
    val glassThin: Color = GlassThin,
    val glassLight: Color = GlassWhiteTint,
    val glassMedium: Color = GlassMedium,
    val glassStrong: Color = GlassStrong,
    val glassBorder: Color = GlassBorder,
    val glassBorderFocused: Color = GlassBorderFocused,

    // ── Text hierarchy ────────────────────────────────────────────────────────
    val textPrimary: Color,
    val textSecondary: Color,
    val textTertiary: Color,
    val textDisabled: Color,
    val textPlaceholder: Color,

    // ── Semantic ──────────────────────────────────────────────────────────────
    val success: Color = Success,
    val warning: Color = Warning,
    val info: Color = Info,

    // ── Shimmer ───────────────────────────────────────────────────────────────
    val shimmerBase: Color,
    val shimmerHighlight: Color,

    // ── Ambient background ──────────────────────────────────────────────────
    val ambientBase: Color = AmbientBase,
    val ambientPurple: Color = AmbientPurple,
    val ambientCyan: Color = AmbientCyan,
    val ambientPink: Color = AmbientPink,

    // ── Quality badges ───────────────────────────────────────────────────────
    val qualityCam: Color,
    val qualitySd: Color = Info,
    val qualityHd: Color = Success,
    val qualityFhd: Color = PurplePrimary,
    val qualityFourK: Color = Warning,
    val qualityBluRay: Color = PinkTertiary,
)

/**
 * CompositionLocal that carries [DreamStreamExtendedColors] down the tree.
 * Provided by [DreamStreamTheme]; throws a clear error if accessed outside it.
 */
internal val LocalDreamStreamColors = staticCompositionLocalOf<DreamStreamExtendedColors> {
    error(
        "DreamStreamExtendedColors not provided. " + "Wrap your content in DreamStreamTheme { }.",
    )
}
