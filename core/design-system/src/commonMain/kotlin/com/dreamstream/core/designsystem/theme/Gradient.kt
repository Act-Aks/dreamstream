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
 *
 * Groups:
 * - **Brand** — signature violet/cyan/pink sweeps for CTAs and badges.
 * - **Hero scrims** — overlays that fade poster art into the background so
 *   text stays legible. Use these for featured banners, poster cards, and
 *   hero sections.
 * - **Utility** — shimmer for skeleton screens, card glow for hover states.
 */
object DreamStreamGradients {

    // ── Brand ─────────────────────────────────────────────────────────────────

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

    // ── Hero scrims ───────────────────────────────────────────────────────────

    /**
     * Bottom-up hero scrim — transparent → screen background color.
     *
     * Primary scrim for featured content banners. Layer this over a poster
     * image so that title text placed below the image remains legible against
     * the screen background. The opacity ramp is gradual to avoid a harsh
     * color cut mid-image.
     *
     * ```
     * Box {
     *     AsyncImage(...)          // poster fills the Box
     *     Box(Modifier.matchParentSize().background(DreamStreamGradients.heroScrimBottomUp))
     *     Text("Title", ...)       // rendered over the scrim
     * }
     * ```
     */
    val heroScrimBottomUp: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color.Transparent,
            0.35f to DreamStreamPalette.Background.copy(alpha = 0.20f),
            0.65f to DreamStreamPalette.Background.copy(alpha = 0.70f),
            0.88f to DreamStreamPalette.Background.copy(alpha = 0.92f),
            1.00f to DreamStreamPalette.Background,
        ),
    )

    /**
     * Top-down hero scrim — screen background → transparent.
     *
     * Use at the top of a hero banner to blend the status bar / top app bar
     * area into the poster art, making the bar feel integrated rather than
     * floating.
     */
    val heroScrimTopDown: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to DreamStreamPalette.Background.copy(alpha = 0.80f),
            0.40f to DreamStreamPalette.Background.copy(alpha = 0.30f),
            1.00f to Color.Transparent,
        ),
    )

    /**
     * Poster scrim — full-height overlay with a transparency hold in the
     * upper half and a solid fade in the lower half.
     *
     * Keeps the centre of a poster image clear (good for artwork) while
     * ensuring the bottom is fully opaque for metadata text. Use on portrait-
     * oriented content cards where the title sits at the bottom of the image.
     */
    val posterScrim: Brush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.00f to Color.Transparent,
            0.45f to Color.Transparent,
            0.72f to DreamStreamPalette.Background.copy(alpha = 0.60f),
            1.00f to DreamStreamPalette.Background,
        ),
    )

    /**
     * Side scrim — screen background → transparent (left → right).
     *
     * Use in horizontal hero layouts where the content description is placed
     * on the left and the poster art extends to the right. Fades the left edge
     * into the background so text is always legible.
     */
    val heroScrimSide: Brush = Brush.horizontalGradient(
        colorStops = arrayOf(
            0.00f to DreamStreamPalette.Background,
            0.45f to DreamStreamPalette.Background.copy(alpha = 0.85f),
            0.75f to DreamStreamPalette.Background.copy(alpha = 0.30f),
            1.00f to Color.Transparent,
        ),
    )

    // ── Utility ───────────────────────────────────────────────────────────────

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
