package com.dreamstream.core.designsystem.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.search.MovieResult
import com.dreamstream.core.domain.model.search.SearchResult
import dev.chrisbanes.haze.HazeState

@Composable
fun ContentRow(
    title: String,
    items: List<SearchResult>,
    onItemClick: (SearchResult) -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onSeeAllClick: (() -> Unit)? = null,
    onBookmarkClick: ((SearchResult) -> Unit)? = null,
    bookmarkedUrls: Set<String> = emptySet(),
    watchProgressMap: Map<String, Float> = emptyMap(),
    isLoading: Boolean = false,
    itemWidth: Dp = 110.dp,
    contentPadding: PaddingValues = PaddingValues(
        horizontal = DreamStreamTheme.spacing.padScreen
    ),
) {
    val listState = rememberLazyListState()

    Column(
        modifier = modifier.fillMaxWidth().aspectRatio(3f / 2f)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(
                horizontal = DreamStreamTheme.spacing.padScreen,
                vertical = DreamStreamTheme.spacing.px,
            ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            if (onSeeAllClick != null) {
                TextButton(onClick = onSeeAllClick) {
                    Text(
                        text = "See all",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.padding(start = 2.dp).height(14.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.px))

        // Horizontal list
        LazyRow(
            state = listState,
            horizontalArrangement = Arrangement.spacedBy(DreamStreamTheme.spacing.xs),
            contentPadding = contentPadding,
        ) {
            if (isLoading) {
                items(6) {
                    ContentCardSkeleton(modifier = Modifier.width(itemWidth))
                }
            } else {
                items(
                    items = items,
                    key = { "${it.providerId}_${it.url}" },
                ) { item ->
                    ContentCard(
                        modifier = Modifier.width(itemWidth),
                        hazeState = hazeState,
                        isBookmarked = item.url in bookmarkedUrls,
                        item = item,
                        onBookmarkClick = onBookmarkClick?.let { { it(item) } },
                        onClick = { onItemClick(item) },
                        watchProgress = watchProgressMap[item.url] ?: 0f,
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Content Row - Loaded",
    showBackground = true,
)
@Composable
private fun ContentRowLoadedPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        val items = listOf(
            MovieResult(
                name = "Solo Leveling",
                url = "/movie/solo-leveling",
                providerId = "ds",
                posterUrl = "https://example.com/solo.jpg",
                quality = Quality.FullHd,
                year = 2024,
            ),
            MovieResult(
                name = "My Hero Academia",
                url = "/movie/mha",
                providerId = "ds",
                posterUrl = "https://example.com/mha.jpg",
                quality = Quality.HD,
                year = 2016,
            ),
            MovieResult(
                name = "Forza Horizon 5",
                url = "/movie/forza5",
                providerId = "ds",
                posterUrl = "https://example.com/forza5.jpg",
                quality = Quality.FourK,
                year = 2021,
            ),
        )

        GradientBackground(hazeState = hazeState) {
            ContentRow(
                title = "Continue watching",
                items = items,
                hazeState = hazeState,
                onItemClick = {},
                onSeeAllClick = {},
                onBookmarkClick = {},
                bookmarkedUrls = setOf("/movie/mha"),
                watchProgressMap = mapOf(
                    "/movie/solo-leveling" to 0.3f,
                    "/movie/mha" to 0.8f,
                ),
                isLoading = false,
            )
        }
    }
}
