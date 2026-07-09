package com.dreamstream.plugin.movieboxonline.provider.parser

/**
 * Internal DTO representing a single parsed content item from MovieBox Online.
 *
 * Instances are created by [HomePageParser] and [SearchPageParser] and converted
 * to plugin-API types ([com.dreamstream.plugin.api.model.search.ApiSearchResult])
 * or domain types ([com.dreamstream.core.domain.model.search.SearchResult]) by
 * [com.dreamstream.plugin.movieboxonline.provider.MovieBoxProvider].
 *
 * @property title     Display title as shown on the site (may include season suffix for series).
 * @property url       Absolute detail page URL (`https://movieboxonline.org/detailpage/{slug}`).
 * @property posterUrl CDN poster image URL, or `null` if the Nuxt payload did not yield one.
 * @property year      Release year parsed from the metadata line, or `null` if absent/non-numeric.
 * @property genre     Primary genre parsed from the metadata line, or `null` if absent.
 * @property isMovie   `true` when the title has no TV series season suffix (` S\d` pattern).
 * @property rating    IMDB-style rating from search result cards, or `null` on the home page.
 */
data class MovieBoxItem(
    val title: String,
    val url: String,
    val posterUrl: String?,
    val year: Int?,
    val genre: String?,
    val isMovie: Boolean,
    val rating: Float?,
)

/**
 * Internal DTO representing a named content section on the MovieBox Online home page.
 *
 * @property name  Section heading (e.g., "Trending Now", "Bollywood").
 * @property items Ordered list of items within the section.
 */
data class MovieBoxSection(
    val name: String,
    val items: List<MovieBoxItem>,
)
