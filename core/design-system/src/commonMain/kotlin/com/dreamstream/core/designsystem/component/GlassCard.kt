package com.dreamstream.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
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
 * Clickable glassmorphic card.
 *
 * Applies a [hazeEffect] blur over whatever [HazeState] source is behind this
 * card, then overlays a frosted white + brand-violet tint. The translucent
 * border adds the characteristic rim-light of real frosted glass.
 *
 * Requires a [HazeState] that is connected via [Modifier.hazeSource] to the
 * content rendered beneath this card — typically a [GradientBackground].
 *
 * ```kotlin
 * val hazeState = remember { HazeState() }
 *
 * Box {
 *     GradientBackground(hazeState = hazeState)
 *
 *     GlassCard(
 *         hazeState = hazeState,
 *         onClick = { /* navigate */ },
 *     ) {
 *         Text("Content", modifier = Modifier.padding(16.dp))
 *     }
 * }
 * ```
 *
 * For a non-clickable glass container see [GlassSurface].
 */
@Composable
fun GlassCard(
    hazeState: HazeState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.regular,
    shape: Shape = MaterialTheme.shapes.large,
    content: @Composable BoxScope.() -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .clip(shape)
            .hazeEffect(
                state = hazeState,
                style = style.toHazeStyle(),
            ),
        shape = shape,
        border = BorderStroke(
            width = style.borderWidth,
            color = Color.White.copy(alpha = style.borderAlpha),
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(content = content)
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
                modifier = Modifier.padding(32.dp),
            ) {
                Text(
                    text = "Glass Card",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    modifier = Modifier.padding(20.dp),
                )
            }
        }
    }
}
