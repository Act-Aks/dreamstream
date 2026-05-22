package com.dreamstream.feature.home.presentation.util

import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.Content
import com.dreamstream.feature.home.domain.model.ContentType
import com.dreamstream.feature.home.domain.model.HomeSection
import com.dreamstream.feature.home.presentation.model.ContentUi
import com.dreamstream.feature.home.presentation.model.HomeSectionUi

// ─────────────────────────────────────────────────────────────────────────────
// Domain → UI mappers
// ─────────────────────────────────────────────────────────────────────────────

internal fun HomeSection.toHomeSectionUi(): HomeSectionUi = HomeSectionUi(
    id = id,
    title = title,
    items = items.map { it.toContentUi() },
)

internal fun Content.toContentUi(): ContentUi = ContentUi(
    id = id.value,
    title = title,
    description = description,
    thumbnailUrl = thumbnailUrl,
    typeName = type.toDisplayName(),
    year = year?.toString() ?: "",
    rating = rating?.let { "%.1f".format(it) } ?: "",
)

private fun ContentType.toDisplayName(): String = when (this) {
    ContentType.Movie -> "Movie"
    ContentType.Series -> "Series"
    ContentType.Anime -> "Anime"
    ContentType.Documentary -> "Documentary"
    ContentType.Short -> "Short"
}

// ─────────────────────────────────────────────────────────────────────────────
// Error → UiText mapper
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Maps a [HomeError] to a user-facing [UiText].
 *
 * TODO: Replace [UiText.DynamicString] with [UiText.StringResourceId] once
 * string resources are added to :feature:home:presentation.
 */
internal fun HomeError.toUiText(): UiText = when (this) {
    HomeError.NoContentAvailable -> UiText.DynamicString("No content available right now.")
    HomeError.LoadFailed -> UiText.DynamicString("Failed to load content. Please try again.")
}
