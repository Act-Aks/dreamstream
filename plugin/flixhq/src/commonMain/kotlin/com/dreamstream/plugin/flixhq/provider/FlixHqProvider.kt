package com.dreamstream.plugin.flixhq.provider

import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.media.StreamLink
import com.dreamstream.core.model.media.Subtitle
import com.dreamstream.core.model.search.MovieResult
import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.model.search.SeriesResult
import com.dreamstream.plugin.api.model.catalog.CatalogRequest
import com.dreamstream.plugin.api.model.catalog.CatalogResponse
import com.dreamstream.plugin.api.model.catalog.CatalogSection
import com.dreamstream.plugin.api.model.detail.ApiContentDetail
import com.dreamstream.plugin.api.model.search.ApiMovieResult
import com.dreamstream.plugin.api.model.search.ApiSearchResult
import com.dreamstream.plugin.api.model.search.ApiSeriesResult
import com.dreamstream.plugin.api.provider.ContentProvider
import com.dreamstream.plugin.flixhq.FlixHqConfig
import com.dreamstream.plugin.flixhq.provider.parser.FlixHqItem
import com.dreamstream.plugin.flixhq.provider.parser.HomePageParser
import com.dreamstream.plugin.flixhq.provider.parser.SearchParser
import io.ktor.client.statement.bodyAsText

/**
 * [ContentProvider] implementation for FlixHQ.
 *
 * Supports:
 * - **Home page sections** (`supportsHomePage = true`): scrapes `GET /home`.
 * - **Keyword search** (`supportsSearch = true`): scrapes `GET /search/{query}`.
 * - **Detail loading** (`load`): stubbed — returns `null` until the player module
 *   is implemented (tracked as a future task).
 * - **Stream resolution** (`loadLinks`): stubbed — returns `false` until extractors
 *   are wired up.
 */
class FlixHqProvider : ContentProvider() {

    override val name: String = FlixHqConfig.PROVIDER_ID
    override val mainUrl: String = FlixHqConfig.MAIN_URL
    override val lang: String = "en"
    override val supportedTypes: Set<ContentType> = setOf(ContentType.Movie, ContentType.TvSeries)
    override val supportsHomePage: Boolean = true
    override val supportsSearch: Boolean = true

    // ── Home page ─────────────────────────────────────────────────────────────

    override suspend fun getMainPage(page: Int, request: CatalogRequest): CatalogResponse? {
        return try {
            val html = get("$mainUrl${FlixHqConfig.HOME_PATH}").bodyAsText()
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
            log("getMainPage failed: ${e.message}", com.dreamstream.plugin.api.plugin.LogLevel.ERROR)
            null
        }
    }

    // ── Search ────────────────────────────────────────────────────────────────

    override suspend fun search(query: String): List<ApiSearchResult> {
        if (query.isBlank()) return emptyList()
        return try {
            val encodedQuery = query.trim()
                .replace(" ", "-")
                .lowercase()
            val url = "$mainUrl${FlixHqConfig.SEARCH_PATH_TEMPLATE.replace("{query}", encodedQuery)}"
            val html = get(url).bodyAsText()
            SearchParser.parse(html, mainUrl).map { it.toApiSearchResult() }
        } catch (e: Exception) {
            log("search failed: ${e.message}", com.dreamstream.plugin.api.plugin.LogLevel.ERROR)
            emptyList()
        }
    }

    // ── Detail (stubbed) ──────────────────────────────────────────────────────

    /**
     * Full detail loading is not yet implemented — the player/extractor module has not
     * been built. Returns `null` so callers fall back gracefully (the details screen
     * keeps showing its stub data from [InMemoryDetailsRepository]).
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
     * Converts a parsed [FlixHqItem] to an [ApiSearchResult] (used by [search]).
     * The registry's [ApiMapper] will later convert this to a domain [SearchResult].
     */
    private fun FlixHqItem.toApiSearchResult(): ApiSearchResult =
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
     * Converts a parsed [FlixHqItem] directly to a domain [SearchResult] (used by [getMainPage],
     * which populates [CatalogSection.items] with domain types directly).
     */
    private fun FlixHqItem.toSearchResult(): SearchResult =
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
