package com.dreamstream.plugin.movieboxonline.provider

import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.core.domain.model.catalog.CatalogResponse
import com.dreamstream.core.domain.model.catalog.CatalogSection
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.domain.model.media.Subtitle
import com.dreamstream.core.domain.model.search.MovieResult
import com.dreamstream.core.domain.model.search.SearchResult
import com.dreamstream.core.domain.model.search.SeriesResult
import com.dreamstream.plugin.api.model.detail.ApiContentDetail
import com.dreamstream.plugin.api.model.search.ApiMovieResult
import com.dreamstream.plugin.api.model.search.ApiSearchResult
import com.dreamstream.plugin.api.model.search.ApiSeriesResult
import com.dreamstream.plugin.api.plugin.LogLevel
import com.dreamstream.plugin.api.provider.ContentProvider
import com.dreamstream.plugin.movieboxonline.MovieBoxConfig
import com.dreamstream.plugin.movieboxonline.provider.parser.HomePageParser
import com.dreamstream.plugin.movieboxonline.provider.parser.MovieBoxItem
import com.dreamstream.plugin.movieboxonline.provider.parser.SearchPageParser
import io.ktor.client.statement.bodyAsText

/**
 * [ContentProvider] implementation for MovieBox Online (https://movieboxonline.org).
 *
 * Supports:
 * - **Home page sections** (`supportsHomePage = true`): scrapes `GET /` and parses the
 *   server-rendered HTML sections plus the embedded Nuxt JSON payload for poster URLs.
 * - **Keyword search** (`supportsSearch = true`): scrapes
 *   `GET /searchpage?keyword={query}`.
 * - **Detail loading** (`load`): stubbed — returns `null` until the player module
 *   is implemented.
 * - **Stream resolution** (`loadLinks`): stubbed — returns `false` until extractors
 *   are wired up.
 *
 * ## Important differences from FlixHQ
 * - The home page and search page use **different card class names** (`a.movie-card`
 *   vs `a.card`) and different title/meta selectors.
 * - Poster images are never present in the SSR HTML `src` attribute; they are
 *   extracted from the Nuxt SSR JSON payload by [HomePageParser] / [SearchPageParser].
 * - Content type (movie vs. series) is inferred from a season-range suffix in the
 *   title (e.g., `"Avatar S1-S2"` → series; `"Inception"` → movie), since the detail
 *   URL carries no `/movie/` or `/tv/` prefix.
 */
class MovieBoxProvider : ContentProvider() {

    override val name: String = MovieBoxConfig.PROVIDER_ID
    override val mainUrl: String = MovieBoxConfig.MAIN_URL
    override val lang: String = "en"
    override val supportedTypes: Set<ContentType> = setOf(ContentType.Movie, ContentType.TvSeries)
    override val supportsHomePage: Boolean = true
    override val supportsSearch: Boolean = true

    // ── Home page ─────────────────────────────────────────────────────────────

    override suspend fun getHomePage(page: Int, request: CatalogRequest): CatalogResponse? {
        return try {
            val html = get("$mainUrl${MovieBoxConfig.HOME_PATH}").bodyAsText()
            val sections = HomePageParser.parse(html, mainUrl)
            CatalogResponse(
                sections = sections.map { section ->
                    CatalogSection(
                        name = section.name,
                        items = section.items.map { it.toSearchResult() },
                    )
                },
            )
        } catch (e: Exception) {
            log("getHomePage failed: ${e.message}", LogLevel.ERROR)
            null
        }
    }

    // ── Search ────────────────────────────────────────────────────────────────

    override suspend fun search(query: String): List<ApiSearchResult> {
        if (query.isBlank()) return emptyList()
        return try {
            val encodedQuery = query.trim()
            val url = "$mainUrl${MovieBoxConfig.SEARCH_PATH_TEMPLATE.replace("{query}", encodedQuery)}"
            val html = get(url).bodyAsText()
            SearchPageParser.parse(html, mainUrl).map { it.toApiSearchResult() }
        } catch (e: Exception) {
            log("search failed: ${e.message}", LogLevel.ERROR)
            emptyList()
        }
    }

    // ── Detail (stubbed) ──────────────────────────────────────────────────────

    /**
     * Full detail loading is not yet implemented — the player/extractor module has
     * not been built. Returns `null` so callers fall back gracefully.
     */
    override suspend fun load(url: String): ApiContentDetail? = null

    // ── Stream resolution (stubbed) ───────────────────────────────────────────

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (Subtitle) -> Unit,
        linkCallback: (StreamLink) -> Unit,
    ): Boolean = false

    // ── Mapping helpers ───────────────────────────────────────────────────────

    /**
     * Converts a [MovieBoxItem] to an [ApiSearchResult] (used by [search]).
     * The registry's ApiMapper will later convert this to a domain [SearchResult].
     */
    private fun MovieBoxItem.toApiSearchResult(): ApiSearchResult =
        if (isMovie) {
            ApiMovieResult(
                name = title,
                url = url,
                posterUrl = posterUrl,
                year = year,
            )
        } else {
            ApiSeriesResult(
                name = title,
                url = url,
                posterUrl = posterUrl,
                year = year,
            )
        }

    /**
     * Converts a [MovieBoxItem] directly to a domain [SearchResult] (used by
     * [getHomePage] which populates [CatalogSection.items] with domain types).
     */
    private fun MovieBoxItem.toSearchResult(): SearchResult =
        if (isMovie) {
            MovieResult(
                name = title,
                url = url,
                posterUrl = posterUrl,
                providerId = name,
                year = year,
            )
        } else {
            SeriesResult(
                name = title,
                url = url,
                posterUrl = posterUrl,
                providerId = name,
                year = year,
            )
        }
}
