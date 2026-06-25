package com.dreamstream.core.designsystem.components.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dreamstream.core.designsystem.theme.DreamStreamPalette
import com.dreamstream.core.designsystem.theme.DreamStreamTheme

private const val SelectedProgress = 1f
private const val UnselectedProgress = 0f
private const val GlowAlphaStrong = 0.24f
private const val GlowAlphaSoft = 0.10f
private const val GlowRadiusFraction = 0.46f
private const val IconScaleBase = 0.86f
private const val IconScaleDelta = 0.14f
private const val IconTranslationYBase = 2f
private const val IconTranslationYDelta = 2f
private const val LabelAlphaBase = 0.58f
private const val LabelAlphaDelta = 0.42f
private const val SelectionThreshold = 0.01f
private const val SelectionSpringDampingRatio = 0.72f
private const val SelectionSpringStiffness = 380f

private val GlassNavItemHeight = 64.dp
private val GlassNavIconSize = 24.dp
private val GlassNavLabelTopPadding = 2.dp
private val GlassNavLabelFontSize = 11.sp
private val GlassNavLabelLetterSpacing = 0.3.sp

/**
 * A single navigation item inside [GlassNavigationBar].
 *
 * The item animates icon scale, label opacity, and a subtle radial glow behind
 * the active destination. It behaves like a tab, so it exposes [Role.Tab]
 * semantics for accessibility.
 *
 * @param icon Vector icon to display.
 * @param label Label shown below the icon and used for accessibility.
 * @param isSelected Whether this item is currently selected.
 * @param onClick Called when the user taps this item.
 * @param modifier Modifier applied to the item container.
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
        targetValue = if (isSelected) SelectedProgress else UnselectedProgress,
        animationSpec = spring(
            dampingRatio = SelectionSpringDampingRatio,
            stiffness = SelectionSpringStiffness,
        ),
        label = "glass_nav_item_selection",
    )

    val activeColor = DreamStreamTheme.colors.navItemActive
    val inactiveColor = DreamStreamTheme.colors.navItemInactive
    val iconColor = lerp(inactiveColor, activeColor, progress)

    Box(
        modifier = modifier
            .height(GlassNavItemHeight)
            .semantics { role = Role.Tab }
            .clickable(
                onClick = onClick,
                role = Role.Tab,
            )
            .drawBehind {
                if (progress > SelectionThreshold) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DreamStreamPalette.Primary.copy(alpha = GlowAlphaStrong * progress),
                                DreamStreamPalette.Primary.copy(alpha = GlowAlphaSoft * progress),
                                Color.Transparent,
                            ),
                        ),
                        radius = size.width * GlowRadiusFraction,
                    )
                }
            },
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier
                    .size(GlassNavIconSize)
                    .graphicsLayer {
                        scaleX = IconScaleBase + IconScaleDelta * progress
                        scaleY = IconScaleBase + IconScaleDelta * progress
                        translationY = IconTranslationYBase - IconTranslationYDelta * progress
                    },
            )

            Text(
                text = label,
                style = DreamStreamTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = GlassNavLabelFontSize,
                    letterSpacing = GlassNavLabelLetterSpacing,
                ),
                color = iconColor,
                modifier = Modifier
                    .padding(top = GlassNavLabelTopPadding)
                    .alpha(LabelAlphaBase + LabelAlphaDelta * progress),
            )
        }
    }
}

/**
 * Preview of [GlassNavigationBarItem] in selected state.
 */
@Preview(showBackground = true)
@Composable
private fun GlassNavigationBarItemSelectedPreview() {
    DreamStreamTheme {
        GlassNavigationBarItem(
            icon = Icons.Filled.Home,
            label = "Home",
            isSelected = true,
            onClick = {},
        )
    }
}

/**
 * Preview of [GlassNavigationBarItem] in unselected state.
 */
@Preview(showBackground = true)
@Composable
private fun GlassNavigationBarItemUnselectedPreview() {
    DreamStreamTheme {
        GlassNavigationBarItem(
            icon = Icons.Filled.Search,
            label = "Search",
            isSelected = false,
            onClick = {},
        )
    }
}
