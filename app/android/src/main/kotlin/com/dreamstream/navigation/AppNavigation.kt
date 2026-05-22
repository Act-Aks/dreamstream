package com.dreamstream.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.dreamstream.feature.home.presentation.HomeRoot
import com.dreamstream.feature.home.presentation.HomeRoute

/**
 * Root navigation host for DreamStream.
 *
 * Assembles all feature nav graphs in one place. Cross-feature navigation
 * is expressed as lambda callbacks — feature modules never import each
 * other's routes directly.
 *
 * Uses Navigation3 NavDisplay with a serializable back stack. Each entry
 * is matched by route type via the entryProvider DSL.
 */
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(HomeRoute)

    NavDisplay(
        backStack = backStack,
        modifier = modifier,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry(HomeRoute) {
                // No cross-feature navigation yet — detail screen added in a future slice
                HomeRoot(onNavigateToDetail = { /* TODO: navigate to detail */ })
            }
        },
    )
}
