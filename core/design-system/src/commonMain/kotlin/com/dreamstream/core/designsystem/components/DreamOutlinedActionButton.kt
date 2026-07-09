package com.dreamstream.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.dreamstream.core.designsystem.theme.DreamStreamTheme

@Composable
fun DreamOutlinedActionButton(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = DreamStreamTheme.materialColors.onBackground),
        border = BorderStroke(
            width = DreamStreamTheme.metrics.borderWidth.small,
            color = if (enabled) DreamStreamTheme.materialColors.primary
            else DreamStreamTheme.materialColors.primary.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(DreamStreamTheme.metrics.radius.infinite),
        modifier = modifier.height(IntrinsicSize.Min)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = DreamStreamTheme.metrics.spacing.xs),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(DreamStreamTheme.metrics.spacing.md)
                    .alpha(if (isLoading) 1f else 0f),
                strokeWidth = DreamStreamTheme.metrics.borderWidth.large,
                color = DreamStreamTheme.materialColors.onBackground
            )
            Text(
                text = text,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview
@Composable
private fun DreamOutlinedActionButtonPreview() {
    DreamStreamTheme {
        DreamGradientBackground {
            DreamOutlinedActionButton(
                text = "Click",
                isLoading = false,
                onClick = {},
            )
            DreamOutlinedActionButton(
                text = "Click",
                isLoading = false,
                onClick = {},
            )
            DreamOutlinedActionButton(
                text = "Click",
                isLoading = false,
                onClick = {},
            )
            DreamOutlinedActionButton(
                text = "Click",
                isLoading = false,
                onClick = {},
            )
            DreamOutlinedActionButton(
                text = "Click", isLoading = false, onClick = {}, enabled = false
            )
        }
    }
}
