package com.dreamstream.plugin.movieboxonline

/**
 * Central configuration for the MovieBox Online plugin.
 *
 * All URLs, CSS selectors, and regex patterns are consolidated here so they
 * can be updated in one place when the site structure changes.
 *
 * Note: MovieBox Online is a Nuxt.js SSR app. The home page and search page
 * use *different* card class names, so two separate selector groups are defined.
 * Poster images are never present in SSR HTML — they must be extracted from the
 * Nuxt JSON payload embedded in the page (see [NuxtPayloadParser]).
 */
object MovieBoxConfig {

    /** Base URL for all requests. */
    const val MAIN_URL = "https://movieboxonline.org"

    /** Provider ID used in [com.dreamstream.core.domain.model.search.SearchResult.providerId]. */
    const val PROVIDER_ID = "movieboxonline"

    // ── Home page ─────────────────────────────────────────────────────────────

    /** Home page path appended to [MAIN_URL]. */
    const val HOME_PATH = "/"

    /** CSS selector for a content section/row on the home page. */
    const val SELECTOR_SECTION = "article.section-panel"

    /** CSS selector for a section's title element (relative to [SELECTOR_SECTION]). */
    const val SELECTOR_SECTION_TITLE = "div.title"

    /** CSS selector for a single content card on the home page. */
    const val SELECTOR_HOME_CARD = "a.movie-card"

    /** CSS selector for a card's title text (relative to [SELECTOR_HOME_CARD]). */
    const val SELECTOR_HOME_CARD_TITLE = "p.movie-card-title"

    /**
     * CSS selector for a card's metadata line (relative to [SELECTOR_HOME_CARD]).
     * Format: `"YYYY • Genre"` — split on ` • `.
     */
    const val SELECTOR_HOME_CARD_META = "p.movie-card-meta"

    // ── Search page ───────────────────────────────────────────────────────────

    /**
     * URL template for keyword search. Replace `{query}` with the URL-encoded query.
     * The site uses a query-param based route (`/searchpage?keyword=...`) rather
     * than a path-based route like FlixHQ.
     */
    const val SEARCH_PATH_TEMPLATE = "/searchpage?keyword={query}"

    /**
     * CSS selector for a single result card on the search page.
     * Note: search uses `a.card`, NOT `a.movie-card` — different from the home page.
     */
    const val SELECTOR_SEARCH_CARD = "a.card"

    /** CSS selector for a search card's title (relative to [SELECTOR_SEARCH_CARD]). */
    const val SELECTOR_SEARCH_CARD_TITLE = "h2.card-title"

    /**
     * CSS selector for a search card's metadata line (relative to [SELECTOR_SEARCH_CARD]).
     * Format: `"YYYY • Genre"` — same structure as the home page meta.
     */
    const val SELECTOR_SEARCH_CARD_META = "div.card-meta"

    /** CSS selector for the IMDB-style rating badge on a search card. */
    const val SELECTOR_SEARCH_CARD_RATING = "span.rate"

    // ── Nuxt SSR payload ──────────────────────────────────────────────────────

    /**
     * CSS selector for the Nuxt SSR data payload script tag.
     * Poster images are not present in the SSR HTML; they are embedded as full
     * CDN URL strings inside this JSON payload array.
     */
    const val SELECTOR_NUXT_PAYLOAD = "script[data-nuxt-data=nuxt-app]"

    /**
     * Regex pattern to extract CDN poster image URLs from the raw Nuxt payload JSON.
     *
     * The CDN follows the pattern:
     * `https://pbcdnw.aoneroom.com/image/YYYY/MM/DD/<hash>.<ext>`
     *
     * URLs appear in the flat reference array in the same order as the cards
     * rendered in the HTML, making positional pairing reliable.
     */
    val CDN_POSTER_REGEX = Regex(
        """https://pbcdnw\.aoneroom\.com/image/\d{4}/\d{2}/\d{2}/[\w]+\.\w+"""
    )

    // ── Content-type heuristic ────────────────────────────────────────────────

    /**
     * Regex that detects a TV series season-range suffix in a title.
     * MovieBox Online appends ` S1`, ` S1-S2`, ` S1-S3`, etc. to series titles.
     *
     * Examples that match (series): `"Avatar: The Last Airbender S1-S2"`, `"One Piece S1"`
     * Examples that do NOT match (movies): `"Avatar: The Way of Water"`, `"Inception"`
     */
    val SERIES_TITLE_REGEX = Regex("""\s+S\d""")
}
