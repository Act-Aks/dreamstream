package com.dreamstream.core.model.detail

import kotlinx.serialization.Serializable

/**
 * Broadcast or streaming release status of a series or anime title.
 *
 * Used by [SeriesDetail] and [AnimeDetail] to communicate whether a show is
 * still airing, has concluded, or is temporarily paused.
 *
 * [displayName] provides a human-readable label suitable for display in the UI.
 */
@Serializable
enum class ShowStatus {
    /** The series is currently airing or releasing new episodes. */
    Ongoing,

    /** The series has concluded and no further episodes are expected. */
    Completed,

    /** The series has been cancelled before a natural conclusion. */
    Cancelled,

    /** The series is on an indefinite or announced break. */
    Hiatus,

    /** Status is not known or was not reported by the provider. */
    Unknown;

    val displayName: String
        get() = when (this) {
            Ongoing -> "Ongoing"
            Completed -> "Completed"
            Cancelled -> "Cancelled"
            Hiatus -> "On Hiatus"
            Unknown -> "Unknown"
        }
}
