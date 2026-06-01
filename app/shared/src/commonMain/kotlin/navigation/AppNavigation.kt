package navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.dreamstream.core.designsystem.components.GlassNavigationBar
import com.dreamstream.core.presentation.navigation.NavigationItem
import com.dreamstream.feature.details.presentation.DetailsRoot
import com.dreamstream.feature.details.presentation.DetailsRoute
import com.dreamstream.feature.home.presentation.HomeRoot
import com.dreamstream.feature.home.presentation.HomeRoute
import com.dreamstream.feature.search.presentation.SearchRoot
import com.dreamstream.feature.search.presentation.SearchRoute
import com.dreamstream.feature.settings.presentation.SettingsRoot
import com.dreamstream.feature.settings.presentation.SettingsRoute
import dev.chrisbanes.haze.HazeState
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

private val navConfig = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(androidx.navigation3.runtime.NavKey::class) {
            subclass(HomeRoute::class, HomeRoute.serializer())
            subclass(DetailsRoute::class, DetailsRoute.serializer())
            subclass(SearchRoute::class, SearchRoute.serializer())
            subclass(SettingsRoute::class, SettingsRoute.serializer())
        }
    }
}

/**
 * Root navigation host for DreamStream.
 *
 * Assembles all feature nav graphs in one place. Cross-feature navigation
 * is expressed as lambda callbacks — feature modules never import each
 * other's routes directly.
 *
 * Uses Navigation3 NavDisplay with a serializable back stack. Each entry
 * is matched by route type via the entryProvider DSL.
 *
 * Features a glassmorphic bottom navigation bar that scaffolds the three
 * main tab screens (Home, Search, Settings). The bar is visible when the
 * user is on a tab root and hides when a detail screen is pushed on top.
 */
@Composable
internal fun AppNavigation(modifier: Modifier = Modifier) {
    val backStack = rememberNavBackStack(navConfig, HomeRoute)

    // Tab state — tracks which bottom tab is active.
    var selectedTab by rememberSaveable { mutableStateOf(BottomTab.Home) }

    // Shared HazeState for the bottom navigation bar.
    val hazeState = remember { HazeState() }

    // Show the bar only when the back stack has a single entry (tab root).
    // When DetailsRoute is pushed, backStack.size == 2 → bar hides.
    val showBar by remember {
        derivedStateOf { backStack.size == 1 }
    }

    // Helper: switch to a tab, clearing the back stack and pushing the tab root.
    fun switchTab(tab: BottomTab) {
        if (tab != selectedTab) {
            selectedTab = tab
            backStack.clear()
            backStack.add(tab.route)
        }
    }

    Box(modifier = modifier) {
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.fillMaxSize(),
            onBack = {
                if (backStack.size > 1) {
                    // Detail screen is on top — pop it.
                    backStack.removeLastOrNull()
                } else if (selectedTab != BottomTab.Home) {
                    // On a non-Home tab — switch back to Home.
                    switchTab(BottomTab.Home)
                }
                // On Home tab root — do nothing (could exit app).
            },
            entryProvider = entryProvider {
                entry<HomeRoute> {
                    HomeRoot(
                        onNavigateToDetail = { contentId ->
                            backStack.add(DetailsRoute(contentId))
                        },
                        onNavigateToSearch = {
                            switchTab(BottomTab.Search)
                        },
                        onNavigateToSettings = {
                            switchTab(BottomTab.Settings)
                        },
                    )
                }
                entry<DetailsRoute> { route ->
                    DetailsRoot(
                        contentId = route.contentId,
                        onNavigateBack = { backStack.removeLastOrNull() },
                    )
                }
                entry<SearchRoute> {
                    SearchRoot(
                        onNavigateToDetail = { contentId ->
                            backStack.add(DetailsRoute(contentId))
                        },
                        onNavigateBack = {
                            switchTab(BottomTab.Home)
                        },
                    )
                }
                entry<SettingsRoute> {
                    SettingsRoot(
                        onNavigateBack = {
                            switchTab(BottomTab.Home)
                        },
                    )
                }
            },
        )

        // Bottom navigation bar — slides in/out based on back stack depth.
        AnimatedVisibility(
            visible = showBar,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut(),
            modifier = Modifier.align(Alignment.BottomCenter),
        ) {
            GlassNavigationBar(
                items = BottomTab.entries.map { NavigationItem(it.icon, it.label) },
                selectedIndex = selectedTab.ordinal,
                onItemSelected = { index ->
                    switchTab(BottomTab.entries[index])
                },
                hazeState = hazeState,
            )
        }
    }
}
