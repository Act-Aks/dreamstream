package com.dreamstream.core.designsystem.components.glass

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import dev.chrisbanes.haze.HazeState

/**
 * Glossy, gradient-like shimmer brush for skeleton placeholders.
 *
 * Uses [DreamStreamTheme.colors.shimmerBase] and
 * [DreamStreamTheme.colors.shimmerHighlight] and adds a diagonal movement to
 * emulate light reflections on glass.
 */
@Composable
fun glossyShimmerBrush(
    showShimmer: Boolean = true,
    travelDistance: Float = 1000f,
): Brush {
    if (!showShimmer) {
        return Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent),
            start = Offset.Zero,
            end = Offset.Zero,
        )
    }

    val base = DreamStreamTheme.colors.shimmerBase
    val highlight = DreamStreamTheme.colors.shimmerHighlight

    val colors = listOf(
        base.copy(alpha = 0.7f),
        base.copy(alpha = 0.9f),
        highlight.copy(alpha = 1.0f),
        base.copy(alpha = 0.9f),
        base.copy(alpha = 0.7f),
    )

    val transition = rememberInfiniteTransition(label = "glossy_shimmer_transition")
    val offset by transition.animateFloat(
        initialValue = -travelDistance,
        targetValue = travelDistance,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1200,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "glossy_shimmer_offset",
    )

    // Slight diagonal movement for a more liquid, glassy feel
    return Brush.linearGradient(
        colors = colors,
        start = Offset(x = offset, y = 0f),
        end = Offset(x = offset + travelDistance, y = travelDistance / 2f),
    )
}

/**
 * Modifier that applies a glossy, gradient-like shimmer background.
 *
 * Use this on skeleton shapes (Boxes, rows, etc.) to show loading state.
 */
fun Modifier.glossyShimmer(
    enabled: Boolean = true,
    travelDistance: Float = 1000f,
    shape: Shape? = null,
): Modifier = composed {
    val brush = glossyShimmerBrush(
        showShimmer = enabled,
        travelDistance = travelDistance,
    )
    val withBackground = background(brush)
    if (shape != null) withBackground.clip(shape) else withBackground
}

/**
 * Convenience glossy shimmer box for simple skeleton placeholders.
 *
 * @param modifier optional modifier for the shimmer box.
 */
@Composable
fun GlossyShimmerBox(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.glossyShimmer())
}

// ─────────────────────────────────────────────────────────────────────────────
// Basic shimmer previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = true, widthDp = 360, heightDp = 120)
@Composable
private fun GlossyShimmerBoxDarkPreview() {
    DreamStreamTheme(darkTheme = true) {
        GlossyShimmerBox(
            modifier = Modifier.fillMaxWidth().height(80.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 120)
@Composable
private fun GlossyShimmerBoxLightPreview() {
    DreamStreamTheme(darkTheme = false) {
        GlossyShimmerBox(
            modifier = Modifier.fillMaxWidth().height(80.dp),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Example: shimmer layout inside a glass card
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 260)
@Composable
private fun GlassCardGlossyShimmerPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }

        // Assumes you have GradientBackground(hazeState, modifier, content)
        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassCard(
                hazeState = hazeState,
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(24.dp).height(140.dp),
            ) {
                Column {
                    GlossyShimmerBox(
                        modifier = Modifier.fillMaxWidth(0.6f).height(20.dp),
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    GlossyShimmerBox(
                        modifier = Modifier.fillMaxWidth().height(14.dp),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    GlossyShimmerBox(
                        modifier = Modifier.fillMaxWidth(0.8f).height(14.dp),
                    )
                }
            }
        }
    }
}
