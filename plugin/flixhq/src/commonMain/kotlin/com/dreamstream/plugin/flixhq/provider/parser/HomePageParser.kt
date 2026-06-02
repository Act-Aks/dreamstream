package com.dreamstream.plugin.flixhq.provider.parser

import com.fleeksoft.ksoup.Ksoup
import com.dreamstream.plugin.flixhq.FlixHqConfig

/**
 * Parses a single FlixHQ content card (`div.flw-item`) into a [FlixHqItem].
 *
 * All selectors are documented inline so they are easy to update when
 * FlixHQ changes its HTML structure.
 *
 * Selector notes (as observed on flixhq.to):
 * - Poster:  `div.film-poster img` → `data-src` attribute (lazy-loaded)
 * - Title:   `h3.film-name a` or `div.film-name a` → text + `href`
 * - Year:    first `span.fdi-item` inside `div.fd-infor`
 * - Type:    inferred from the href path (`/movie/` → movie, otherwise series)
 */
data class FlixHqItem(
    val title: String,
    val url: String,
    val posterUrl: String?,
    val year: Int?,
    val isMovie: Boolean,
)

data class FlixHqSection(
    val name: String,
    val items: List<FlixHqItem>,
)

object HomePageParser {

    /**
     * Parses the FlixHQ home page HTML and returns all content sections found.
     *
     * Each `div.block_area` element on the page becomes a [FlixHqSection].
     * Sections with no parseable items are excluded from the result.
     *
     * @param html Raw HTML of the FlixHQ home page.
     * @param baseUrl Base URL prepended to relative item hrefs (default: [FlixHqConfig.MAIN_URL]).
     * @return Ordered list of home sections; empty if the page structure has changed.
     */
    fun parse(html: String, baseUrl: String = FlixHqConfig.MAIN_URL): List<FlixHqSection> {
        val doc = Ksoup.parse(html)
        return doc.select(FlixHqConfig.SELECTOR_BLOCK).mapNotNull { block ->
            val titleEl = block.selectFirst(FlixHqConfig.SELECTOR_BLOCK_TITLE)
            val title = titleEl?.text()?.trim()?.takeIf { it.isNotEmpty() } ?: return@mapNotNull null
            val items = block.select(FlixHqConfig.SELECTOR_ITEM).mapNotNull { el ->
                parseItem(el, baseUrl)
            }
            if (items.isEmpty()) null else FlixHqSection(name = title, items = items)
        }
    }

    internal fun parseItem(element: com.fleeksoft.ksoup.nodes.Element, baseUrl: String): FlixHqItem? {
        // Poster: prefer data-src (lazy loading), fall back to src
        val posterImg = element.selectFirst("div.film-poster img") ?: return null
        val posterUrl = posterImg.attr(FlixHqConfig.ATTR_POSTER_SRC)
            .takeIf { it.isNotEmpty() }
            ?: posterImg.attr("src").takeIf { it.isNotEmpty() }

        // Title and URL
        val anchor = element.selectFirst(FlixHqConfig.SELECTOR_TITLE_ANCHOR) ?: return null
        val title = anchor.text().trim().takeIf { it.isNotEmpty() } ?: return null
        val href = anchor.attr("href").trim().takeIf { it.isNotEmpty() } ?: return null
        val url = if (href.startsWith("http")) href else "$baseUrl$href"

        // Year: first fdi-item span; try to parse as an integer
        val year = element.selectFirst(FlixHqConfig.SELECTOR_META_YEAR)
            ?.text()?.trim()?.toIntOrNull()

        // Content type inferred from URL path segment
        val isMovie = href.contains("/movie/", ignoreCase = true)

        return FlixHqItem(
            title = title,
            url = url,
            posterUrl = posterUrl,
            year = year,
            isMovie = isMovie,
        )
    }
}
