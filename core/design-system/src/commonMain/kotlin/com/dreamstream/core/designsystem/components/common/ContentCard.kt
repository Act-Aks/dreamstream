package com.dreamstream.core.designsystem.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.BookmarkAdded
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.dreamstream.core.designsystem.components.badges.QualityBadge
import com.dreamstream.core.designsystem.components.glass.GlassCard
import com.dreamstream.core.designsystem.components.glass.GradientBackground
import com.dreamstream.core.designsystem.theme.DreamStreamTheme
import com.dreamstream.core.designsystem.theme.GlassDefaults
import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.search.MovieResult
import com.dreamstream.core.domain.model.search.SearchResult
import com.dreamstream.core.domain.model.search.SeriesResult
import dev.chrisbanes.haze.HazeState

@Composable
fun ContentCard(
    item: SearchResult,
    onClick: () -> Unit,
    hazeState: HazeState,
    modifier: Modifier = Modifier,
    onBookmarkClick: (() -> Unit)? = null,
    isBookmarked: Boolean = false,
    watchProgress: Float = 0f,
    showProgress: Boolean = true,
) {
    GlassCard(
        hazeState = hazeState,
        onClick = onClick,
        modifier = modifier,
        style = GlassDefaults.regular,
        shape = DreamStreamTheme.shapes.small,
        contentPadding = PaddingValues(DreamStreamTheme.spacing.zero),
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current).data(item.posterUrl)
                .crossfade(true).build(),
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.TopStart)) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                QualityBadge(
                    quality = (item as? MovieResult)?.quality ?: Quality.Unknown,
                    modifier = Modifier.padding(6.dp),
                    hazeState = hazeState,
                )
                if (onBookmarkClick != null) {
                    BookmarkButton(
                        isBookmarked = isBookmarked,
                        onBookmarkClick = onBookmarkClick,
                    )
                }
            }
        }
        Box(modifier = Modifier.fillMaxWidth().align(Alignment.BottomStart)) {
            Column {
                MetaData(item = item)
                if (showProgress && watchProgress > 0f) {
                    ProgressIndicator(progress = watchProgress)
                }
            }
        }
    }
}

@Composable
private fun BookmarkButton(
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit,
) {
    IconButton(
        onClick = onBookmarkClick, modifier = Modifier.size(36.dp)
    ) {
        Icon(
            imageVector = if (isBookmarked) Icons.Filled.BookmarkAdded else Icons.Filled.BookmarkAdd,
            contentDescription = if (isBookmarked) "Remove bookmark" else "Add bookmark",
            tint = if (isBookmarked) DreamStreamTheme.materialColors.primary else Color.White,
            modifier = Modifier.size(20.dp),
        )
    }
}

@Composable
private fun MetaData(item: SearchResult) {
    Column(modifier = Modifier.padding(DreamStreamTheme.spacing.sm)) {
        Text(
            text = item.name,
            style = MaterialTheme.typography.bodySmall,
            color = DreamStreamTheme.colors.textPrimary,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        val year = when (item) {
            is MovieResult -> item.year
            is SeriesResult -> item.year
            else -> null
        }
        year?.let {
            Text(
                text = it.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = DreamStreamTheme.colors.textSecondary,
            )
        }
    }
}


@Composable
private fun ProgressIndicator(progress: Float) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier.fillMaxWidth().height(5.dp).clip(shape = DreamStreamTheme.shapes.large)
            .background(DreamStreamTheme.materialColors.inversePrimary),
        color = DreamStreamTheme.materialColors.primary,
        trackColor = DreamStreamTheme.materialColors.inversePrimary,
        strokeCap = StrokeCap.Round,
        gapSize = (-5 / 2).dp,    // Negative half the height
        drawStopIndicator = {},         // Hide the stop indicator
    )
}

@Preview(showBackground = true)
@Composable
private fun ContentCardPreview() {
    DreamStreamTheme {
        val hazeState = remember { HazeState() }
        val content = MovieResult(
            name = "Forza Horizon 5",
            url = "/movie/forza5",
            providerId = "ds",
            posterUrl = "https://example.com/forza5.jpg",
            quality = Quality.FourK,
            year = 2021,
        )
        GradientBackground(hazeState = hazeState) {
            ContentCard(
                item = content,
                onClick = {},
                hazeState = hazeState,
                onBookmarkClick = {},
                watchProgress = 0.3f
            )
        }
    }
}
