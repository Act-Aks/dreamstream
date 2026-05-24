package com.dreamstream.feature.details.presentation.util

import com.dreamstream.core.model.detail.ContentDetail
import com.dreamstream.core.model.detail.MovieDetail
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.presentation.model.DetailContentUi

// ─────────────────────────────────────────────────────────────────────────────
// Domain → UI mappers
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Maps a [ContentDetail] to a [DetailContentUi].
 *
 * Field mapping:
 * - [ContentDetail.url]                 → [DetailContentUi.contentId]
 * - [ContentDetail.name]                → [DetailContentUi.title]
 * - [ContentDetail.plot]                → [DetailContentUi.synopsis]
 * - [ContentDetail.posterUrl]           → [DetailContentUi.thumbnailUrl]
 * - [ContentDetail.backgroundPosterUrl] → [DetailContentUi.backdropUrl]
 * - [ContentDetail.tags]                → [DetailContentUi.genres]
 * - [MovieDetail.duration]              → [DetailContentUi.duration] (other types → "")
 */
internal fun ContentDetail.toDetailContentUi(): DetailContentUi = DetailContentUi(
    contentId = url,
    title = name,
    synopsis = plot ?: "",
    thumbnailUrl = posterUrl,
    backdropUrl = backgroundPosterUrl,
    typeName = type.displayName,
    year = year?.toString() ?: "",
    rating = displayRating ?: "",
    genres = tags,
    duration = durationMinutes().toDurationString(),
)

/**
 * Extracts the runtime in minutes from subtypes that carry it.
 * Returns null for episodic and live types where a single duration is not meaningful.
 */
private fun ContentDetail.durationMinutes(): Int? = when (this) {
    is MovieDetail -> duration
    else -> null
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
