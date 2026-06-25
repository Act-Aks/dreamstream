package com.dreamstream.core.designsystem.components.glass

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.designsystem.theme.GlassGradientTint
import com.dreamstream.core.designsystem.theme.GlassStyle
import com.dreamstream.core.designsystem.theme.toHazeStyle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * Non-clickable glassmorphic surface with blur + gradient tint overlay.
 *
 * Use this for:
 * - Top bars
 * - Bottom navigation bars
 * - Panels, chips, overlays
 *
 * @param hazeState Haze state used to compute the backdrop blur.
 * @param modifier optional modifier for the surface.
 * @param style [GlassStyle] level controlling blur, border, tint.
 * @param shape corner shape for the surface.
 * @param contentPadding inner padding applied after gradient.
 * @param gradient gradient tint painted on top of the blur.
 * @param content content composable drawn inside the surface.
 */
@Composable
fun GlassSurface(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.regular,
    shape: Shape = MaterialTheme.shapes.medium,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    gradient: Brush = GlassGradientTint,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier.clip(shape).hazeEffect(
            state = hazeState,
            style = style.toHazeStyle(),
        ),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(
            width = style.borderWidth,
            color = Color.White.copy(alpha = style.borderAlpha),
        ),
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
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

@PreviewLightDark()
@Composable
private fun GlassSurfacePreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }

        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassSurface(
                hazeState = hazeState,
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                style = GlassDefaults.regular,
            ) {
                Text(
                    text = "Glass Surface",
                    style = DreamStreamTheme.typography.titleMedium,
                    color = DreamStreamTheme.colors.textPrimary,
                )
            }
        }
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 200)
@Composable
private fun GlassSurfaceThinPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }

        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassSurface(
                hazeState = hazeState,
                modifier = Modifier.fillMaxWidth().padding(24.dp),
                style = GlassDefaults.thin,
                contentPadding = PaddingValues(12.dp),
            ) {
                Text(
                    text = "Thin Glass Surface",
                    style = DreamStreamTheme.typography.bodyMedium,
                    color = DreamStreamTheme.colors.textPrimary,
                )
            }
        }
    }
}
