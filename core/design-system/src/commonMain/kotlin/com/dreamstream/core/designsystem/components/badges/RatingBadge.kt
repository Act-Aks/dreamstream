package com.dreamstream.core.designsystem.components.badges

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamstream.core.designsystem.components.glass.GlassSurface
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import dev.chrisbanes.haze.HazeState

@Composable
fun RatingBadge(
    rating: Float,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    GlassSurface(
        hazeState = hazeState,
        modifier = modifier,
        style = GlassDefaults.thin,
        gradient = Brush.sweepGradient(
            colors = listOf(
                DreamStreamTheme.colors.gold.copy(alpha = 0.85f),
                DreamStreamTheme.colors.gold.copy(alpha = 0.25f),
                DreamStreamTheme.colors.gold.copy(alpha = 0.85f),
            ),
        ),
        contentPadding = PaddingValues(
            horizontal = DreamStreamTheme.spacing.xs,
            vertical = DreamStreamTheme.spacing.px
        )
    ) {
        Row {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = DreamStreamTheme.colors.gold,
                modifier = Modifier.size(10.dp).padding(end = 2.dp),
            )
            Text(
                text = " ${"%.1f".format(rating)}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                style = TextStyle(
                    brush = Brush.linearGradient(
                        listOf(
                            DreamStreamTheme.colors.ambientPurple,
                            DreamStreamTheme.colors.ambientBase,
                            DreamStreamTheme.colors.ambientPurple
                        )
                    )
                )
            )
        }
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
                RatingBadge(7.8f, hazeState = hazeState)
                RatingBadge(8.9f, hazeState = hazeState)
                RatingBadge(9.6f, hazeState = hazeState)
            }
        }
    }
}
