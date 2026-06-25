package navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation3.runtime.NavKey
import com.dreamstream.feature.home.presentation.HomeRoute
import com.dreamstream.feature.search.presentation.SearchRoute
import com.dreamstream.feature.settings.presentation.SettingsRoute

/**
 * Bottom navigation tab definitions.
 *
 * Maps each tab to its Navigation3 route, Material icon, and display label.
 * The order here determines the visual order in [com.dreamstream.core.designsystem.components.glass.GlassNavigationBar].
 */
enum class BottomTab(
    val route: NavKey,
    val icon: ImageVector,
    val label: String,
) {
    Home(
        route = HomeRoute,
        icon = Icons.Filled.Home,
        label = "Home",
    ),
    Search(
        route = SearchRoute,
        icon = Icons.Filled.Search,
        label = "Search",
    ),
    Settings(
        route = SettingsRoute,
        icon = Icons.Filled.Settings,
        label = "Settings",
    ),
}
