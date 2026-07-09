package com.dreamstream.plugin.movieboxonline.provider.parser

import com.fleeksoft.ksoup.Ksoup
import com.dreamstream.plugin.movieboxonline.MovieBoxConfig

/**
 * Extracts CDN poster image URLs from the Nuxt SSR JSON payload embedded in a
 * MovieBox Online page.
 *
 * ## Background
 * MovieBox Online is a Nuxt.js SSR application. Poster images are **not** present
 * as `src` or `data-src` attributes on `<img>` elements in the server-rendered HTML.
 * Instead, every page embeds a flat JSON reference array inside:
 * ```html
 * <script type="application/json" data-nuxt-data="nuxt-app" id="__NUXT_DATA__">
 *   [ ... ]
 * </script>
 * ```
 * The array contains all page data (subjects, seasons, metadata, …). Full reference
 * resolution is complex; as a pragmatic alternative this parser scans the **raw JSON
 * string** for CDN poster URL substrings matching [MovieBoxConfig.CDN_POSTER_REGEX].
 *
 * ## Ordering guarantee
 * CDN poster URLs appear in the payload in the same order as the content cards
 * rendered in the HTML. Callers can therefore pair the N-th poster URL with the
 * N-th card element by index.
 *
 * ## Deduplication
 * The Nuxt payload may encode the same URL multiple times (e.g., thumbnail vs.
 * full-size variants with the same path). Only the **first** occurrence of each
 * unique URL is retained so the list length matches the card count.
 */
object NuxtPayloadParser {

    /**
     * Extracts an ordered list of CDN poster image URLs from [html].
     *
     * @param html Raw HTML of any MovieBox Online page.
     * @return Deduplicated, ordered list of CDN poster URLs. Empty when the
     *         payload script tag is absent, empty, or contains no CDN URLs.
     */
    fun extractPosterUrls(html: String): List<String> {
        val doc = Ksoup.parse(html)
        val script = doc.selectFirst(MovieBoxConfig.SELECTOR_NUXT_PAYLOAD)
            ?: return emptyList()
        val json = script.data().takeIf { it.isNotBlank() }
            ?: return emptyList()
        return MovieBoxConfig.CDN_POSTER_REGEX
            .findAll(json)
            .map { it.value }
            .distinct()
            .toList()
    }
}
