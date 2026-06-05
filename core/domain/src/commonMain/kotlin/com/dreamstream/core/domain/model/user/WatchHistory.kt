package com.dreamstream.core.domain.model.user

import com.dreamstream.core.domain.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Watch history entry tracking playback progress for a content item.
 *
 * [WatchHistory] represents the user's playback state for an anime episode,
 * movie, series episode, or live stream. It is managed by ***`WatchHistoryManager`***
 * and displayed in the **Continue Watching** row, **History** screen, and
 * resurrection logic for resuming playback.
 *
 * This class contains:
 * - **Identity**: [id], [providerId], [url]
 * - **Metadata**: [title], [posterUrl], [type], [season], [episode], [episodeName]
 * - **Playback**: [watchPositionMs], [totalDurationMs], [episodeData]
 * - **Timestamps**: [lastWatchedAt], [createdAt]
 * - **Computed**: [percentageWatched], [isFinished], [displayProgress]
 *
 * ## Key Properties:
 * - [id]: Unique history entry ID (***required, auto-generated***)
 * - [url]: Content lookup key (***required***)
 * - [title]: Content title (***required***)
 * - [watchPositionMs]: Current playback position in milliseconds (***required, defaults to 0**_)
 * - [totalDurationMs]: Total duration in milliseconds (***required, defaults to 0**_)
 * - [lastWatchedAt]: Last playback timestamp (***required**_)
 *
 * ## Playback Progress:
 * [watchPositionMs] is the current playback position (in milliseconds):
 * - `0L` = not started
 * - Example: `125000L` = 2 minutes 5 seconds
 *
 * [totalDurationMs] is the total content duration (in milliseconds):
 * - `0L` = unknown (live stream or metadata not loaded)
 * - Example: `1440000L` = 24 minutes (typical anime episode)
 *
 * ## Computed Properties:
 * ### [percentageWatched]:
 * ```kotlin
 * val percentageWatched: Float
 *     get() = if (totalDurationMs > 0) {
 *         (watchPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
 *     } else 0f
 * ```
 * - Returns `0.0f`–`1.0f` (coerced to prevent >100%)
 * - Returns `0f` if [totalDurationMs] is `0` (unknown duration)
 *
 * ### [isFinished]:
 * ```kotlin
 * val isFinished: Boolean get() = percentageWatched >= 0.9f
 * ```
 * - Returns `true` if ≥90% watched
 * - Used to mark content as "completed" and remove from Continue Watching
 *
 * ### [displayProgress]:
 * ```kotlin
 * val displayProgress: String
 *     get() = "${(percentageWatched * 100).toInt()}%"
 * ```
 * - Returns formatted percentage (e.g., `"45%"`, `"100%"`)
 * - Used in UI progress labels
 *
 * ## Episode Metadata:
 * For episodic content (anime/series):
 * - [season]: Season number (1-based, null if unknown)
 * - [episode]: Episode number (1-based, null if unknown)
 * - [episodeName]: Episode title (e.g., `"The Beginning"`, null if unknown)
 * - [episodeData]: Provider-specific episode lookup key (JSON or serialized data)
 *
 * ## Timestamps:
 * - [lastWatchedAt]: Unix epoch (ms) when playback was last updated.
 *   Used for sorting Continue Watching (newest first).
 *   Example: `1716945600000L` = May 29, 2024 00:00:00 UTC
 * - [createdAt]: Unix epoch (ms) when history entry was first created.
 *   Used for tie-breaking or original bookmark time.
 *
 * ## Usage:
 * ```kotlin
 * // Create history entry on first play
 * val history = WatchHistory(
 *     id = "hist_1716945600000_abc",
 *     providerId = "anime-provider",
 *     url = "anime://attack-on-titan",
 *     title = "Attack on Titan",
 *     posterUrl = "https://cdn.example.com/posters/aot.jpg",
 *     type = ContentType.Anime,
 *     season = 1,
 *     episode = 1,
 *     episodeName = "To You, in 2000 Years",
 *     watchPositionMs = 0L,
 *     totalDurationMs = 1440000L,
 *     lastWatchedAt = System.currentTimeMillis(),
 *     createdAt = System.currentTimeMillis()
 * )
 *
 * // Update position during playback
 * history.copy(watchPositionMs = 720000L) // 50% watched
 *
 * // Check if finished
 * if (history.isFinished) {
 *     markAsCompleted(history.url)
 *     removeFromContinueWatching(history.id)
 * }
 *
 * // Display progress
 * Text(text = history.displayProgress) // "50%"
 * ```
 *
 * ## Continue Watching Flow:
 * 1. User starts playback → create/update [WatchHistory]
 * 2. Periodically save position (every 10s or on seek)
 * 3. On pause/exit → update [lastWatchedAt] and [watchPositionMs]
 * 4. On app start → load history with [isFinished] = `false`
 * 5. Display in **Continue Watching** row (sorted by [lastWatchedAt] desc)
 * 6. User taps → resume from [watchPositionMs]
 * 7. If [isFinished] → remove from Continue Watching
 *
 * ## Sorting & Filtering:
 * ```kotlin
 * // Get continue watching (not finished, sorted by last watched)
 * val continueWatching = historyList
 *     .filter { !it.isFinished && it.type != ContentType.Live }
 *     .sortedByDescending { it.lastWatchedAt }
 *
 * // Get history by type
 * val animeHistory = historyList.filter { it.type == ContentType.Anime }
 *
 * // Get recently watched (last 7 days)
 * val weekAgo = System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000
 * val recent = historyList.filter { it.lastWatchedAt > weekAgo }
 * ```
 *
 * ## Resuming Playback:
 * ```kotlin
 * suspend fun resumePlayback(history: WatchHistory) {
 *     val detail = contentProvider.loadDetail(history.url)
 *     val streams = detail.getStreams(history.season, history.episode)
 *
 *     player.seekTo(history.watchPositionMs)
 *     player.play()
 *
 *     // Show resume dialog if watched >10%
 *     if (history.percentageWatched > 0.1f) {
 *         showDialog("Resume from ${history.displayProgress}?")
 *     }
 * }
 * ```
 *
 * ## UI Representation:
 * - **Continue Watching Row**:
 *   - [posterUrl] as thumbnail
 *   - [title] as main text
 *   - [episodeName] or `S${season}E${episode}` as subtitle
 *   - Progress bar with [displayProgress] overlay
 *   - [lastWatchedAt] as "Watched 2h ago"
 *
 * - **History Screen**:
 *   - Full list sorted by [lastWatchedAt]
 *   - Filter by [type] (Anime/Movie/Series)
 *   - Swipe to delete
 *
 * ## Stale Metadata Handling:
 * Metadata ([title], [posterUrl]) is captured at history creation time.
 * Update on load if content metadata changes:
 * ```kotlin
 * suspend fun refreshHistoryMetadata(history: WatchHistory): WatchHistory {
 *     val detail = contentProvider.loadDetail(history.url)
 *     return history.copy(
 *         title = detail.name,
 *         posterUrl = detail.posterUrl
 *     )
 * }
 * ```
 *
 * ## Related:
 * - Manager: ***`WatchHistoryManager`***
 * - Bookmark: [Bookmark]
 * - Categories: [BookmarkCategory]
 * - Content type: [ContentType]
 * - Detail model: ***`com.dreamstream.core.model.detail.ContentDetail`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class WatchHistory(
    /**
     * Unique history entry identifier.
     *
     * ***Required, auto-generated***. Unique ID for this history entry.
     * Used for history-specific operations (delete, update, resurrection).
     * Example: `"hist_1716945600000_abc123"`
     */
    val id: String,

    /**
     * Source plugin/provider ID.
     *
     * ***Required***. Identifies which plugin provided this content.
     * Used to load content details and route to correct provider.
     * Example: `"anime-provider"`, `"movie-plugin"`
     */
    val providerId: String,

    /**
     * Provider-side content URL.
     *
     * ***Required***. Lookup key for loading content details. **Never changes**
     * after history entry is created. Used to resume playback and refresh metadata.
     * Example: `"anime-provider://attack-on-titan"`
     */
    val url: String,

    /**
     * Content title.
     *
     * ***Required***. Display name captured at history creation time. May become
     * stale if content is renamed by the provider. Consider refreshing on load.
     * Example: `"Attack on Titan"`, `"Inception"`
     */
    val title: String,

    /**
     * Poster/thumbnail image URL.
     *
     * ***Optional***. Null if unavailable. Captured at history creation time.
     * May become stale. Lazy-loaded in Continue Watching row.
     * Example: `"https://cdn.example.com/posters/aot.jpg"`
     */
    val posterUrl: String? = null,

    /**
     * Content type.
     *
     * ***Required***. Type of content (Anime, Movie, Series, Live).
     * Used for filtering and routing in history screens.
     */
    val type: ContentType,

    /**
     * Episode title (for episodic content).
     *
     * ***Optional***. Null if not available or for movies.
     * Example: `"To You, in 2000 Years"`, `"Pilot"`
     */
    val episodeName: String? = null,

    /**
     * Season number (1-based).
     *
     * ***Optional***. Null if not applicable (movie) or unknown.
     * Example: `1`, `2`, `3`
     */
    val season: Int? = null,

    /**
     * Episode number (1-based).
     *
     * ***Optional***. Null if not applicable (movie) or unknown.
     * Example: `1`, `12`, `24`
     */
    val episode: Int? = null,

    /**
     * Provider-specific episode lookup data.
     *
     * ***Optional***. Null if not required. Serialized JSON or provider key
     * for episode resolution (e.g., episode ID, slug).
     * Example: `"{"episodeId":"ep-1","slug":"pilot"}"`
     */
    val episodeData: String? = null,

    /**
     * Current playback position in milliseconds.
     *
     * ***Required***. Defaults to `0L` (not started).
     * Updated periodically during playback and on pause/exit.
     * Example: `0L` (not started), `720000L` (50% of 24min episode)
     */
    val watchPositionMs: Long = 0L,

    /**
     * Total content duration in milliseconds.
     *
     * ***Required***. Defaults to `0L` (unknown).
     * `0L` for live streams or metadata not yet loaded.
     * Example: `1440000L` (24 minutes), `7200000L` (2 hours)
     */
    val totalDurationMs: Long = 0L,

    /**
     * Last playback update timestamp (Unix epoch, milliseconds).
     *
     * ***Required***. When playback was last updated (paused, seeked, or exited).
     * Used for sorting Continue Watching (newest first).
     * Example: `1716945600000L` = May 29, 2024 00:00:00 UTC
     */
    val lastWatchedAt: Long,

    /**
     * History entry creation timestamp (Unix epoch, milliseconds).
     *
     * ***Required***. When this history entry was first created.
     * Used for tie-breaking or original watch time.
     * Example: `1716859200000L` = May 28, 2024 00:00:00 UTC
     */
    val createdAt: Long,
) {
    /**
     * Playback progress as a fraction (0.0–1.0).
     *
     * ***Computed***. Returns `watchPositionMs / totalDurationMs`, coerced to `0f–1f`.
     * Returns `0f` if [totalDurationMs] is `0` (unknown duration).
     *
     * Examples:
     * ```kotlin
     * watchPositionMs = 720000L, totalDurationMs = 1440000L → 0.5f (50%)
     * watchPositionMs = 0L → 0.0f
     * totalDurationMs = 0L → 0.0f
     * ```
     */
    val percentageWatched: Float
        get() = if (totalDurationMs > 0) {
            (watchPositionMs.toFloat() / totalDurationMs.toFloat()).coerceIn(0f, 1f)
        } else 0f

    /**
     * Whether the content is considered finished (≥90% watched).
     *
     * ***Computed***. Returns `true` if [percentageWatched] ≥ `0.9f`.
     * Used to mark content as completed and remove from Continue Watching.
     */
    val isFinished: Boolean get() = percentageWatched >= 0.9f

    /**
     * Formatted progress string for UI display, e.g. `"50%"`.
     *
     * ***Computed***. Returns percentage as integer with `%` suffix.
     * Uses [percentageWatched] for calculation.
     *
     * Examples:
     * ```kotlin
     * percentageWatched = 0.5f → "50%"
     * percentageWatched = 0.95f → "95%"
     * percentageWatched = 0.0f → "0%"
     * ```
     */
    val displayProgress: String
        get() = "${(percentageWatched * 100).toInt()}%"
}
