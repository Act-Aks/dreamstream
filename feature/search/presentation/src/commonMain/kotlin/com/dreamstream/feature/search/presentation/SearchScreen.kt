package com.dreamstream.feature.search.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.input.ImeAction
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
import com.dreamstream.core.presentation.resources.action_back
import com.dreamstream.core.presentation.resources.action_clear
import com.dreamstream.core.presentation.ui.ObserveAsEvents
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.search.presentation.model.SearchResultUi
import com.dreamstream.feature.search.presentation.resources.Res
import com.dreamstream.feature.search.presentation.resources.search_hint
import com.dreamstream.feature.search.presentation.resources.search_no_results_heading
import com.dreamstream.feature.search.presentation.resources.search_no_results_hint
import com.dreamstream.feature.search.presentation.resources.search_prompt_heading
import com.dreamstream.feature.search.presentation.resources.search_prompt_message
import dev.chrisbanes.haze.HazeState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

// ─────────────────────────────────────────────────────────────────────────────
// Root — injects ViewModel, observes events
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SearchRoot(
    onNavigateToDetail: (String) -> Unit,
    onNavigateBack: () -> Unit,
    viewModel: SearchViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SearchEvent.NavigateToDetail -> onNavigateToDetail(event.contentId)
            is SearchEvent.NavigateBack -> onNavigateBack()
        }
    }

    SearchScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Screen — pure state + callbacks, previewable
// ─────────────────────────────────────────────────────────────────────────────

@Composable
fun SearchScreen(
    state: SearchState,
    onAction: (SearchAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val hazeState = remember { HazeState() }
    val focusRequester = remember { FocusRequester() }

    // Auto-focus the search field when the screen first appears.
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(modifier = modifier.fillMaxSize()) {
        GradientBackground(hazeState = hazeState)

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                GlassTopBar(
                    title = {
                        SearchTextField(
                            query = state.query,
                            onQueryChange = { onAction(SearchAction.OnQueryChange(it)) },
                            onClearQuery = { onAction(SearchAction.OnClearQuery) },
                            focusRequester = focusRequester,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    },
                    hazeState = hazeState,
                    navigationIcon = {
                        IconButton(onClick = { onAction(SearchAction.OnBackClick) }) {
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
                        SearchErrorState(
                            message = state.error.asString(),
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    state.query.isNotBlank() && state.results.isEmpty() -> {
                        NoResultsState(
                            query = state.query,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    state.results.isNotEmpty() -> {
                        SearchResultList(
                            results = state.results,
                            hazeState = hazeState,
                            onResultClick = { id -> onAction(SearchAction.OnResultClick(id)) },
                        )
                    }

                    else -> {
                        SearchPromptState(modifier = Modifier.align(Alignment.Center))
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
private fun SearchTextField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier,
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.focusRequester(focusRequester),
        placeholder = {
            Text(
                text = stringResource(Res.string.search_hint),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.5f),
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.7f),
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = onClearQuery) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(CoreRes.string.action_clear),
                        tint = Color.White.copy(alpha = 0.7f),
                    )
                }
            }
        } else null,
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = { /* IME search — already triggered by query change */ }),
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
        ),
    )
}

@Composable
private fun SearchResultList(
    results: List<SearchResultUi>,
    hazeState: HazeState,
    onResultClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(
            items = results,
            key = { it.id },
        ) { result ->
            SearchResultRow(
                result = result,
                hazeState = hazeState,
                onClick = { onResultClick(result.id) },
            )
        }
    }
}

@Composable
private fun SearchResultRow(
    result: SearchResultUi,
    hazeState: HazeState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    GlassCard(
        hazeState = hazeState,
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        style = GlassDefaults.thin,
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Thumbnail placeholder — replaced with AsyncImage once Coil is wired.
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .graphicsLayer { alpha = 0.5f },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = result.typeName.asString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = result.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                val meta = listOfNotNull(
                    result.year.takeIf { it.isNotEmpty() },
                    result.rating.takeIf { it.isNotEmpty() }?.let { "★ $it" },
                ).joinToString(" · ")
                if (meta.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = meta,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchPromptState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.search_prompt_heading),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
        Text(
            text = stringResource(Res.string.search_prompt_message),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun NoResultsState(
    query: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = stringResource(Res.string.search_no_results_heading, query),
            style = MaterialTheme.typography.titleMedium,
            color = Color.White,
        )
        Text(
            text = stringResource(Res.string.search_no_results_hint),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchErrorState(
    message: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = message,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.error,
        modifier = modifier.padding(32.dp),
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Previews
// ─────────────────────────────────────────────────────────────────────────────

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun SearchScreenEmptyPreview() {
    DreamStreamTheme {
        SearchScreen(
            state = SearchState(),
            onAction = {},
        )
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun SearchScreenLoadingPreview() {
    DreamStreamTheme {
        SearchScreen(
            state = SearchState(query = "cosmic", isLoading = true),
            onAction = {},
        )
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun SearchScreenNoResultsPreview() {
    DreamStreamTheme {
        SearchScreen(
            state = SearchState(query = "xyzzy", results = emptyList()),
            onAction = {},
        )
    }
}

@Preview(showBackground = false, widthDp = 360, heightDp = 780)
@Composable
private fun SearchScreenResultsPreview() {
    DreamStreamTheme {
        SearchScreen(
            state = SearchState(
                query = "cosmic",
                results = listOf(
                    SearchResultUi(
                        id = "t1",
                        title = "Cosmic Drift",
                        thumbnailUrl = null,
                        typeName = UiText.DynamicString("Movie"),
                        year = "2024",
                        rating = "8.2",
                    ),
                    SearchResultUi(
                        id = "t3",
                        title = "The Last Horizon",
                        thumbnailUrl = null,
                        typeName = UiText.DynamicString("Movie"),
                        year = "2024",
                        rating = "9.0",
                    ),
                ),
            ),
            onAction = {},
        )
    }
}
