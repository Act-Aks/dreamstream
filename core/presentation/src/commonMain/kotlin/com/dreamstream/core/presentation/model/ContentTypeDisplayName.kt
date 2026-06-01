package com.dreamstream.core.presentation.model

import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.presentation.resources.CoreRes
import com.dreamstream.core.presentation.resources.anime
import com.dreamstream.core.presentation.resources.anime_movie
import com.dreamstream.core.presentation.resources.documentary
import com.dreamstream.core.presentation.resources.live
import com.dreamstream.core.presentation.resources.movie
import com.dreamstream.core.presentation.resources.music
import com.dreamstream.core.presentation.resources.others
import com.dreamstream.core.presentation.resources.tv_series
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.core.presentation.ui.UiText.StringResourceId

/**
 * Returns a localized, human-readable label for this [ContentType].
 *
 * This property is used in section headers, content card badges, filter chips,
 * and detail screens to describe the type of content (e.g., "Movie", "TV Series", "Anime").
 *
 * Mappings:
 * - [ContentType.Anime] → localized "Anime"
 * - [ContentType.AnimeMovie] → localized "Anime Movie"
 * - [ContentType.Documentary] → localized "Documentary"
 * - [ContentType.Live] → localized "Live"
 * - [ContentType.Movie] → localized "Movie"
 * - [ContentType.Music] → localized "Music"
 * - [ContentType.Others] → localized "Others"
 * - [ContentType.TvSeries] → localized "TV Series"
 *
 * All labels are resolved from localized string resources supporting English,
 * Hindi, German, and Japanese.
 *
 * @return A [UiText] wrapping the corresponding localized string resource ID.
 */
val ContentType.displayName: UiText
    get() = when (this) {
        ContentType.Anime -> StringResourceId(CoreRes.string.anime)
        ContentType.AnimeMovie -> StringResourceId(CoreRes.string.anime_movie)
        ContentType.Documentary -> StringResourceId(CoreRes.string.documentary)
        ContentType.Live -> StringResourceId(CoreRes.string.live)
        ContentType.Movie -> StringResourceId(CoreRes.string.movie)
        ContentType.Music -> StringResourceId(CoreRes.string.music)
        ContentType.Others -> StringResourceId(CoreRes.string.others)
        ContentType.TvSeries -> StringResourceId(CoreRes.string.tv_series)
    }
