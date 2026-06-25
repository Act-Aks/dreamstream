package com.dreamstream.core.designsystem.components.glass

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.dreamstream.core.designsystem.theme.DreamStreamPalette
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.designsystem.theme.GlassGradientTint
import com.dreamstream.core.designsystem.theme.GlassStyle
import com.dreamstream.core.presentation.navigation.NavigationItem
import dev.chrisbanes.haze.HazeState

private const val SelectedIndicatorAnimationDurationMs = 320
private const val GlowCenterInterpolationStart = 0f
private const val GlowCenterInterpolationEnd = 1f
private const val GlowAlphaStrong = 0.26f
private const val GlowAlphaSoft = 0.10f
private const val GlowAlphaTrace = 0.04f
private const val GlowRadiusFraction = 0.56f
private const val GlowYFraction = 0.42f

/**
 * Glossy, gradient-tinted bottom navigation bar with glassmorphism.
 *
 * This component uses [GlassSurface] as its glass primitive, so the blur,
 * border, gradient tint, and rounded shape stay consistent with the rest of
 * the design system.
 *
 * The caller owns the destination list and selected index, which keeps the
 * component stateless and easy to use from screen-level state.
 *
 * A soft liquid-glass glow animates between the previously selected item and
 * the newly selected item, making tab changes feel smoother and more organic.
 *
 * @param items Navigation destinations to render.
 * @param selectedIndex Index of the currently selected item.
 * @param hazeState Shared haze state connected to the content behind this bar.
 * @param modifier Modifier applied to the outer container.
 * @param shape Shape of the glass surface.
 * @param style Glass effect style.
 * @param gradient Gradient tint overlay used for the glossy glass look.
 */
@Composable
fun GlassNavigationBar(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    style: GlassStyle = GlassDefaults.thin,
    gradient: Brush = GlassGradientTint,
) {
    val selectionProgress by animateFloatAsState(
        targetValue = selectedIndex.coerceIn(0, (items.size - 1).coerceAtLeast(0)).toFloat(),
        animationSpec = tween(durationMillis = SelectedIndicatorAnimationDurationMs),
        label = "glass_nav_selection_progress",
    )

    GlassSurface(
        hazeState = hazeState,
        modifier = modifier.fillMaxWidth(),
        style = style,
        shape = shape,
        contentPadding = PaddingValues(
            horizontal = DreamStreamTheme.spacing.xs,
            vertical = DreamStreamTheme.spacing.px,
        ),
        gradient = gradient,
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().drawBehind {
                if (items.isNotEmpty()) {
                    val segmentWidth = size.width / items.size
                    val centerX = segmentWidth * (selectionProgress + 0.5f)
                    val centerY = size.height * GlowYFraction
                    val radius = segmentWidth * GlowRadiusFraction

                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                DreamStreamPalette.Primary.copy(alpha = GlowAlphaStrong),
                                DreamStreamPalette.Primary.copy(alpha = GlowAlphaSoft),
                                DreamStreamPalette.Primary.copy(alpha = GlowAlphaTrace),
                                Color.Transparent,
                            ),
                            center = Offset(centerX, centerY),
                            radius = radius,
                        ),
                        radius = radius,
                        center = Offset(centerX, centerY),
                    )
                }
            },
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                items.forEachIndexed { index, item ->
                    GlassNavigationBarItem(
                        icon = item.icon,
                        label = item.label,
                        isSelected = index == selectedIndex,
                        onClick = { onItemSelected(index) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

/**
 * Preview of [GlassNavigationBar] with two destination items.
 */
@Preview(showBackground = false, widthDp = 360, heightDp = 160)
@Composable
private fun GlassNavigationBarPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }

        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassNavigationBar(
                items = listOf(
                    NavigationItem(
                        icon = Icons.Filled.Home,
                        label = "Home",
                    ),
                    NavigationItem(
                        icon = Icons.Filled.Search,
                        label = "Search",
                    ),
                ),
                selectedIndex = 0,
                hazeState = hazeState,
                onItemSelected = {}
            )
        }
    }
}

/**
 * Preview of [GlassNavigationBar] with three destination items.
 */
@Preview(showBackground = false, widthDp = 390, heightDp = 160)
@Composable
private fun GlassNavigationBarThreeItemsPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        var selectedIndex = remember { 0 }

        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassNavigationBar(
                items = listOf(
                    NavigationItem(
                        icon = Icons.Filled.Home,
                        label = "Home",
                    ),
                    NavigationItem(
                        icon = Icons.Filled.Search,
                        label = "Search",
                    ),
                    NavigationItem(
                        icon = Icons.Filled.Settings,
                        label = "Profile",
                    ),
                ),
                selectedIndex = selectedIndex,
                hazeState = hazeState,
                onItemSelected = { selectedIndex = it }
            )
        }
    }
}
