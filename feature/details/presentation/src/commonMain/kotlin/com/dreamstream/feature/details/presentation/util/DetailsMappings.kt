package com.dreamstream.feature.details.presentation.util

import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.domain.model.DetailContent
import com.dreamstream.feature.details.domain.model.DetailMediaType
import com.dreamstream.feature.details.presentation.model.DetailContentUi

// ─────────────────────────────────────────────────────────────────────────────
// Domain → UI mappers
// ─────────────────────────────────────────────────────────────────────────────

internal fun DetailContent.toDetailContentUi(): DetailContentUi = DetailContentUi(
    contentId = contentId,
    title = title,
    synopsis = synopsis,
    thumbnailUrl = thumbnailUrl,
    backdropUrl = backdropUrl,
    typeName = type.toDisplayName(),
    year = year?.toString() ?: "",
    rating = rating?.let { "%.1f".format(it) } ?: "",
    genres = genres,
    duration = durationMinutes.toDurationString(),
)

private fun DetailMediaType.toDisplayName(): String = when (this) {
    DetailMediaType.Movie -> "Movie"
    DetailMediaType.Series -> "Series"
    DetailMediaType.Anime -> "Anime"
    DetailMediaType.Documentary -> "Documentary"
    DetailMediaType.Short -> "Short"
}

private fun Int?.toDurationString(): String {
    if (this == null) return ""
    val hours = this / 60
    val minutes = this % 60
    return when {
        hours == 0 -> "${minutes}m"
        minutes == 0 -> "${hours}h"
        else -> "${hours}h ${minutes}m"
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Error → UiText mapper
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Maps a [DetailsError] to a user-facing [UiText].
 *
 * TODO: Replace [UiText.DynamicString] with [UiText.StringResourceId] once
 * string resources are added to :feature:details:presentation.
 */
internal fun DetailsError.toUiText(): UiText = when (this) {
    DetailsError.NotFound -> UiText.DynamicString("Content not found.")
    DetailsError.LoadFailed -> UiText.DynamicString("Failed to load content. Please try again.")
}
