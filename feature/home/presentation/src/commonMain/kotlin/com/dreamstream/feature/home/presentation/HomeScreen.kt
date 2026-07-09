package com.dreamstream.feature.home.presentation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.dreamstream.core.presentation.ui.ObserveAsEvents
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.presentation.model.ContentUi
import com.dreamstream.feature.home.presentation.model.HomeSectionUi
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun HomeRoot(
    onNavigateToDetail: (String) -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

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
            HomeEvent.NavigateToSearch -> onNavigateToSearch()
            HomeEvent.NavigateToSettings -> onNavigateToSettings()
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

// Screen — pure state + callbacks

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onAction: (HomeAction) -> Unit,
) {
    val hazeState = remember { HazeState() }

    Box(modifier = modifier.fillMaxSize()) {

        GradientBackground(hazeState = hazeState)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                GlassTopBar(
                    title = {
                        Column {
                            Text(
                                text = "DreamStream",
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                            )
                            Text(
                                text = "Handpicked movies & shows",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFCBD5F5),
                            )
                        }
                    },
                    hazeState = hazeState,
                    actions = {
                        IconButton(onClick = { onAction(HomeAction.OnRefresh) }) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reload",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { onAction(HomeAction.OnSettingsClick) }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White,
                            )
                        }
                        IconButton(onClick = { onAction(HomeAction.OnSearchClick) }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
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
                        HomeSectionList(
                            sections = state.sections,
                            hazeState = hazeState,
                            onContentClick = { id ->
                                onAction(HomeAction.OnContentClick(id))
                            },
                            onLoadMoreSection = { sectionId ->
                                onAction(HomeAction.OnLoadMoreSection(sectionId))
                            },
                            isAppendingBySectionId = state.isAppendingBySectionId,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }
}

// Section list + rows + cards

@Composable
private fun HomeSectionList(
    sections: List<HomeSectionUi>,
    hazeState: HazeState,
    onContentClick: (String) -> Unit,
    onLoadMoreSection: (String) -> Unit,
    isAppendingBySectionId: Map<String, Boolean>,
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
            val isAppending = isAppendingBySectionId[section.id] == true
            HomeSectionRow(
                section = section,
                hazeState = hazeState,
                isAppending = isAppending,
                onContentClick = onContentClick,
                onLoadMore = onLoadMoreSection,
            )
        }
    }
}

@Composable
private fun HomeSectionRow(
    section: HomeSectionUi,
    hazeState: HazeState,
    isAppending: Boolean,
    onContentClick: (String) -> Unit,
    onLoadMore: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 4.dp),
        )
        Text(
            text = "Swipe to explore",
            style = MaterialTheme.typography.bodySmall,
            color = Color(0xFF9CA3AF),
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            itemsIndexed(
                items = section.items,
                key = { _, item -> item.id },
            ) { index, content ->
                // Trigger load-more only when not already appending
                if (!isAppending && index == section.items.lastIndex) {
                    LaunchedEffect(section.id, index) {
                        onLoadMore(section.id)
                    }
                }

                ContentCard(
                    content = content,
                    hazeState = hazeState,
                    onClick = { onContentClick(content.id) },
                )
            }

            // Shimmer skeleton at the end while appending
            if (isAppending) {
                item {
                    ShimmerSkeletonCard()
                }
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
            .height(240.dp),
        style = GlassDefaults.regular,
        shape = RoundedCornerShape(18.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(18.dp)),
        ) {
            // Full-card poster image
            if (content.thumbnailUrl != null) {
                AsyncImage(
                    model = content.thumbnailUrl,
                    contentDescription = content.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = content.typeName.asString(),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }

            // Bottom scrim overlay + text
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(0xCC000000),
                            )
                        )
                    )
                    .padding(horizontal = 10.dp, vertical = 8.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp),
                ) {
                    Text(
                        text = content.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )

                    if (content.year.isNotEmpty() || content.rating.isNotEmpty()) {
                        Text(
                            text = listOfNotNull(
                                content.year.takeIf { it.isNotEmpty() },
                                content.rating.takeIf { it.isNotEmpty() }?.let { "★ $it" },
                            ).joinToString(" · "),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFFFBBF24),
                        )
                    }

                    Text(
                        text = content.typeName.asString(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF93C5FD),
                    )
                }
            }
        }
    }
}

// Empty state

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
            text = "Nothing here yet",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
        Text(
            text = error?.asString() ?: "Try refreshing or check your connection.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF9CA3AF),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

// Shimmer skeleton loader for "load more"

@Composable
private fun ShimmerSkeletonCard(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(160.dp)
            .height(240.dp)
            .clip(RoundedCornerShape(18.dp))
            .shimmerSkeleton(),
    )
}

private fun Modifier.shimmerSkeleton(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "skeletonShimmer")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
            )
        ),
        label = "skeletonAnim",
    )

    val baseColor = Color(0xFF1F2937)
    val highlightColor = Color(0xFF4B5563)

    drawWithContent {
        drawRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    baseColor,
                    highlightColor,
                    baseColor,
                ),
                start = androidx.compose.ui.geometry.Offset(size.width * translateAnim.value, 0f),
                end = androidx.compose.ui.geometry.Offset(
                    size.width * (translateAnim.value + 0.5f),
                    size.height
                ),
            )
        )
    }
}

// Minimal design primitives

private class HazeState

@Composable
private fun GradientBackground(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0F172A),
                        Color(0xFF1E293B),
                        Color(0xFF020617),
                    )
                )
            ),
    )
}

@Composable
private fun GlassTopBar(
    title: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = Color.White.copy(alpha = 0.10f),
        tonalElevation = 10.dp,
        shadowElevation = 10.dp,
    ) {
        TopAppBar(
            title = title,
            actions = { actions() },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                titleContentColor = Color.White,
                actionIconContentColor = Color.White,
            ),
        )
    }
}

private object GlassDefaults {
    val regularElevation: Dp = 10.dp
    val regularAlpha: Float = 0.20f

    val regular: GlassStyle = GlassStyle(
        elevation = regularElevation,
        alpha = regularAlpha,
    )
}

private data class GlassStyle(
    val elevation: Dp,
    val alpha: Float,
)

@Composable
private fun GlassCard(
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    style: GlassStyle = GlassDefaults.regular,
    shape: Shape = RectangleShape,
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(onClick = onClick),
        color = Color.White.copy(alpha = style.alpha),
        tonalElevation = style.elevation,
        shadowElevation = style.elevation,
        shape = shape,
    ) {
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.04f)),
        ) {
            content()
        }
    }
}
