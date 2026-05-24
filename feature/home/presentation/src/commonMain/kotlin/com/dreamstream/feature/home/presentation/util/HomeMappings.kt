package com.dreamstream.feature.home.presentation.util

import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.model.search.displayRating
import com.dreamstream.core.model.search.year
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.domain.error.HomeError
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

/**
 * Maps a [SearchResult] to a [ContentUi].
 *
 * [ContentUi.id] is set to [SearchResult.url] — the provider-side stable
 * identifier — so the detail screen can look up the full [com.dreamstream.core.model.detail.ContentDetail] by URL.
 *
 * Year and rating are extracted via shared extensions on [SearchResult].
 */
internal fun SearchResult.toContentUi(): ContentUi = ContentUi(
    id = url,
    title = name,
    thumbnailUrl = posterUrl,
    typeName = type.displayName,
    year = year?.toString() ?: "",
    rating = displayRating,
)

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
