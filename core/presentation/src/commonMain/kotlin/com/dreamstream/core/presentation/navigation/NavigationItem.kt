package com.dreamstream.core.presentation.navigation

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Generic model for a navigation bar item.
 *
 * Route-agnostic — the design-system component only cares about the icon and
 * label. Route mapping lives in the app layer (e.g. [BottomTab] enum).
 */
data class NavigationItem(
    val icon: ImageVector,
    val label: String,
)
