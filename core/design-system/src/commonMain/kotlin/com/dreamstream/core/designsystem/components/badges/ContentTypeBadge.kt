package com.dreamstream.core.designsystem.components.badges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.components.glass.GlassSurface
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import dev.chrisbanes.haze.HazeState

@Composable
fun ContentTypeBadge(
    text: String,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    color: Color = DreamStreamTheme.materialColors.primary,
) {
    GlassSurface(
        hazeState = hazeState,
        modifier = modifier,
        style = GlassDefaults.thin,
        gradient = Brush.linearGradient(
            colors = listOf(
                color.copy(alpha = 0.80f),
                color.copy(alpha = 0.20f),
                color.copy(alpha = 0.80f),
            ),
        ),
        contentPadding = PaddingValues(
            horizontal = DreamStreamTheme.spacing.sm,
            vertical = DreamStreamTheme.spacing.px
        )
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.SemiBold,
            style = DreamStreamTheme.typography.labelSmall.copy(
                brush = Brush.verticalGradient(
                    listOf(
                        DreamStreamTheme.colors.ambientBase,
                        DreamStreamTheme.colors.gold,
                        DreamStreamTheme.colors.ambientBase,
                    )
                )
            )
        )
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(showBackground = false, widthDp = 360, heightDp = 360)
@Composable
private fun GlassSurfacesPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }

        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ContentTypeBadge("Movie", hazeState = hazeState)
                ContentTypeBadge("TV Show", hazeState = hazeState)
                ContentTypeBadge(
                    text = "Anime",
                    color = DreamStreamTheme.materialColors.tertiary,
                    hazeState = hazeState,
                )
                NewBadge(hazeState = hazeState)
            }
        }
    }
}
