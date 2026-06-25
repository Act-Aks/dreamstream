package com.dreamstream.core.designsystem.components.glass

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeSource

/**
 * Ambient gradient background for DreamStream screens.
 *
 * Paints a deep-space dark base with soft colored glows — making
 * glassmorphic elements placed above visually rich even if blur is unavailable
 * on older devices.
 *
 * When [hazeState] is provided, the background registers as a [hazeSource]
 * so that any [GlassCard] or [GlassSurface] referencing the same state can
 * blur it. Place this as the bottommost layer in a [Box] or [Scaffold]:
 *
 * ```kotlin
 * val hazeState = remember { HazeState() }
 * Box(Modifier.fillMaxSize()) {
 *     GradientBackground(hazeState = hazeState)
 *     Scaffold(containerColor = Color.Transparent) { ... }
 * }
 * ```
 *
 * The [content] slot is provided for convenience when wrapping this in a
 * single-child layout. For multi-layer screens prefer the outer [Box] pattern
 * above instead.
 */
@Composable
fun GradientBackground(
    modifier: Modifier = Modifier,
    hazeState: HazeState? = null,
    content: @Composable BoxScope.() -> Unit = {},
) {
    val baseModifier = modifier
        .fillMaxSize()
        .background(DreamStreamTheme.colors.ambientBase)
        .let { m -> if (hazeState != null) m.hazeSource(hazeState) else m }
    val ambientPurple = DreamStreamTheme.colors.ambientPurple
    val ambientCyan = DreamStreamTheme.colors.ambientCyan
    val ambientPink = DreamStreamTheme.colors.ambientPink

    Box(modifier = baseModifier) {
        // Ambient color glow blobs — painted once, never redrawn during scroll.
        Canvas(modifier = Modifier.fillMaxSize()) {

            // ── Purple glow — upper-left ──────────────────────────────────────
            // The primary brand color radiates from the top-left corner to
            // create depth and contrast against dark content cards.
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ambientPurple,
                        Color.Transparent,
                    ),
                    center = Offset(x = size.width * 0.15f, y = size.height * 0.10f),
                    radius = size.width * 0.60f,
                ),
            )

            // ── Cyan glow — upper-right ───────────────────────────────────────
            // Secondary accent color anchors the eye to the opposite corner.
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ambientCyan,
                        Color.Transparent,
                    ),
                    center = Offset(x = size.width * 0.88f, y = size.height * 0.07f),
                    radius = size.width * 0.45f,
                ),
            )

            // ── Pink glow — lower-right ───────────────────────────────────────
            // Tertiary accent provides warmth to the lower section of the screen
            // and prevents the background from feeling monotone at the bottom.
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        ambientPink,
                        Color.Transparent,
                    ),
                    center = Offset(x = size.width * 0.88f, y = size.height * 0.90f),
                    radius = size.width * 0.50f,
                ),
            )
        }

        content()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun GradientBackgroundPreview() {
    DreamStreamTheme {
        GradientBackground(modifier = Modifier.fillMaxSize())
    }
}
