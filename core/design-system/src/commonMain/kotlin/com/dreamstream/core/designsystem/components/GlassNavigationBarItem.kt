package com.dreamstream.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamstream.core.designsystem.theme.DreamStreamPalette
import com.dreamstream.core.designsystem.theme.DreamStreamTheme

/**
 * A single tab item inside [GlassNavigationBar].
 *
 * Animates the icon scale, icon/label color, and a radial glow behind the
 * active icon — all below the recomposition layer using `graphicsLayer`.
 *
 * @param icon the vector icon to display.
 * @param label short text label below the icon.
 * @param isSelected whether this is the currently active tab.
 * @param onClick called when the user taps this item.
 */
@Composable
fun GlassNavigationBarItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val progress by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        label = "tab_selection",
    )

    val activeColor = DreamStreamTheme.colors.navItemActive
    val inactiveColor = DreamStreamTheme.colors.navItemInactive
    val iconColor = lerp(inactiveColor, activeColor, progress)

    Box(
        modifier = modifier
            .height(64.dp)
            .semantics { role = Role.Tab }
            .clickable(
                onClick = onClick,
                role = Role.Tab,
            )
            .drawBehind {
                if (progress > 0.01f) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DreamStreamPalette.Primary.copy(alpha = 0.25f * progress),
                                DreamStreamPalette.Primary.copy(alpha = 0.08f * progress),
                                Color.Transparent,
                            ),
                        ),
                        radius = size.width * 0.45f,
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer {
                        scaleX = 0.85f + 0.15f * progress
                        scaleY = 0.85f + 0.15f * progress
                        translationY = 2f - 2f * progress
                    },
            )

            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 11.sp,
                    letterSpacing = 0.3.sp,
                ),
                color = iconColor,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .alpha(0.6f + 0.4f * progress),
            )
        }
    }
}
