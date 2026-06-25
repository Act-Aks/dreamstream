package com.dreamstream.core.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * A single navigation destination used by [GlassNavigationBar].
 *
 * @param icon Icon shown for the destination.
 * @param label Label shown below the icon and used for accessibility.
 */
data class NavigationItem(
    val icon: ImageVector,
    val label: String,
)
