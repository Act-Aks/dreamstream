package com.dreamstream.feature.search.presentation.util

import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.model.search.displayRating
import com.dreamstream.core.model.search.year
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.search.domain.error.SearchError
import com.dreamstream.feature.search.presentation.model.SearchResultUi
import com.dreamstream.feature.search.presentation.resources.Res
import com.dreamstream.feature.search.presentation.resources.search_error_search_failed

// ─────────────────────────────────────────────────────────────────────────────
// Domain → UI mappers
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Maps a [SearchResult] to a [SearchResultUi].
 *
 * [SearchResultUi.id] is set to [SearchResult.url] — the provider-side stable
 * identifier — so the detail screen can look up the full
 * [com.dreamstream.core.model.detail.ContentDetail] by URL.
 *
 * Year and rating are extracted via shared extensions on [SearchResult].
 */
internal fun SearchResult.toSearchResultUi(): SearchResultUi = SearchResultUi(
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
 * Maps a [SearchError] to a user-facing [UiText] backed by a localized string resource.
 */
internal fun SearchError.toUiText(): UiText = when (this) {
    SearchError.SearchFailed -> UiText.StringResourceId(Res.string.search_error_search_failed)
}
