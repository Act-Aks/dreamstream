package com.dreamstream.core.designsystem.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.designsystem.theme.GlassStyle
import com.dreamstream.core.designsystem.theme.toHazeStyle
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect

/**
 * Glassmorphic top app bar.
 *
 * Wraps Material3 [TopAppBar] with a [hazeEffect] modifier so that whatever
 * [HazeState] source scrolls beneath the bar is blurred through it. The
 * container and scrolled-container colors are both set to transparent so the
 * glass effect is visible regardless of scroll position.
 *
 * The bar uses no shape clip (`shape = null` in [hazeEffect]) — top bars are
 * full-width and should not have rounded corners at the top edge.
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
 *         )
 *     },
 * ) { padding ->
 *     LazyColumn(modifier = Modifier.hazeSource(hazeState)) { ... }
 * }
 * ```
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassTopBar(
    title: @Composable () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    style: GlassStyle = GlassDefaults.thin,
) {
    TopAppBar(
        title = title,
        modifier = modifier
            .hazeEffect(
                state = hazeState,
                // shape = null → rectangular — no corner clip on a top bar
                style = style.toHazeStyle(),
            ),
        navigationIcon = navigationIcon,
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent,
            titleContentColor = Color.White,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 160)
@Composable
private fun GlassTopBarPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        GradientBackground(hazeState = hazeState) {
            GlassTopBar(
                title = { Text("DreamStream") },
                hazeState = hazeState,
            )
        }
    }
}
