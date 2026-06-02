package com.dreamstream.plugin.flixhq.provider.parser

import com.fleeksoft.ksoup.Ksoup
import com.dreamstream.plugin.flixhq.FlixHqConfig

/**
 * Parses FlixHQ search result pages into [FlixHqItem] lists.
 *
 * Search results use the same `div.flw-item` card structure as the home page, so
 * this parser delegates item extraction to [HomePageParser.parseItem].
 *
 * Expected URL: `https://flixhq.to/search/{url-encoded-query}`
 */
object SearchParser {

    /**
     * Parses the FlixHQ search results page HTML.
     *
     * @param html Raw HTML of the search results page.
     * @param baseUrl Base URL prepended to relative item hrefs.
     * @return List of parsed items; empty when no results or structure has changed.
     */
    fun parse(html: String, baseUrl: String = FlixHqConfig.MAIN_URL): List<FlixHqItem> {
        val doc = Ksoup.parse(html)
        return doc.select(FlixHqConfig.SELECTOR_ITEM).mapNotNull { el ->
            HomePageParser.parseItem(el, baseUrl)
        }
    }
}
