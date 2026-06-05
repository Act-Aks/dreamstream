package com.dreamstream.core.domain.model.user

import com.dreamstream.core.domain.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * A user-saved bookmark for content (anime, movie, series, or live channel).
 *
 * [Bookmark] represents a content item that the user has explicitly saved to
 * their library. It is managed by ***`BookmarkManager`*** and displayed in the
 * **Bookmarks/Library** screen. Bookmarks can be organized into custom categories
 * (folders) for better organization.
 *
 * This class contains:
 * - **Identity**: [id], [providerId], [url]
 * - **Metadata**: [title], [posterUrl], [type], [category]
 * - **Timestamp**: [createdAt] (when bookmarked)
 *
 * ## Key Properties:
 * - [id]: Unique bookmark ID (***required, auto-generated***)
 * - [url]: Content lookup key (***required***)
 * - [title]: Content title (***required***)
 * - [type]: Content type (***required***)
 * - [category]: Folder/category name (***optional, defaults to [BookmarkCategory.DEFAULT]***)
 * - [createdAt]: Bookmark timestamp (***required***)
 *
 * ## Identity & Navigation:
 * - [id]: Unique identifier for this bookmark entry (not the content ID).
 *   Used for bookmark-specific operations (rename, delete, move category).
 *   Example: `"bm_1716945600000_abc123"`
 * - [providerId]: Source plugin ID. Used to load content details.
 *   Example: `"anime-provider"`, `"movie-plugin"`
 * - [url]: Provider-side content URL. Used to navigate to detail screen
 *   and reload content metadata. **Never changes** after bookmarking.
 *   Example: `"anime-provider://attack-on-titan"`
 *
 * ## Content Metadata:
 * - [title]: Content title at bookmark time. May become stale if content
 *   is renamed by the provider. Consider refreshing on load.
 *   Example: `"Attack on Titan"`, `"Inception"`
 * - [posterUrl]: Poster/thumbnail at bookmark time. May become stale.
 *   Lazy-load in bookmark list. Null if unavailable.
 * - [type]: Content type ([ContentType.Anime], [ContentType.Movie], etc.).
 *   Used for filtering and routing in bookmark list.
 *
 * ## Categories (Folders):
 * [category] organizes bookmarks into custom folders:
 * - Default: [BookmarkCategory.DEFAULT] ("My List", "All")
 * - Custom: User-created categories (e.g., `"Watching"`, `"Finished"`, `"Movie Favorites"`)
 *
 * ```kotlin
 * // Default category bookmark
 * val bookmark1 = Bookmark(
 *     id = "bm_1",
 *     providerId = "anime-provider",
 *     url = "anime://aot",
 *     title = "Attack on Titan",
 *     type = ContentType.Anime,
 *     category = BookmarkCategory.DEFAULT,
 *     createdAt = 1716945600000L
 * )
 *
 * // Custom category bookmark
 * val bookmark2 = bookmark1.copy(category = "Watching")
 * ```
 *
 * ## Timestamps:
 * [createdAt] is the Unix epoch timestamp in **milliseconds** when the bookmark
 * was created. Used for sorting (newest first by default).
 * ```kotlin
 * val bookmarkedDate = Instant.ofEpochMilli(bookmark.createdAt)
 * ```
 *
 * ## Usage:
 * ```kotlin
 * // Bookmark content
 * bookmarkManager.addBookmark(
 *     url = result.url,
 *     providerId = result.providerId,
 *     title = result.name,
 *     posterUrl = result.posterUrl,
 *     type = result.type
 * )
 *
 * // Retrieve bookmarks
 * val allBookmarks = bookmarkManager.getBookmarks()
 * val watchingBookmarks = allBookmarks.filter { it.category == "Watching" }
 * val animeBookmarks = allBookmarks.filter { it.type == ContentType.Anime }
 *
 * // Navigate to content
 * navigator.navigateToDetail(bookmark.url)
 * ```
 *
 * ## Bookmark Flow:
 * 1. User taps "Bookmark" / heart icon on search/detail screen
 * 2. Create [Bookmark] with current metadata
 * 3. Save to local database (Room/DataStore)
 * 4. Display in **Bookmarks** screen
 * 5. User can:
 *    - Remove bookmark → `bookmarkManager.removeBookmark(bookmark.id)`
 *    - Move to category → `bookmarkManager.moveToCategory(bookmark.id, "Finished")`
 *    - Rename category → Update all bookmarks with old category name
 *
 * ## Sorting & Filtering:
 * ```kotlin
 * // Sort by newest first
 * val sorted = bookmarks.sortedByDescending { it.createdAt }
 *
 * // Filter by type
 * val movies = bookmarks.filter { it.type == ContentType.Movie }
 *
 * // Filter by category
 * val inWatching = bookmarks.filter { it.category == "Watching" }
 *
 * // Group by category
 * val grouped = bookmarks.groupBy { it.category }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the **Bookmarks/Library** screen
 * - [title] shown as main text
 * - [posterUrl] displayed as thumbnail (lazy-loaded, may be stale)
 * - [type] shown as icon or badge (e.g., "Anime", "Movie")
 * - [category] shown as folder/chip (e.g., "Watching")
 * - [createdAt] shown as "Bookmarked: May 29, 2024" in details
 * - Long-press → context menu (remove, move category, refresh)
 *
 * ## Stale Metadata Handling:
 * Metadata ([title], [posterUrl]) is captured at bookmark time and may become
 * stale. Refresh on load:
 * ```kotlin
 * suspend fun refreshBookmarkMetadata(bookmark: Bookmark): Bookmark {
 *     val detail = contentProvider.loadDetail(bookmark.url)
 *     return bookmark.copy(
 *         title = detail.name,
 *         posterUrl = detail.posterUrl
 *     )
 * }
 * ```
 *
 * ## Related:
 * - Manager: ***`BookmarkManager`***
 * - Category constants: [BookmarkCategory]
 * - Content type: [ContentType]
 * - Detail model: ***`com.dreamstream.core.model.detail.ContentDetail`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class Bookmark(
    /**
     * Unique bookmark identifier.
     *
     * ***Required, auto-generated***. Unique ID for this bookmark entry (not
     * the content ID). Used for bookmark-specific operations (delete, move, rename).
     * Example: `"bm_1716945600000_abc123"`
     */
    val id: String,

    /**
     * Source plugin/provider ID.
     *
     * ***Required***. Identifies which plugin provided this content. Used to
     * load content details and route to correct provider.
     * Example: `"anime-provider"`, `"movie-plugin"`, `"live-tv"`
     */
    val providerId: String,

    /**
     * Provider-side content URL.
     *
     * ***Required***. Lookup key for loading content details. **Never changes**
     * after bookmarking. Used to navigate to detail screen and refresh metadata.
     * Example: `"anime-provider://attack-on-titan"`
     */
    val url: String,

    /**
     * Content title.
     *
     * ***Required***. Display name captured at bookmark time. May become stale
     * if content is renamed by the provider. Consider refreshing on load.
     * Example: `"Attack on Titan"`, `"Inception"`
     */
    val title: String,

    /**
     * Poster/thumbnail image URL.
     *
     * ***Optional***. Null if unavailable. Captured at bookmark time. May become
     * stale. Lazy-loaded in bookmark list. Example: `"https://cdn.example.com/posters/aot.jpg"`
     */
    val posterUrl: String? = null,

    /**
     * Content type.
     *
     * ***Required***. Type of content (Anime, Movie, Series, Live). Used for
     * filtering, routing, and UI differentiation in bookmark list.
     */
    val type: ContentType,

    /**
     * Category/folder name for organization.
     *
     * ***Optional***. Defaults to [BookmarkCategory.DEFAULT] ("My List", "All").
     * User-created categories (e.g., `"Watching"`, `"Finished"`, `"Favorites"`)
     * allow grouping bookmarks. Empty string also means default category.
     */
    val category: String = BookmarkCategory.DEFAULT,

    /**
     * Bookmark creation timestamp (Unix epoch, milliseconds).
     *
     * ***Required***. When this bookmark was created. Used for sorting
     * (newest first by default). Example: `1716945600000L` = May 29, 2024
     */
    val createdAt: Long,
)
