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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamstream.core.designsystem.components.glass.GlassSurface
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.domain.model.catalog.Quality
import dev.chrisbanes.haze.HazeState

// ---------------------------------------------------------------------------
// UI model mapping from domain Quality
// ---------------------------------------------------------------------------

data class QualityBadgeUiModel(
    val label: String,
    val color: Color,
)

@Composable
fun getQualityBadgeUiModel(
    quality: Quality,
): QualityBadgeUiModel? = DreamStreamTheme.colors.let { colors ->
    when (quality) {
        Quality.Unknown, Quality.Auto -> null

        Quality.Cam, Quality.CamRip, Quality.HdCam -> QualityBadgeUiModel(
            "CAM", colors.qualityCam
        )

        Quality.Low, Quality.Medium, Quality.MediumL -> QualityBadgeUiModel(
            "SD", colors.qualitySd
        )

        Quality.High, Quality.HD -> QualityBadgeUiModel("HD", colors.qualityHd)

        Quality.FullHd -> QualityBadgeUiModel("FHD", colors.qualityFhd)

        Quality.FourK -> QualityBadgeUiModel("4K", colors.qualityFourK)

        Quality.BluRay -> QualityBadgeUiModel("BD", colors.qualityBluRay)
    }
}

@Composable
fun QualityBadge(
    quality: Quality,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    val uiModel = getQualityBadgeUiModel(quality) ?: return

    GlassSurface(
        hazeState = hazeState,
        modifier = modifier,
        style = GlassDefaults.thin,
        gradient = Brush.radialGradient(
            colors = listOf(
                uiModel.color.copy(alpha = 0.50f),
                uiModel.color.copy(alpha = 0.15f),
                uiModel.color.copy(alpha = 0.50f),
            ),
        ),
        contentPadding = PaddingValues(DreamStreamTheme.spacing.xs)
    ) {
        Text(
            text = uiModel.label,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            style = TextStyle(
                brush = Brush.verticalGradient(
                    listOf(
                        DreamStreamTheme.colors.textPrimary,
                        DreamStreamTheme.colors.gold,
                        DreamStreamTheme.colors.textDisabled,
                    )
                )
            )
        )
    }
}

// ---------------------------------------------------------------------------
// Preview
// ---------------------------------------------------------------------------

@Preview(name = "QualityBadge – Common qualities", showBackground = false)
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
                QualityBadge(Quality.Cam, hazeState = hazeState)
                QualityBadge(Quality.Medium, hazeState = hazeState)
                QualityBadge(Quality.HD, hazeState = hazeState)
                QualityBadge(Quality.FullHd, hazeState = hazeState)
                QualityBadge(Quality.FourK, hazeState = hazeState)
                QualityBadge(Quality.BluRay, hazeState = hazeState)
            }
        }
    }
}
