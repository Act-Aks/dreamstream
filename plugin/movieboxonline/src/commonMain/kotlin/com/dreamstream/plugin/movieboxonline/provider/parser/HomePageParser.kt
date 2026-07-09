package com.dreamstream.plugin.movieboxonline.provider.parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.dreamstream.plugin.movieboxonline.MovieBoxConfig

/**
 * Parses the MovieBox Online home page HTML into a list of [MovieBoxSection]s.
 *
 * ## HTML structure (observed on movieboxonline.org)
 * ```
 * article.section-panel          ← one section/row
 *   div.title                    ← section heading ("Trending Now", "Bollywood", …)
 *   div.movie-card-list
 *     a.movie-card[href]         ← one content card
 *       p.movie-card-title       ← title text
 *       p.movie-card-meta        ← "2024 • Action" (year + primary genre)
 * ```
 *
 * ## Poster images
 * Poster `<img>` elements in the SSR HTML have **no `src` attribute** — images are
 * injected by Vue after hydration. [NuxtPayloadParser] extracts the CDN poster URLs
 * from the embedded JSON payload, and this parser pairs them with cards by a running
 * global index across all sections.
 *
 * ## Content-type detection
 * MovieBox Online does not embed a `/movie/` or `/tv/` prefix in the detail URL.
 * Instead, TV series titles carry a season-range suffix such as `" S1-S2"`. Cards
 * whose title matches [MovieBoxConfig.SERIES_TITLE_REGEX] are treated as series;
 * all others are treated as movies.
 */
object HomePageParser {

    /**
     * Parses the MovieBox Online home page HTML and returns all content sections.
     *
     * Sections with no parseable cards are omitted from the result.
     *
     * @param html    Raw HTML of the home page.
     * @param baseUrl Base URL prepended to relative card hrefs (default: [MovieBoxConfig.MAIN_URL]).
     * @return Ordered list of home sections; empty if the page structure has changed.
     */
    fun parse(html: String, baseUrl: String = MovieBoxConfig.MAIN_URL): List<MovieBoxSection> {
        val doc = Ksoup.parse(html)
        val posterUrls = NuxtPayloadParser.extractPosterUrls(html)
        var posterIndex = 0

        return doc.select(MovieBoxConfig.SELECTOR_SECTION).mapNotNull { section ->
            val title = section.selectFirst(MovieBoxConfig.SELECTOR_SECTION_TITLE)
                ?.text()?.trim()?.takeIf { it.isNotEmpty() }
                ?: return@mapNotNull null

            val items = section.select(MovieBoxConfig.SELECTOR_HOME_CARD).mapNotNull { card ->
                val posterUrl = posterUrls.getOrNull(posterIndex)
                posterIndex++
                parseHomeCard(card, baseUrl, posterUrl)
            }

            if (items.isEmpty()) null else MovieBoxSection(name = title, items = items)
        }
    }

    /**
     * Parses a single home-page card element (`a.movie-card`) into a [MovieBoxItem].
     *
     * Returns `null` when the card is missing a title or a valid href.
     */
    internal fun parseHomeCard(
        element: Element,
        baseUrl: String,
        posterUrl: String?,
    ): MovieBoxItem? {
        // Title — p.movie-card-title text content
        val title = element.selectFirst(MovieBoxConfig.SELECTOR_HOME_CARD_TITLE)
            ?.text()?.trim()?.takeIf { it.isNotEmpty() }
            ?: return null

        // Detail page URL — absolute href on the card anchor itself
        val href = element.attr("href").trim().takeIf { it.isNotEmpty() }
            ?: return null
        val url = if (href.startsWith("http")) href else "$baseUrl$href"

        // Year and genre — "2024 • Action" format
        val (year, genre) = parseMeta(
            element.selectFirst(MovieBoxConfig.SELECTOR_HOME_CARD_META)?.text() ?: ""
        )

        return MovieBoxItem(
            title = title,
            url = url,
            posterUrl = posterUrl,
            year = year,
            genre = genre,
            isMovie = isMovie(title),
            rating = null, // not present on home-page cards
        )
    }

    // ── Shared helpers ────────────────────────────────────────────────────────

    /**
     * Splits a metadata string of the form `"YYYY • Genre"` into a (year, genre) pair.
     *
     * Both values are optional — a non-numeric year field returns `null` for year,
     * and an absent separator returns `null` for genre.
     */
    internal fun parseMeta(meta: String): Pair<Int?, String?> {
        val parts = meta.split("•").map { it.trim() }
        val year = parts.getOrNull(0)?.toIntOrNull()
        val genre = parts.getOrNull(1)?.takeIf { it.isNotEmpty() }
        return year to genre
    }

    /**
     * Returns `true` when [title] does NOT contain a TV-series season marker.
     *
     * MovieBox Online appends ` S1`, ` S1-S2`, etc. to series titles.
     * Absence of this pattern is the strongest HTML-level signal that the item is a movie.
     */
    internal fun isMovie(title: String): Boolean =
        !MovieBoxConfig.SERIES_TITLE_REGEX.containsMatchIn(title)
}
