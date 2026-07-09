package com.dreamstream.plugin.movieboxonline.provider.parser

import com.fleeksoft.ksoup.Ksoup
import com.fleeksoft.ksoup.nodes.Element
import com.dreamstream.plugin.movieboxonline.MovieBoxConfig

/**
 * Parses the MovieBox Online search results page (`/searchpage?keyword=...`) into
 * a flat list of [MovieBoxItem]s.
 *
 * ## HTML structure (observed on movieboxonline.org/searchpage?keyword=avatar)
 * ```
 * div.results-grid               ← CSS-grid wrapper (no section grouping)
 *   a.card[href]                 ← one result card  (NOTE: class is "card", not "movie-card")
 *     h2.card-title              ← title text
 *     div.card-meta              ← "2024 • Action" (year + primary genre)
 *     span.rate                  ← IMDB-style rating, e.g. "7.9"
 * ```
 *
 * The search page uses `a.card` (not `a.movie-card`), and a `h2.card-title` (not
 * `p.movie-card-title`). All other conventions — poster extraction from the Nuxt
 * payload, meta parsing, and the `isMovie` heuristic — are identical to the home page.
 *
 * ## Poster images
 * Same as the home page: no `src` in HTML, extracted via [NuxtPayloadParser].
 */
object SearchPageParser {

    /**
     * Parses the MovieBox Online search results page HTML.
     *
     * @param html    Raw HTML of the `/searchpage?keyword=...` page.
     * @param baseUrl Base URL prepended to relative card hrefs (default: [MovieBoxConfig.MAIN_URL]).
     * @return Ordered list of parsed items; empty when no results or structure has changed.
     */
    fun parse(html: String, baseUrl: String = MovieBoxConfig.MAIN_URL): List<MovieBoxItem> {
        val doc = Ksoup.parse(html)
        val posterUrls = NuxtPayloadParser.extractPosterUrls(html)

        return doc.select(MovieBoxConfig.SELECTOR_SEARCH_CARD).mapIndexed { index, card ->
            parseSearchCard(card, baseUrl, posterUrls.getOrNull(index))
        }.filterNotNull()
    }

    /**
     * Parses a single search result card element (`a.card`) into a [MovieBoxItem].
     *
     * Returns `null` when the card is missing a title or a valid href.
     */
    internal fun parseSearchCard(
        element: Element,
        baseUrl: String,
        posterUrl: String?,
    ): MovieBoxItem? {
        // Title — h2.card-title text content
        val title = element.selectFirst(MovieBoxConfig.SELECTOR_SEARCH_CARD_TITLE)
            ?.text()?.trim()?.takeIf { it.isNotEmpty() }
            ?: return null

        // Detail page URL — absolute href on the card anchor itself
        val href = element.attr("href").trim().takeIf { it.isNotEmpty() }
            ?: return null
        val url = if (href.startsWith("http")) href else "$baseUrl$href"

        // Year and genre — "2024 • Action" format
        val (year, genre) = HomePageParser.parseMeta(
            element.selectFirst(MovieBoxConfig.SELECTOR_SEARCH_CARD_META)?.text() ?: ""
        )

        // Rating — optional float (e.g. "7.9")
        val rating = element.selectFirst(MovieBoxConfig.SELECTOR_SEARCH_CARD_RATING)
            ?.text()?.trim()?.toFloatOrNull()

        return MovieBoxItem(
            title = title,
            url = url,
            posterUrl = posterUrl,
            year = year,
            genre = genre,
            isMovie = HomePageParser.isMovie(title),
            rating = rating,
        )
    }
}
