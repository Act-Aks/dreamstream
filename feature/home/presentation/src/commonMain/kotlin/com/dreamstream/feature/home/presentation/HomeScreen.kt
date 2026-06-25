package com.dreamstream.feature.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreamstream.core.designsystem.components.glass.GlassCard
import com.dreamstream.core.designsystem.components.glass.GlassTopBar
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.presentation.resources.CoreRes
import com.dreamstream.core.presentation.resources.action_search
import com.dreamstream.core.presentation.resources.action_settings
import com.dreamstream.core.presentation.resources.app_name
import com.dreamstream.core.presentation.ui.ObserveAsEvents
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.presentation.model.ContentUi
import com.dreamstream.feature.home.presentation.model.HomeSectionUi
import com.dreamstream.feature.home.presentation.resources.Res
import com.dreamstream.feature.home.presentation.resources.home_action_reload
import com.dreamstream.feature.home.presentation.resources.home_empty_heading
import com.dreamstream.feature.home.presentation.resources.home_empty_message
import com.dreamstream.feature.home.presentation.resources.home_retry_button
import dev.chrisbanes.haze.HazeState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Root — injects ViewModel, observes events
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun HomeRoot(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var pendingError by remember { mutableStateOf<UiText?>(null) }
    val errorMessage = pendingError?.asString()

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            pendingError = null
        }
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HomeEvent.NavigateToDetail -> onNavigateToDetail(event.contentId)
            is HomeEvent.NavigateToSearch -> onNavigateToSearch()
            is HomeEvent.NavigateToSettings -> onNavigateToSettings()
            is HomeEvent.ShowError -> {
                pendingError = event.message
            }
        }
    }

    HomeScreen(
        state = state,
        snackbarHostState = snackbarHostState,
        onAction = viewModel::onAction,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Screen — pure state + callbacks, previewable
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (HomeAction) -> Unit,
) {
    // hazeState is Compose-owned: connects GradientBackground (source) to
    // GlassTopBar and GlassCards (children) for the blur-through-glass effect.
    val hazeState = remember { HazeState() }

    Box(modifier = modifier.fillMaxSize()) {

        // ── 1. Gradient background — the hazeSource ───────────────────────────
        // Lives at the bottom of the z-stack. GlassTopBar and content cards
        // blur this layer rather than each other.
        GradientBackground(hazeState = hazeState)

        // ── 2. Scaffold — transparent so the gradient shows through ───────────
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                GlassTopBar(
                    title = {
                        Text(
                            text = stringResource(CoreRes.string.app_name),
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                        )
                    },
                    hazeState = hazeState,
                    actions = {
                        IconButton(onClick = { onAction(HomeAction.OnRefresh) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = stringResource(Res.string.home_action_reload),
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { onAction(HomeAction.OnSettingsClick) }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = stringResource(CoreRes.string.action_settings),
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { onAction(HomeAction.OnSearchClick) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(CoreRes.string.action_search),
                                tint = Color.White,
                            )
                        }
                    },
                )
            },
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when {
                    state.isLoading && state.sections.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    state.sections.isEmpty() -> {
                        EmptyHomeState(
                            modifier = Modifier.align(Alignment.Center),
                            error = state.error,
                            onRetry = { onAction(HomeAction.OnRefresh) },
                        )
                    }

                    else -> {
                        PullToRefreshBox(
                            isRefreshing = state.isLoading,
                            onRefresh = { onAction(HomeAction.OnRefresh) },
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            HomeSectionList(
                                sections = state.sections,
                                hazeState = hazeState,
                                onContentClick = { id -> onAction(HomeAction.OnContentClick(id)) },
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private composable helpers
// ─────────────────────────────────────────────────────────────────────────────

@Composable
private fun HomeSectionList(
    sections: List<HomeSectionUi>,
    hazeState: HazeState,
    onContentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        items(
            items = sections,
            key = { it.id },
        ) { section ->
            HomeSectionRow(
                section = section,
                hazeState = hazeState,
                onContentClick = onContentClick,
            )
        }
    }
}

@Composable
private fun HomeSectionRow(
    section: HomeSectionUi,
    hazeState: HazeState,
    onContentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(
                items = section.items,
                key = { it.id },
            ) { content ->
                ContentCard(
                    content = content,
                    hazeState = hazeState,
                    onClick = { onContentClick(content.id) },
                )
            }
        }
    }
}

@Composable
private fun ContentCard(
    content: ContentUi,
    hazeState: HazeState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassCard(
        hazeState = hazeState,
        onClick = onClick,
        modifier = modifier
            .width(160.dp)
            .height(220.dp),
        style = GlassDefaults.regular,
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            // Thumbnail placeholder — replace with AsyncImage once Coil is wired.
            // Keep the alpha dim so the glass background glows through.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .graphicsLayer { alpha = 0.5f },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = content.typeName.asString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Column {
                Text(
                    text = content.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                if (content.year.isNotEmpty() || content.rating.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = listOfNotNull(
                            content.year.takeIf { it.isNotEmpty() },
                            content.rating.takeIf { it.isNotEmpty() }?.let { "★ $it" },
                        ).joinToString(" · "),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyHomeState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    error: UiText? = null,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.home_empty_heading),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
        Text(
            text = error?.asString() ?: stringResource(Res.string.home_empty_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = stringResource(Res.string.home_retry_button))
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun HomeScreenLoadingPreview() {
    DreamStreamTheme {
        HomeScreen(
            state = HomeState(isLoading = true),
            onAction = {},
        )
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun HomeScreenEmptyPreview() {
    DreamStreamTheme {
        HomeScreen(
            state = HomeState(isLoading = false, sections = emptyList()),
            onAction = {},
        )
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun HomeScreenContentPreview() {
    DreamStreamTheme {
        HomeScreen(
            state = HomeState(
                isLoading = false,
                sections = listOf(
                    HomeSectionUi(
                        id = "trending",
                        title = "Trending Now",
                        items = listOf(
                            ContentUi(
                                id = "1",
                                title = "Cosmic Odyssey",
                                thumbnailUrl = null,
                                typeName = UiText.DynamicString("Movie"),
                                year = "2024",
                                rating = "8.4",
                            ),
                            ContentUi(
                                id = "2",
                                title = "Neon City Chronicles",
                                thumbnailUrl = null,
                                typeName = UiText.DynamicString("TV Series"),
                                year = "2023",
                                rating = "9.1",
                            ),
                            ContentUi(
                                id = "3",
                                title = "The Last Frontier",
                                thumbnailUrl = null,
                                typeName = UiText.DynamicString("Movie"),
                                year = "2024",
                                rating = "7.8",
                            ),
                        ),
                    ),
                    HomeSectionUi(
                        id = "new",
                        title = "New Arrivals",
                        items = listOf(
                            ContentUi(
                                id = "4",
                                title = "Echo Chamber",
                                thumbnailUrl = null,
                                typeName = UiText.DynamicString("Movie"),
                                year = "2024",
                                rating = "",
                            ),
                            ContentUi(
                                id = "5",
                                title = "Dark Horizon",
                                thumbnailUrl = null,
                                typeName = UiText.DynamicString("Movie"),
                                year = "2024",
                                rating = "8.0",
                            ),
                        ),
                    ),
                ),
            ),
            onAction = {},
        )
    }
}
