package com.dreamstream.core.designsystem.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/**
 * Predefined gradient brushes for DreamStream.
 *
 * Consume these by name — never create gradient [Brush]es inline inside
 * composables. If a new gradient is genuinely needed, add it here with a
 * clear doc comment.
 *
 * Brushes in this file use the [DreamStreamPalette] color tokens so they
 * automatically reflect any brand-color updates.
 */
object DreamStreamGradients {

    /**
     * Content scrim — vertical fade applied over thumbnails so that text
     * labels are legible against any image. Run from transparent at the
     * top to near-opaque at the bottom.
     */
    val contentScrim: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color.Transparent,
            0.45f to Color(0x80000000),
            1.00f to Color(0xE0000000),
        ),
    )

    /**
     * Brand primary — electric violet → electric cyan (left → right).
     * Used for CTAs, highlighted badges, and interactive progress indicators.
     */
    val brandPrimary: Brush = Brush.linearGradient(
        colors = listOf(
            DreamStreamPalette.Primary,
            DreamStreamPalette.Secondary,
        ),
    )

    /**
     * Brand accent — electric violet → vibrant pink (left → right).
     * Used for featured content labels, "New" badges, and hero accents.
     */
    val brandAccent: Brush = Brush.linearGradient(
        colors = listOf(
            DreamStreamPalette.Primary,
            DreamStreamPalette.Tertiary,
        ),
    )

    /**
     * Tri-color brand sweep — violet → cyan → pink.
     * Reserved for splash screens, onboarding headers, and hero moments.
     */
    val brandTricolor: Brush = Brush.linearGradient(
        colors = listOf(
            DreamStreamPalette.Primary,
            DreamStreamPalette.Secondary,
            DreamStreamPalette.Tertiary,
        ),
    )

    /**
     * Loading skeleton shimmer — three-stop left-to-right sweep.
     * Animate the horizontal translation offset to create the shimmer effect.
     */
    val shimmer: Brush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF1A1B2E),
            Color(0xFF2D2E48),
            Color(0xFF1A1B2E),
        ),
    )

    /**
     * Radial card glow — violet bloom centered on a card for selection or
     * hover state. Animate alpha from 0 → 1 via `graphicsLayer` to avoid
     * recomposition.
     */
    val cardGlow: Brush = Brush.radialGradient(
        colors = listOf(
            DreamStreamPalette.Primary.copy(alpha = 0.30f),
            Color.Transparent,
        ),
    )
}
