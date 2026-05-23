package com.dreamstream.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.designsystem.theme.GlassStyle
import com.dreamstream.core.designsystem.theme.toHazeStyle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * Non-clickable glassmorphic surface container.
 *
 * Semantically equivalent to Material's [Surface] but with glassmorphic
 * rendering. Apply [GlassCard] when the container must respond to clicks.
 *
 * ```kotlin
 * GlassSurface(
 *     hazeState = hazeState,
 *     style = GlassDefaults.thick,
 * ) {
 *     Column(modifier = Modifier.padding(16.dp)) { ... }
 * }
 * ```
 */
@Composable
fun GlassSurface(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.regular,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable BoxScope.() -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .hazeEffect(
                state = hazeState,
                style = style.toHazeStyle(),
            ),
        shape = shape,
        color = Color.Transparent,
        border = BorderStroke(
            width = style.borderWidth,
            color = Color.White.copy(alpha = style.borderAlpha),
        ),
    ) {
        Box(content = content)
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 260)
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
                style = GlassDefaults.thick,
                modifier = Modifier.padding(32.dp),
            ) {
                Text(
                    text = "Glass Surface",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(20.dp),
                )
            }
        }
    }
}
