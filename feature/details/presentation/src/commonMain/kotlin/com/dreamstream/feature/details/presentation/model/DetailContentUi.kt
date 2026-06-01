package com.dreamstream.feature.details.presentation.model

import com.dreamstream.core.presentation.ui.UiText

/**
 * UI representation of [DetailContent] — all display formatting is done in
 * the mapper so the composable receives ready-to-render strings.
 */
data class DetailContentUi(
    val contentId: String,
    val title: String,
    val synopsis: String,
    val thumbnailUrl: String?,
    val backdropUrl: String?,
    val typeName: UiText,
    val year: String,
    val rating: String,
    val genres: List<String>,
    /** Pre-formatted duration, e.g. "2h 7m", "45m", or "" when unknown. */
    val duration: String,
)
