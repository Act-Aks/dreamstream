package com.dreamstream.core.designsystem.components.badges

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.dreamstream.core.designsystem.components.glass.GlassSurface
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import dev.chrisbanes.haze.HazeState

@Composable
fun NewBadge(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val accent = DreamStreamTheme.colors.warning

    GlassSurface(
        hazeState = hazeState,
        modifier = modifier,
        style = GlassDefaults.thin,
        gradient = Brush.radialGradient(
            colors = listOf(
                accent.copy(alpha = 0.85f),
                accent.copy(alpha = 0.25f),
                accent.copy(alpha = 0.85f),
            ),
        ),
    ) {
        Text(
            text = "NEW",
            color = Color.White,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            style = TextStyle(
                brush = Brush.verticalGradient(
                    listOf(
                        DreamStreamTheme.colors.glassStrong,
                        DreamStreamTheme.colors.ambientBase,
                        DreamStreamTheme.colors.glassStrong
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
            NewBadge(hazeState = hazeState)
        }
    }
}

