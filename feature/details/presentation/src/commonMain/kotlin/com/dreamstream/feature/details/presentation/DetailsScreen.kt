package com.dreamstream.feature.details.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dreamstream.core.designsystem.component.GlassCard
import com.dreamstream.core.designsystem.component.GlassSurface
import com.dreamstream.core.designsystem.component.GlassTopBar
import com.dreamstream.core.designsystem.component.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.presentation.resources.action_back
import com.dreamstream.core.presentation.resources.action_retry
import com.dreamstream.core.presentation.ui.ObserveAsEvents
import com.dreamstream.feature.details.presentation.model.DetailContentUi
import com.dreamstream.feature.details.presentation.resources.Res
import com.dreamstream.feature.details.presentation.resources.details_play_button
import com.dreamstream.feature.details.presentation.resources.details_synopsis_label
import dev.chrisbanes.haze.HazeState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import com.dreamstream.core.presentation.resources.Res as CoreRes

// ─────────────────────────────────────────────────────────────────────────────
// Root — injects ViewModel, observes events
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DetailsRoot(
    contentId: String,
    onNavigateBack: () -> Unit,
    viewModel: DetailsViewModel = koinViewModel(parameters = { parametersOf(contentId) }),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is DetailsEvent.NavigateBack -> onNavigateBack()
        }
    }

    DetailsScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Screen — pure state + callbacks, previewable
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun DetailsScreen(
    state: DetailsState,
    onAction: (DetailsAction) -> Unit,
    modifier: Modifier = Modifier,
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
                        state.content?.let { content ->
                            Text(
                                text = content.title,
                                style = MaterialTheme.typography.titleLarge,
                                color = Color.White,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    },
                    hazeState = hazeState,
                    navigationIcon = {
                        IconButton(onClick = { onAction(DetailsAction.OnBackClick) }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(CoreRes.string.action_back),
                                tint = Color.White,
                            )
                        }
                    },
                )
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }

                    state.error != null -> {
                        DetailsErrorState(
                            message = state.error.asString(),
                            onRetry = { onAction(DetailsAction.OnRetry) },
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    state.content != null -> {
                        DetailsContent(
                            content = state.content,
                            hazeState = hazeState,
                        )
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Private composable helpers
// ─────────────────────────────────────────────────────────────────────────────

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun DetailsContent(
    content: DetailContentUi,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // ── Hero card ─────────────────────────────────────────────────────────
        GlassSurface(
            hazeState = hazeState,
            style = GlassDefaults.regular,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                // Thumbnail placeholder — replaced with AsyncImage once Coil is wired.
                Text(
                    text = content.typeName,
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.graphicsLayer { alpha = 0.6f },
                )
            }
        }

        // ── Metadata row ──────────────────────────────────────────────────────
        GlassSurface(
            hazeState = hazeState,
            style = GlassDefaults.thin,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                val meta = buildList {
                    if (content.year.isNotEmpty()) add(content.year)
                    if (content.rating.isNotEmpty()) add("★ ${content.rating}")
                    if (content.duration.isNotEmpty()) add(content.duration)
                    add(content.typeName)
                }.joinToString("  ·  ")

                Text(
                    text = meta,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                )

                if (content.genres.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        content.genres.forEach { genre ->
                            Text(
                                text = genre,
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White.copy(alpha = 0.7f),
                            )
                        }
                    }
                }
            }
        }

        // ── Synopsis ──────────────────────────────────────────────────────────
        GlassSurface(
            hazeState = hazeState,
            style = GlassDefaults.thin,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(Res.string.details_synopsis_label),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content.synopsis,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.85f),
                )
            }
        }

        // ── Play button ───────────────────────────────────────────────────────
        GlassCard(
            hazeState = hazeState,
            onClick = { /* Playback will be wired once :feature:player is added. */ },
            style = GlassDefaults.thick,
            shape = MaterialTheme.shapes.extraLarge,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.details_play_button),
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun DetailsErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
        )
        GlassCard(
            hazeState = remember { HazeState() },
            onClick = onRetry,
            style = GlassDefaults.regular,
            shape = MaterialTheme.shapes.large,
            modifier = Modifier.size(width = 120.dp, height = 44.dp),
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(CoreRes.string.action_retry),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White,
                )
            }
        }
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun DetailsScreenLoadingPreview() {
    DreamStreamTheme {
        DetailsScreen(
            state = DetailsState(isLoading = true),
            onAction = {},
        )
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun DetailsScreenErrorPreview() {
    DreamStreamTheme {
        DetailsScreen(
            state = DetailsState(
                error = com.dreamstream.core.presentation.ui.UiText.DynamicString("Content not found."),
            ),
            onAction = {},
        )
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun DetailsScreenContentPreview() {
    DreamStreamTheme {
        DetailsScreen(
            state = DetailsState(
                content = DetailContentUi(
                    contentId = "t1",
                    title = "Cosmic Drift",
                    synopsis = "A crew of astronauts navigates the outer reaches of an uncharted galaxy, " +
                        "where the laws of physics bend and the boundaries between dimensions blur.",
                    thumbnailUrl = null,
                    backdropUrl = null,
                    typeName = "Movie",
                    year = "2024",
                    rating = "8.2",
                    genres = listOf("Sci-Fi", "Adventure", "Drama"),
                    duration = "2h 7m",
                ),
            ),
            onAction = {},
        )
    }
}
