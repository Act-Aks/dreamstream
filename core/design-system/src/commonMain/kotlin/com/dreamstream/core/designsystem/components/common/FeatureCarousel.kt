package com.dreamstream.core.designsystem.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dreamstream.core.designsystem.components.badges.ContentTypeBadge
import com.dreamstream.core.designsystem.components.badges.RatingBadge
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamGradients
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.domain.extensions.toReadableMinutes
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.detail.ContentDetail
import com.dreamstream.core.domain.model.detail.MovieDetail
import com.dreamstream.core.domain.model.detail.SeriesDetail
import dev.chrisbanes.haze.HazeState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

private const val AUTO_SCROLL_DELAY = 2000L


@Composable
fun FeatureCarousel(
    items: List<ContentDetail>,
    onItemClick: (ContentDetail) -> Unit,
    hazeState: HazeState,
    modifier: Modifier,
    onBookmarkClick: ((ContentDetail) -> Unit)? = null,
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { items.size })
    var autoScroll by remember { mutableStateOf(true) }

    // Auto-scroll
    LaunchedEffect(pagerState.currentPage, autoScroll) {
        if (autoScroll && items.size > 1) {
            delay(AUTO_SCROLL_DELAY.milliseconds)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = modifier) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            HeroItem(
                hazeState = hazeState,
                item = items[page],
                onPlayClick = { onItemClick(items[page]) },
                onBookmarkClick = onBookmarkClick?.let { { it(items[page]) } },
            )
        }

        // Page indicators
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = DreamStreamTheme.spacing.xl),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            repeat(items.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(
                            if (isSelected) DreamStreamTheme.materialColors.primary
                            else DreamStreamTheme.materialColors.inversePrimary
                        )
                        .size(
                            width = if (isSelected) 20.dp else 6.dp,
                            height = 6.dp,
                        ),
                )
            }
        }
    }
}

@Composable
private fun HeroItem(
    item: ContentDetail,
    onPlayClick: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onBookmarkClick: (() -> Unit)? = null,
) {
    Box(modifier = modifier.fillMaxSize()) {
        // Backdrop Image
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(item.backgroundPosterUrl ?: item.posterUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        // Multi-layer gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(DreamStreamGradients.heroScrimTopDown)
        )

        // Content
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(DreamStreamTheme.spacing.lg)
                .padding(bottom = DreamStreamTheme.spacing.xxxl),
        ) {
            // Tags / Genre chips
            if (item.tags.isNotEmpty()) {
                Row(horizontalArrangement = Arrangement.spacedBy(DreamStreamTheme.spacing.px)) {
                    item.tags.take(3).forEach { tag ->
                        ContentTypeBadge(hazeState = hazeState, text = tag)
                    }
                }
                Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.xs))
            }

            // Title
            Text(
                text = item.name,
                style = MaterialTheme.typography.headlineMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.px))

            // Meta info row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(DreamStreamTheme.spacing.xs),
            ) {
                item.year?.let {
                    Text(
                        text = it.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.7f),
                    )
                }
                item.rating?.let {
                    Text("•", color = Color.White.copy(alpha = 0.5f))
                    RatingBadge(hazeState = hazeState, rating = it)
                }
                when (item) {
                    is MovieDetail -> item.duration?.let {
                        Text("•", color = Color.White.copy(alpha = 0.5f))
                        Text(
                            text = it.toReadableMinutes(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f),
                        )
                    }

                    is SeriesDetail -> {
                        Text("•", color = Color.White.copy(alpha = 0.5f))
                        Text(
                            text = "${item.seasons.size} Season${if (item.seasons.size != 1) "s" else ""}",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.7f),
                        )
                    }

                    else -> {}
                }
            }

            Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.sm))

            // Plot
            item.plot?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(DreamStreamTheme.spacing.md))
            }

            // Action Buttons
            Row(horizontalArrangement = Arrangement.spacedBy(DreamStreamTheme.spacing.xs)) {
                Button(
                    onClick = onPlayClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp),
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Play",
                        fontWeight = FontWeight.SemiBold,
                    )
                }

                if (onBookmarkClick != null) {
                    FilledTonalButton(
                        onClick = onBookmarkClick,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color.White,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Filled.BookmarkAdd,
                            contentDescription = "Add to list",
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("My List")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeatureCarouselPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        val items = listOf(
            MovieDetail(
                name = "Solo Leveling",
                url = "/movie/solo-leveling",
                providerId = "ds",
                posterUrl = "https://example.com/solo.jpg",
                dataUrl = "",
                backgroundPosterUrl = "https://example.com/solo.jpg",
                type = ContentType.Movie,
                year = 2026,
                plot = "",
                rating = 5f,
                tags = listOf("Movie", "Best of 2026"),
                recommendations = emptyList(),
                actors = emptyList(),
                trailerUrl = "",
                duration = 220,
                comingSoon = true,
            ),
            MovieDetail(
                name = "My Hero Academia",
                url = "/movie/mha",
                providerId = "ds",
                posterUrl = "https://example.com/mha.jpg",
                dataUrl = "",
            ),
            MovieDetail(
                name = "Forza Horizon 5",
                url = "/movie/forza5",
                providerId = "ds",
                posterUrl = "https://example.com/forza5.jpg",
                dataUrl = "",
            ),
        )

        GradientBackground(hazeState = hazeState) {
            FeatureCarousel(
                items = items,
                onItemClick = { },
                hazeState = hazeState,
                modifier = Modifier
            )
        }
    }
}
