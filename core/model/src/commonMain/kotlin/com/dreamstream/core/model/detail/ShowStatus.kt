package com.dreamstream.core.model.detail

import kotlinx.serialization.Serializable

/**
 * Broadcast or streaming release status of a series or anime title.
 *
 * [ShowStatus] communicates whether a show is still airing, has concluded,
 * is cancelled, on hiatus, or unknown. It is used in [SeriesDetail.showStatus]
 * and [AnimeDetail.showStatus] to display release information in the detail screen.
 *
 * ## Key Properties:
 * - Name: Status identifier (***required***)
 *
 * ## Status Values:
 * | Status | Behavior |
 * |--------|----------|
 * | [Ongoing] | Currently airing new episodes |
 * | [Completed] | Concluded, no more episodes |
 * | [Cancelled] | Cut off before natural conclusion |
 * | [Hiatus] | Temporarily paused (indefinite) |
 * | [Unknown] | Status not provided by source |
 *
 * ## Usage:
 * ```kotlin
 * val status = animeDetail.showStatus
 * if (status == ShowStatus.Ongoing) {
 *     // Show "New Episode This Week" badge
 * }
 * ```
 *
 * ## In Content Detail:
 * ```kotlin
 * AnimeDetail(
 *     name = "One Piece",
 *     showStatus = ShowStatus.Ongoing,
 *     // ... other properties
 * )
 *
 * SeriesDetail(
 *     name = "Breaking Bad",
 *     showStatus = ShowStatus.Completed,
 *     // ... other properties
 * )
 * ```
 *
 * ## UI Representation:
 * - Displayed as a badge in the detail header (e.g., "● Live" for Ongoing)
 * - [Completed] shows a checkmark or "Finished" label
 * - [Cancelled] shows an alert icon or "Ended" label
 * - [Hiatus] shows a pause icon or "Paused" label
 * - [Unknown] shows a question mark or no badge
 *
 * ## Related:
 * - Used in:
 *      [SeriesDetail]
 *      [AnimeDetail]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
enum class ShowStatus {
    /**
     * The series is currently airing or releasing new episodes.
     *
     * ***Behavior***: New episodes are released on a schedule (weekly/daily).
     * Display "● Live" or "Airing" badge in the UI.
     */
    Ongoing,

    /**
     * The series has concluded and no further episodes are expected.
     *
     * ***Behavior***: All episodes are available. Full season is complete.
     * Display checkmark or "Finished" badge in the UI.
     */
    Completed,

    /**
     * The series has been cancelled before a natural conclusion.
     *
     * ***Behavior***: Story left unfinished. No new episodes will be released.
     * Display alert icon or "Cancelled" badge in the UI.
     */
    Cancelled,

    /**
     * The series is on an indefinite or announced break.
     *
     * ***Behavior***: Temporarily paused (e.g., production delay).
     * Return date may be unknown. Display pause icon or "Paused" badge.
     */
    Hiatus,

    /**
     * Status is not known or was not reported by the provider.
     *
     * ***Behavior***: Fallback when the provider omits status information.
     * Display no badge or a question mark icon.
     */
    Unknown,
}
