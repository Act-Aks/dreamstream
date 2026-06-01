package com.dreamstream.core.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.theme.DreamStreamPalette
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.designsystem.theme.GlassStyle
import com.dreamstream.core.designsystem.theme.toHazeStyle
import com.dreamstream.core.presentation.navigation.NavigationItem
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * Glassmorphic bottom navigation bar.
 *
 * Renders a row of [GlassNavigationBarItem]s inside a frosted-glass surface.
 * When a [HazeState] source is connected (e.g. via [GradientBackground] in the
 * same composition tree), the bar blurs the content behind it. Otherwise it
 * falls back to a solid dark glass-tinted background that still looks cohesive.
 *
 * The bar uses [GlassDefaults.thin] by default — subtle blur with an 8%
 * frosted fill and a faint primary-violet tint. A thin top border acts as a
 * visual separator.
 *
 * @param items the navigation items to display.
 * @param selectedIndex index of the currently selected item.
 * @param onItemSelected called when the user taps a different item.
 * @param hazeState shared [HazeState] connected to the content source behind
 *   this bar. When no connected source exists, a solid glass-tinted background
 *   is rendered as a fallback.
 * @param style glass style — defaults to [GlassDefaults.thin].
 */
@Composable
fun GlassNavigationBar(
    items: List<NavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.thin,
) {
    val barShape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(barShape)
            .hazeEffect(
                state = hazeState,
                style = style.toHazeStyle(),
            ),
        shape = barShape,
        color = Color.Transparent,
        border = BorderStroke(
            width = style.borderWidth,
            color = Color.White.copy(alpha = style.borderAlpha),
        ),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Box {
            // Solid fallback background — visible when haze has no connected
            // source. Uses a gradient from NavBarBg to a slightly lighter
            // variant for depth.
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                DreamStreamPalette.NavBarBg,
                                DreamStreamPalette.Background,
                            ),
                        ),
                    ),
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
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
