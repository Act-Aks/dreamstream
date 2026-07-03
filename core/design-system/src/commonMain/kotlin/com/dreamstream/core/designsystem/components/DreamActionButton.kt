package com.dreamstream.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.DreamStreamTheme

@Composable
fun DreamActionButton(
    title: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    cornerRadius: Dp = DreamStreamTheme.metrics.radius.medium,
    borderColor: Color = DreamStreamTheme.colors.glassBorder
) {
    val shape: Shape = RoundedCornerShape(cornerRadius)

    Button(
        modifier = modifier.height(IntrinsicSize.Min),
        shape = shape,
        onClick = onClick,
        enabled = enabled,
        border = BorderStroke(
            width = DreamStreamTheme.metrics.borderWidth.thick,
            color = borderColor
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = DreamStreamTheme.materialColors.primary.copy(0.5f),
            contentColor = DreamStreamTheme.materialColors.onPrimary,
            disabledContainerColor = DreamStreamTheme.materialColors.primary.copy(0.3f),
            disabledContentColor = DreamStreamTheme.materialColors.onPrimary.copy(alpha = 0.45f)
        ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DreamStreamTheme.metrics.spacing.xs),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(DreamStreamTheme.metrics.spacing.md)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = DreamStreamTheme.materialColors.onPrimary
            )
            Text(
                text = title,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun DreamActionButtonPreview() {
    DreamStreamTheme {
        DreamGradientBackground {
            DreamActionButton(
                title = "Click",
                isLoading = false,
                onClick = {},
            )
            DreamActionButton(
                title = "Click",
                isLoading = false,
                onClick = {},
            )
            DreamActionButton(
                title = "Click",
                isLoading = false,
                onClick = {},
            )
            DreamActionButton(
                title = "Click",
                isLoading = false,
                onClick = {},
                enabled = false
            )
        }
    }
}
