package com.dreamstream.core.domain.model.user

import com.dreamstream.core.domain.model.user.BookmarkCategory.COMPLETED
import com.dreamstream.core.domain.model.user.BookmarkCategory.DEFAULT
import com.dreamstream.core.domain.model.user.BookmarkCategory.DROPPED
import com.dreamstream.core.domain.model.user.BookmarkCategory.ON_HOLD
import com.dreamstream.core.domain.model.user.BookmarkCategory.WATCHING
import com.dreamstream.core.domain.model.user.BookmarkCategory.all


/**
 * Predefined categories for organizing [Bookmark] entries.
 *
 * [BookmarkCategory] provides a set of standard folders for the user's library,
 * similar to anime/manga tracking lists. These categories are used to group
 * bookmarks in the **Bookmarks/Library** screen and can be used for filtering
 * and statistics.
 *
 * ## Built-in Categories:
 * | Constant | Value | Purpose |
 * |----------|-------|---------|
 * | [DEFAULT] | `"Watchlist"` | Default category for all bookmarks |
 * | [WATCHING] | `"Watching"` | Currently watching/viewing |
 * | [COMPLETED] | `"Completed"` | Finished content |
 * | [ON_HOLD] | `"On Hold"` | Paused temporarily |
 * | [DROPPED] | `"Dropped"` | Abandoned content |
 *
 * ## Usage:
 * ```kotlin
 * // Add to default watchlist
 * bookmarkManager.addBookmark(..., category = BookmarkCategory.DEFAULT)
 *
 * // Mark as watching
 * bookmarkManager.moveToCategory(bookmarkId, BookmarkCategory.WATCHING)
 *
 * // Mark as completed
 * bookmarkManager.moveToCategory(bookmarkId, BookmarkCategory.COMPLETED)
 * ```
 *
 * ## Filtering Bookmarks:
 * ```kotlin
 * val all = bookmarkManager.getBookmarks()
 * val watching = all.filter { it.category == BookmarkCategory.WATCHING }
 * val completed = all.filter { it.category == BookmarkCategory.COMPLETED }
 * val notCompleted = all.filter { it.category != BookmarkCategory.COMPLETED }
 * ```
 *
 * ## UI Representation:
 * - Displayed as **tabs** or **sidebar folders** in the Bookmarks screen
 * - [DEFAULT] often labeled "Watchlist" or "All"
 * - Each category shows a count (e.g., "Watching (12)")
 * - User can create **custom categories** beyond these predefined ones
 *
 * ## Custom Categories:
 * While these are the standard categories, users can create custom ones:
 * ```kotlin
 * // Custom category (not in predefined list)
 * val favorites = bookmark.copy(category = "Favorites")
 * val rewatching = bookmark.copy(category = "Rewatching")
 * ```
 * The [all] list contains only the predefined constants, not custom categories.
 *
 * ## Related:
 * - Bookmark model: [Bookmark]
 * - Manager: ***`BookmarkManager`***
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
object BookmarkCategory {
    /**
     * Default category for all bookmarks.
     *
     * Value: `"Watchlist"`.
     * Used when no specific category is chosen. Often displayed as "All" or
     * "Watchlist" in the UI. Contains all bookmarks unless moved to another
     * category.
     */
    const val DEFAULT = "Watchlist"

    /**
     * Currently watching/viewing category.
     *
     * Value: `"Watching"`.
     * For content the user is actively consuming. Displayed with a "playing"
     * or "in-progress" indicator in the UI.
     */
    const val WATCHING = "Watching"

    /**
     * Completed content category.
     *
     * Value: `"Completed"`.
     * For finished anime, movies, or series. Often shown with a checkmark
     * or "done" badge.
     */
    const val COMPLETED = "Completed"

    /**
     * Temporarily paused category.
     *
     * Value: `"On Hold"`.
     * For content the user wants to resume later but is not currently watching.
     */
    const val ON_HOLD = "On Hold"

    /**
     * Abandoned content category.
     *
     * Value: `"Dropped"`.
     * For content the user stopped watching and does not plan to resume.
     * Often hidden by default or placed in a secondary section.
     */
    const val DROPPED = "Dropped"

    /**
     * List of all predefined category names.
     *
     * ***Computed***. Returns `[DEFAULT, WATCHING, COMPLETED, ON_HOLD, DROPPED]`.
     * Used to populate category dropdowns, filter options, or statistics.
     * Does **not** include user-created custom categories.
     *
     * Example:
     * ```kotlin
     * BookmarkCategory.all // ["Watchlist", "Watching", "Completed", "On Hold", "Dropped"]
     * ```
     */
    val all = listOf(DEFAULT, WATCHING, COMPLETED, ON_HOLD, DROPPED)
}
