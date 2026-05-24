package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/**
 * High-level classification of a piece of media content.
 *
 * [displayName] is the human-readable label used in section headers, card
 * badges, and detail screens (e.g. "Movie", "TV Series", "Documentary").
 *
 * [isEpisodic] is true for types that are structured as a series of episodes.
 */
@Serializable
enum class ContentType {
    Movie,
    TvSeries,
    Anime,
    AnimeMovie,
    Documentary,
    Live,
    Music,
    Others;

    val displayName: String
        get() = when (this) {
            Movie -> "Movie"
            TvSeries -> "TV Series"
            Anime -> "Anime"
            AnimeMovie -> "Anime Movie"
            Documentary -> "Documentary"
            Live -> "Live"
            Music -> "Music"
            Others -> "Others"
        }

    val isEpisodic: Boolean
        get() = this == TvSeries || this == Anime
}
