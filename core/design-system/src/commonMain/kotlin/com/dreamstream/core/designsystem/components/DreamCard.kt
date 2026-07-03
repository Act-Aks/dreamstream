package com.dreamstream.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.DreamStreamTheme

@Composable
fun DreamCard(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = DreamStreamTheme.metrics.radius.medium,
    backgroundColor: Color = DreamStreamTheme.colors.glassMedium,
    borderColor: Color = DreamStreamTheme.colors.glassBorder,
    contentPadding: PaddingValues = PaddingValues(DreamStreamTheme.metrics.padding.cardPadding),
    onClick: (() -> Unit) = {},
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    Card(
        onClick = onClick,
        modifier = modifier.clip(shape),
        shape = shape,
        border = BorderStroke(
            width = DreamStreamTheme.metrics.borderWidth.thick,
            color = borderColor
        ),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(backgroundColor).padding(contentPadding),
            content = content,
        )
    }
}

@Preview
@Composable
private fun DreamCardPreview() {
    DreamStreamTheme {
        DreamGradientBackground {
            DreamCard(
                modifier = Modifier.fillMaxWidth().height(200.dp),
            ) {
                Column {
                    Text(text = "DreamCard")
                    Text(text = "DreamCard")
                    Text(text = "DreamCard")
                }
            }
            DreamCard(
                modifier = Modifier.fillMaxWidth().height(200.dp),
            ) {
                Column {
                    Text(text = "DreamCard")
                    Text(text = "DreamCard")
                    Text(text = "DreamCard")
                }
            }
            DreamCard(
                modifier = Modifier.fillMaxWidth().height(200.dp),
            ) {
                Column {
                    Text(text = "DreamCard")
                    Text(text = "DreamCard")
                    Text(text = "DreamCard")
                }
            }
        }
    }
}
