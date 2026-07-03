package com.dreamstream.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import com.dreamstream.core.designsystem.theme.DreamStreamTheme

@Composable
fun DreamGradientBackground(
    modifier: Modifier = Modifier,
    hasToolBar: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current

    val containerSize = windowInfo.containerSize
    val smallDimensionPx = minOf(containerSize.width, containerSize.height)
    val smallDimensionDp = with(density) { smallDimensionPx.toDp() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .blur(smallDimensionDp / 3f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            DreamStreamTheme.materialColors.primary,
                            DreamStreamTheme.materialColors.background
                        ),
                        center = Offset(x = containerSize.width / 2f, y = -100f),
                        radius = smallDimensionPx / 2f
                    )
                )
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(if (hasToolBar) Modifier else Modifier.systemBarsPadding())
        ) {
            content()
        }
    }
}

@Preview
@Composable
private fun DreamGradientBackgroundPreview() {
    DreamStreamTheme {
        DreamGradientBackground(
            modifier = Modifier.fillMaxSize()
        ) {

        }
    }
}
