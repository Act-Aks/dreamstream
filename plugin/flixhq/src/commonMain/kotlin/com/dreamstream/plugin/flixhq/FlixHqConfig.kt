package com.dreamstream.plugin.flixhq

import com.dreamstream.plugin.flixhq.FlixHqConfig.MAIN_URL
import com.dreamstream.plugin.flixhq.FlixHqConfig.SELECTOR_BLOCK


/**
 * Central configuration for the FlixHQ plugin.
 *
 * All URLs and selectors are consolidated here so they can be updated in one
 * place when the site structure changes.
 */
object FlixHqConfig {
    /** Base URL. Override for regional mirrors or self-hosted instances. */
    const val MAIN_URL = "https://flixhq.to"

    /** Provider ID used to populate [com.dreamstream.core.model.search.SearchResult.providerId]. */
    const val PROVIDER_ID = "flixhq"

    // ── Home page ─────────────────────────────────────────────────────────────

    /** Home page path appended to [MAIN_URL]. */
    const val HOME_PATH = "/home"

    /** CSS selector for a content block/section on the home page. */
    const val SELECTOR_BLOCK = "div.block_area"

    /** CSS selector for a block's title element (relative to [SELECTOR_BLOCK]). */
    const val SELECTOR_BLOCK_TITLE = "h2.cat-heading"

    // ── Item cards (shared between home and search) ───────────────────────────

    /** CSS selector for a single content card (relative to its parent container). */
    const val SELECTOR_ITEM = "div.flw-item"

    /** Attr holding the poster image URL (checked first; falls back to `src`). */
    const val ATTR_POSTER_SRC = "data-src"

    /** CSS selector for the anchor element containing the title and URL. */
    const val SELECTOR_TITLE_ANCHOR = "h3.film-name a, div.film-name a"

    /** CSS selector for the first metadata span (year, episode count, etc.). */
    const val SELECTOR_META_YEAR = "div.fd-infor span.fdi-item"

    // ── Search ────────────────────────────────────────────────────────────────

    /** Path template for search. Replace `{query}` with URL-encoded query string. */
    const val SEARCH_PATH_TEMPLATE = "/search/{query}"
}
