package com.dreamstream.core.designsystem.components.glass

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.designsystem.theme.GlassGradientTint
import com.dreamstream.core.designsystem.theme.GlassStyle
import dev.chrisbanes.haze.HazeState

/**
 * Glassmorphic top app bar using Material3's [TopAppBar] with a composable `title` API.
 *
 * This component wraps Material3's [TopAppBar] inside a [GlassSurface], providing:
 * - Full glassmorphism via Haze's blur effect ([HazeState])
 * - Gradient tint overlay for a glossy look
 * - Configurable shape, style, and padding via [GlassStyle] and [Shape]
 * - Standard TopAppBar API for navigation icons, actions, and composable title
 *
 * The bar uses transparent container and scrolled colors so the glass effect remains
 * visible regardless of scroll position. The haze effect uses `shape = null` (rectangular)
 * since top bars are full-width and should not have rounded corners at the top edge.
 *
 * ## Usage
 *
 * ```kotlin
 * val hazeState = remember { HazeState() }
 *
 * Scaffold(
 *     containerColor = Color.Transparent,
 *     topBar = {
 *         GlassTopBar(
 *             title = { Text("DreamStream") },
 *             hazeState = hazeState,
 *             navigationIcon = { Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back") },
 *             actions = {
 *                 IconButton(onClick = { /* ... */ }) {
 *                     Icon(imageVector = Icons.Default.Settings, contentDescription = "Settings")
 *                 }
 *             },
 *         )
 *     },
 * ) { padding ->
 *     LazyColumn(
 *         modifier = Modifier.hazeSource(hazeState),
 *         contentPadding = padding
 *     ) {
 *         // Your content here
 *     }
 * }
 * ```
 *
 * @param title Composable content for the top app bar title (e.g., [Text] or custom layout)
 * @param hazeState The Haze state that tracks the blurred content beneath this bar
 * @param modifier Modifier to apply to this top bar
 * @param shape Shape for the glass surface (default: [MaterialTheme.shapes.medium])
 * @param style Glass style defining haze opacity, tint, and other glassmorphism properties (default: [GlassDefaults.thin])
 * @param gradient Brush for the gradient tint overlay (default: [GlassGradientTint])
 * @param navigationIcon Composable content for the navigation icon slot (default: empty)
 * @param actions Composable content for the action icons slot (default: empty)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTopBar(
    title: @Composable () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    style: GlassStyle = GlassDefaults.thin,
    gradient: Brush = GlassGradientTint,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
) {
    GlassSurface(
        hazeState = hazeState,
        modifier = modifier.fillMaxWidth().statusBarsPadding(),
        style = style,
        shape = shape,
        contentPadding = PaddingValues(
            horizontal = DreamStreamTheme.spacing.px,
            vertical = DreamStreamTheme.spacing.zero
        ),
        gradient = gradient,
    ) {
        TopAppBar(
            title = title,
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = navigationIcon,
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                titleContentColor = DreamStreamTheme.colors.textPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            ),
        )
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Preview of [GlassTopBar] with title and actions.
 */
@Preview(showBackground = false, widthDp = 360, heightDp = 160)
@Composable
private fun GlassTopBarPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassTopBar(
                title = {
                    Text(
                        text = "DreamStream",
                        color = DreamStreamTheme.colors.textPrimary,
                        style = DreamStreamTheme.typography.titleMedium,
                    )
                },
                hazeState = hazeState,
                actions = {
                    Text(
                        text = "⚙",
                        color = DreamStreamTheme.colors.textPrimary,
                        style = DreamStreamTheme.typography.bodyMedium,
                    )
                },
            )
        }
    }
}

/**
 * Preview of [GlassTopBar] with title only (no actions).
 */
@Preview(showBackground = false, widthDp = 360, heightDp = 160)
@Composable
private fun GlassTopBarNoActionsPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassTopBar(
                title = {
                    Text(
                        text = "Profile",
                        color = DreamStreamTheme.colors.textPrimary,
                        style = DreamStreamTheme.typography.titleMedium,
                    )
                },
                hazeState = hazeState,
            )
        }
    }
}

/**
 * Preview of [GlassTopBar] with navigation icon, title, and actions.
 */
@Preview(showBackground = false, widthDp = 360, heightDp = 160)
@Composable
private fun GlassTopBarWithNavigationPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        GradientBackground(
            hazeState = hazeState,
            modifier = Modifier,
        ) {
            GlassTopBar(
                title = {
                    Text(
                        text = "Home",
                        color = DreamStreamTheme.colors.textPrimary,
                        style = DreamStreamTheme.typography.titleMedium,
                    )
                },
                hazeState = hazeState,
                navigationIcon = {
                    Text(
                        text = "←",
                        color = DreamStreamTheme.colors.textPrimary,
                        style = DreamStreamTheme.typography.bodyLarge,
                    )
                },
                actions = {
                    Text(
                        text = "⚙",
                        color = DreamStreamTheme.colors.textPrimary,
                        style = DreamStreamTheme.typography.bodyMedium,
                    )
                },
            )
        }
    }
}
