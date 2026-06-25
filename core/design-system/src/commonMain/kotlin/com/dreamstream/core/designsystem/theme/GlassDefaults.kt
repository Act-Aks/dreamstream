package com.dreamstream.core.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.chrisbanes.haze.HazeStyle
import dev.chrisbanes.haze.HazeTint

/**
 * Token system for glassmorphic surfaces in DreamStream.
 *
 * [GlassStyle] encodes the blur radius, background fill alpha, border rim
 * alpha, and primary tint strength for a glass surface. Pick the level that
 * matches the visual weight of the element:
 *
 * | Level       | Typical use                              |
 * |-------------|------------------------------------------|
 * | ultraThin   | System bars, floating tooltips           |
 * | thin        | Filter chips, tags, FAB overlays         |
 * | regular     | Content cards, drawer headers            |
 * | thick       | Hero panels, modal headers               |
 * | ultraThick  | Full-screen sheets, onboarding overlays  |
 *
 * Convert any [GlassStyle] to a Haze [HazeStyle] with [GlassStyle.toHazeStyle].
 * This keeps Haze as an implementation detail — feature code only knows about
 * [GlassDefaults] and [GlassStyle].
 */
@Immutable
data class GlassStyle(
    /** Radius passed to the Haze blur engine. */
    val blurRadius: Dp,
    /** White overlay alpha for the frosted-glass fill (0..1). */
    val backgroundAlpha: Float,
    /** White rim-light border alpha (0..1). */
    val borderAlpha: Float,
    /** Width of the glass border stroke. */
    val borderWidth: Dp,
    /** Primary-violet overlay alpha applied on top of the blur (0..1). */
    val primaryTintAlpha: Float,
    /** Noise grain intensity (0..1). Adds subtle texture to the glass. */
    val noiseFactor: Float,
)

/**
 * Predefined [GlassStyle] levels. Import and use these by name from glass
 * components rather than constructing styles inline.
 */
object GlassDefaults {

    /**
     * Barely-there glass — system bars, tooltips.
     */
    val ultraThin = GlassStyle(
        blurRadius       = 16.dp,
        backgroundAlpha  = 0.05f,
        borderAlpha      = 0.10f,
        borderWidth      = 0.5.dp,
        primaryTintAlpha = 0.04f,
        noiseFactor      = 0.08f,
    )

    /**
     * Subtle glass — floating chips, tags.
     */
    val thin = GlassStyle(
        blurRadius       = 20.dp,
        backgroundAlpha  = 0.08f,
        borderAlpha      = 0.14f,
        borderWidth      = 0.75.dp,
        primaryTintAlpha = 0.06f,
        noiseFactor      = 0.10f,
    )

    /**
     * Standard glass — cards, bottom-sheet headers.
     */
    val regular = GlassStyle(
        blurRadius       = 24.dp,
        backgroundAlpha  = 0.12f,
        borderAlpha      = 0.18f,
        borderWidth      = 1.dp,
        primaryTintAlpha = 0.08f,
        noiseFactor      = 0.12f,
    )

    /**
     * Prominent glass — hero panels, modal surfaces.
     */
    val thick = GlassStyle(
        blurRadius       = 32.dp,
        backgroundAlpha  = 0.16f,
        borderAlpha      = 0.24f,
        borderWidth      = 1.dp,
        primaryTintAlpha = 0.10f,
        noiseFactor      = 0.15f,
    )

    /**
     * Maximum glass — full-screen sheets, onboarding overlays.
     */
    val ultraThick = GlassStyle(
        blurRadius       = 40.dp,
        backgroundAlpha  = 0.20f,
        borderAlpha      = 0.30f,
        borderWidth      = 1.5.dp,
        primaryTintAlpha = 0.12f,
        noiseFactor      = 0.18f,
    )
}

/**
 * Converts a [GlassStyle] to a Haze [HazeStyle].
 *
 * Two tints are layered:
 * 1. A white base tint — creates the frosted brightness.
 * 2. A violet brand tint — adds a faint DreamStream signature hue.
 *
 * [fallbackBackground] doubles as the solid fallback on devices where
 * `RenderEffect` blur is unavailable (Android < 12 / API < 31).
 */
fun GlassStyle.toHazeStyle(
    fallbackBackground: Color = DreamStreamPalette.AmbientBase,
): HazeStyle = HazeStyle(
    blurRadius      = blurRadius,
    backgroundColor = fallbackBackground.copy(alpha = backgroundAlpha),
    tints           = listOf(
        HazeTint(color = DreamStreamPalette.GlassWhiteTint.copy(alpha = borderAlpha)),
        HazeTint(color = DreamStreamPalette.Primary.copy(alpha = primaryTintAlpha)),
    ),
    noiseFactor     = noiseFactor,
)
