package com.dreamstream.core.designsystem.components.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.components.glass.GlossyShimmerBox
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults

@Composable
fun ContentCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        shape = DreamStreamTheme.shapes.large,
        border = BorderStroke(
            width = GlassDefaults.regular.borderWidth,
            color = Color.White.copy(alpha = GlassDefaults.regular.borderAlpha),
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DreamStreamTheme.colors.glassThin)
                .padding(DreamStreamTheme.spacing.padScreen)
        ) {
            GlossyShimmerBox(
                modifier = Modifier.height(36.dp).width(36.dp)
                    .clip(DreamStreamTheme.shapes.extraExtraLarge).align(Alignment.TopStart),
            )
            GlossyShimmerBox(
                modifier = Modifier.height(36.dp).width(36.dp)
                    .clip(DreamStreamTheme.shapes.extraExtraLarge).align(Alignment.TopEnd),
            )
            Column(
                modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                GlossyShimmerBox(
                    modifier = Modifier.fillMaxSize().weight(1f)
                        .clip(DreamStreamTheme.shapes.large),
                )
                Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.sm))
                GlossyShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.9f).height(20.dp)
                        .clip(DreamStreamTheme.shapes.extraSmall),
                )
                Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.px))
                GlossyShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.7f).height(12.dp)
                        .clip(DreamStreamTheme.shapes.extraSmall),
                )
                Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.px))
                GlossyShimmerBox(
                    modifier = Modifier.fillMaxWidth(0.5f).height(12.dp)
                        .clip(DreamStreamTheme.shapes.extraSmall),
                )
            }
        }
    }
}

@Preview
@Composable
private fun ContentCardSkeletonPreview() {
    DreamStreamTheme {
        GradientBackground {
            ContentCardSkeleton()
        }
    }
}
