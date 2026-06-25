package com.dreamstream.core.designsystem.components.glass

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.DreamStreamGradients
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.designsystem.theme.GlassGradientTint
import com.dreamstream.core.designsystem.theme.GlassStyle
import com.dreamstream.core.designsystem.theme.toHazeStyle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * Clickable glassmorphic card with blur + gradient tint overlay.
 *
 * For a non-clickable container see [GlassSurface].
 */
@Composable
fun GlassCard(
    hazeState: HazeState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.regular,
    shape: Shape = DreamStreamTheme.shapes.large,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    gradient: Brush = GlassGradientTint,
    content: @Composable BoxScope.() -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier.clip(shape).hazeEffect(
            state = hazeState,
            style = style.toHazeStyle(),
        ),
        shape = shape,
        border = BorderStroke(
            width = style.borderWidth,
            color = Color.White.copy(alpha = style.borderAlpha),
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier.background(gradient).padding(contentPadding),
            content = content,
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 260)
@Composable
private fun GlassCardPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }

        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassCard(
                hazeState = hazeState,
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(24.dp).heightIn(min = 120.dp),
            ) {
                Text(
                    text = "Glass Card",
                    style = DreamStreamTheme.typography.titleMedium,
                    color = Color.White,
                )
                Text(
                    text = "Blur + gradient overlay using DreamStream tokens.",
                    style = DreamStreamTheme.typography.bodyMedium,
                    color = DreamStreamTheme.colors.textSecondary,
                )
            }
        }
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 260)
@Composable
private fun GlassCardBrandGradientPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }

        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassCard(
                hazeState = hazeState,
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(24.dp).heightIn(min = 120.dp),
                gradient = DreamStreamGradients.brandPrimary,
            ) {
                Text(
                    text = "Brand Gradient Card",
                    style = DreamStreamTheme.typography.titleMedium,
                    color = Color.White,
                )
            }
        }
    }
}
