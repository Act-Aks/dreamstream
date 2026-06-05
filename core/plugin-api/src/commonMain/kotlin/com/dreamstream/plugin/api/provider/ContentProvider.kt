package com.dreamstream.plugin.api.provider

import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.core.domain.model.catalog.CatalogResponse
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.SubtitleFormat
import com.dreamstream.core.domain.model.filter.FilterOption
import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.domain.model.media.Subtitle
import com.dreamstream.plugin.api.model.detail.ApiContentDetail
import com.dreamstream.plugin.api.model.search.ApiSearchResult
import com.dreamstream.plugin.api.plugin.LogLevel
import com.dreamstream.plugin.api.plugin.PluginContext
import com.dreamstream.plugin.api.provider.ContentProvider.Companion.BROWSER_HEADERS
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse

/**
 * Base class for all content providers in DreamStream.
 *
 * A content provider is responsible for:
 * - Providing a home page with categorized content [supportsHomePage]
 * - Searching for content by query [supportsSearch]
 * - Loading full content details (movies, TV series, etc.) [load]
 * - Resolving streaming links for episodes/movies [loadLinks]
 *
 * ## Usage in DreamStream
 * Create a subclass in your provider module:
 * ```kotlin
 * class NetflixProvider : ContentProvider() {
 *     override val name = "netflix"
 *     override val mainUrl = "https://netflix.com"
 *     override val lang = "en"
 *     override val supportedTypes = setOf(ContentType.Movie, ContentType.TvSeries)
 *
 *     override suspend fun load(url: String): ApiContentDetail? {
 *         // Implementation
 *     }
 *
 *     override suspend fun loadLinks(...): Boolean {
 *         // Implementation
 *     }
 * }
 * ```
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
abstract class ContentProvider {

    // ---- Mandatory properties ----

    /**
     * Unique internal identifier for this provider (e.g., "netflix", "adm").
     *
     * Used for:
     * - Plugin registration
     * - User preferences storage
     * - Logging and analytics
     */
    abstract val name: String

    /**
     * Main URL of the source website.
     *
     * Displayed in provider settings and used as a reference for relative URL resolution.
     */
    abstract val mainUrl: String

    /**
     * ISO 639-1 language code (e.g., "en", "es", "fr", "hi").
     *
     * Determines the default language for content fetched from this provider.
     */
    abstract val lang: String

    /**
     * Content types this provider supports.
     *
     * Example: [setOf][ContentType.Movie, ContentType.TvSeries]
     * for a general streaming service, or [setOf][ContentType.Anime] for anime-only sources.
     *
     * Uses [ContentType] from `:core:model:catalog` (shared across DreamStream).
     */
    abstract val supportedTypes: Set<ContentType>

    // ---- Optional capabilities ----

    /**
     * Whether this provider offers a categorized home page with sections.
     *
     * When `true`, [getHomePage] will be called to populate the home screen
     * with sections like "Trending", "New Releases", "Genres", etc.
     *
     * Default: `false`
     */
    open val supportsHomePage: Boolean = false

    /**
     * Whether this provider supports search functionality.
     *
     * When `true`, [search] and [searchWithFilter] will be available to users.
     *
     * Default: `true`
     */
    open val supportsSearch: Boolean = true

    /**
     * Whether this provider supports Chromecast/driving mode streaming.
     *
     * When `true`, the player will enable cast-specific link selection
     * and may prioritize direct links over extracted ones.
     *
     * Default: `false`
     */
    open val supportsChromecast: Boolean = false

    /**
     * VPN requirement status for accessing this provider.
     *
     * - [VpnStatus.None]: No VPN needed
     * - [VpnStatus.MightBeNeeded]: VPN may help in some regions
     * - [VpnStatus.Needed]: VPN is required for this provider
     *
     * Default: [VpnStatus.None]
     */
    open val vpnStatus: VpnStatus = VpnStatus.None

    /**
     * The type of provider based on how it returns streaming data.
     *
     * - [ProviderType.DirectLink]: Returns direct video URLs
     * - [ProviderType.MetaData]: Returns links to video hosts requiring extraction
     *
     * Default: [ProviderType.MetaData]
     */
    open val providerType: ProviderType = ProviderType.MetaData

    /**
     * Language code used for search queries.
     *
     * Override this if the provider's search interface uses a different
     * language than the content itself (e.g., search in English, content in Hindi).
     *
     * Default: same as [lang]
     */
    open val searchLanguage: String get() = lang

    /**
     * Supported subtitle formats for this provider.
     *
     * Common values: [SubtitleFormat.SRT], [SubtitleFormat.VTT], [SubtitleFormat.ASS]
     *
     * Default: [SRT, VTT][SubtitleFormat.SRT]
     *
     * Uses [SubtitleFormat] from `:core:model:catalog` (shared across DreamStream).
     */
    open val supportedSubtitleTypes: Set<SubtitleFormat> =
        setOf(SubtitleFormat.SRT, SubtitleFormat.VTT)

    // ---- Injected by host (DreamStream plugin host) ----

    /**
     * HTTP client provided by the DreamStream host app.
     *
     * **Do NOT create your own HttpClient** — reuse this instance for:
     * - Connection pooling
     * - Shared interceptors (logging, auth, user-agent)
     * - Proper resource management
     *
     * Initialized by the plugin host via [inject] before any provider methods are called.
     */
    lateinit var client: HttpClient
        private set

    /**
     * Plugin context for storage, logging, and shared resources.
     *
     * Provides:
     * - Structured logging via [log]
     * - Shared disk storage for caching
     * - Plugin lifecycle management
     *
     * Initialized by the plugin host via [inject] before any provider methods are called.
     */
    lateinit var pluginContext: PluginContext
        private set

    /**
     * Called by the plugin host (`:core:plugin-runtime`) after provider instantiation
     * and before any provider method is invoked.
     *
     * **Not intended for plugin authors** — the host calls this automatically.
     */
    fun inject(client: HttpClient, context: PluginContext) {
        this.client = client
        this.pluginContext = context
    }

    // ---- API Methods ----

    /**
     * Returns categorized content for the home page.
     *
     * Called when [supportsHomePage] is `true`. Should return sections like:
     * - "Trending Now"
     * - "New Releases"
     * - "Action Movies"
     * - "Continue Watching"
     *
     * @param page Page number (starting at 1) for pagination
     * @param request Contains section name for loading more items in that section
     * @return [CatalogResponse] with sections and content, or `null` on failure
     */
    open suspend fun getHomePage(
        page: Int,
        request: CatalogRequest,
    ): CatalogResponse? = null

    /**
     * Search for content matching the user's query.
     *
     * @param query User's search string (e.g., "inception", "stranger things")
     * @return List of matching content (recommended max: ~20 results)
     *
     * ## Implementation notes:
     * - Respect [searchLanguage] for query encoding
     * - Return empty list instead of `null` on no results
     * - Apply rate limiting to avoid blocking
     */
    open suspend fun search(query: String): List<ApiSearchResult> = emptyList()

    /**
     * Search with additional filters (genre, year, rating, etc.).
     *
     * Override this if your provider supports advanced filtering.
     *
     * @param query User's search string
     * @param filters List of selected filters from [getFilterList]
     * @return Filtered list of matching content
     *
     * Default implementation: falls back to [search]
     */
    open suspend fun searchWithFilter(
        query: String,
        filters: List<FilterOption>,
    ): List<ApiSearchResult> = search(query)

    /**
     * Get available search filters for this provider.
     *
     * Return a list of filter definitions (genre, year, rating, sort order, etc.)
     * to display in the search UI.
     *
     * @return List of [FilterOption] definitions, or empty list if no filters
     *
     * ## Example:
     * ```kotlin
     * override fun getFilterList(): List<ApiFilter> = listOf(
     *     ApiFilter.Select("genre", "Genre", listOf("Action", "Comedy", "Drama")),
     *     ApiFilter.Range("year", "Year", 1990 to 2026),
     *     ApiFilter.Select("sort", "Sort by", listOf("Relevance", "Rating", "Date"))
     * )
     * ```
     */
    open fun getFilterList(): List<FilterOption> = emptyList()

    /**
     * Load full details for content at the given URL.
     *
     * Called when user taps on a search result or home page item.
     *
     * @param url URL or ID returned from [search] or [getHomePage]
     * @return [ApiContentDetail] with full content details (title, episodes, metadata),
     *         or `null` on failure
     *
     * ## Must include in response:
     * - Title, overview, poster, rating
     * - Episode list (for TV series) or movie details
     * - Data string/URL for [loadLinks]
     */
    abstract suspend fun load(url: String): ApiContentDetail?

    /**
     * Resolve streaming links for content.
     *
     * This is the core method that extracts playable video URLs.
     *
     * @param data URL or data string from episode/movie (from [ApiContentDetail])
     * @param isCasting `true` if user is casting to Chromecast/device
     * @param subtitleCallback Called for each subtitle found
     * @param linkCallback Called for each streaming link found
     * @return `true` if at least one stream link was found, `false` otherwise
     *
     * ## Implementation pattern:
     * ```kotlin
     * override suspend fun loadLinks(...): Boolean {
     *     val page = client.get(data).body<Document>()
     *
     *     page.select(".video-link").forEach { element ->
     *         linkCallback(
     *             ApiStreamLink(
     *                 url = element.attr("href"),
     *                 quality = "1080p",
     *                 source = "Server 1"
     *             )
     *         )
     *     }
     *
     *     page.select(".subtitle").forEach { element ->
     *         subtitleCallback(
     *             ApiSubtitle(
     *                 url = element.attr("href"),
     *                 lang = "en",
     *                 format = SubtitleFormat.VTT
     *             )
     *         )
     *     }
     *
     *     return true // found links
     * }
     * ```
     */
    abstract suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (Subtitle) -> Unit,
        linkCallback: (StreamLink) -> Unit,
    ): Boolean

    // ---- HTTP convenience methods ----

    /**
     * Simplified GET request with headers, parameters, and browser-like defaults.
     *
     * [BROWSER_HEADERS] are applied first so any entry in [headers] can override a
     * default (e.g. a provider can supply its own `Accept-Language`).
     *
     * Cloudflare and many other CDN/WAF products perform HTTP-header fingerprinting in
     * addition to TLS fingerprinting. Sending only a `User-Agent` is enough for them to
     * issue a TCP RST ("Connection reset"). The defaults below match what Chrome 120
     * sends for a top-level page navigation and are sufficient to pass basic bot checks.
     *
     * @param url Request URL
     * @param headers Additional HTTP headers (override defaults where keys overlap)
     * @param params Query parameters
     * @return [HttpResponse]
     */
    protected suspend fun get(
        url: String,
        headers: Map<String, String> = emptyMap(),
        params: Map<String, String> = emptyMap(),
    ): HttpResponse = client.get(url) {
        // Browser defaults first — caller headers win on collision.
        BROWSER_HEADERS.forEach { (key, value) -> header(key, value) }
        headers.forEach { (key, value) -> header(key, value) }
        params.forEach { (key, value) -> parameter(key, value) }
    }

    /**
     * Simplified POST request with headers, form data, and browser-like defaults.
     *
     * @param url Request URL
     * @param headers Additional HTTP headers (override defaults where keys overlap)
     * @param data Form parameters (application/x-www-form-urlencoded)
     * @param body JSON or raw body (optional)
     * @return [HttpResponse]
     */
    protected suspend fun post(
        url: String,
        headers: Map<String, String> = emptyMap(),
        data: Map<String, String> = emptyMap(),
        body: Any? = null,
    ): HttpResponse = client.post(url) {
        // Browser defaults first — caller headers win on collision.
        BROWSER_HEADERS.forEach { (key, value) -> header(key, value) }
        headers.forEach { (key, value) -> header(key, value) }
        if (body != null) setBody(body)
        if (data.isNotEmpty() && body == null) {
            // Use formData for form-urlencoded data
            formData { data.forEach { (key, value) -> append(key, value) } }
        }
    }

    /**
     * Log a message with the provider's name.
     *
     * @param message Log message
     * @param level Log level (DEBUG, INFO, WARN, ERROR)
     *
     * ## Example:
     * ```kotlin
     * log("Fetching episode 5 from server")
     * log("Failed to load links", LogLevel.ERROR)
     * ```
     */
    protected fun log(
        message: String,
        level: LogLevel = LogLevel.DEBUG,
    ) {
        pluginContext.log(name, message, level)
    }

    companion object {
        /**
         * User-Agent string mimicking Chrome 120 on Windows 10.
         *
         * Exposed as a constant so provider subclasses can reference it when building
         * custom header maps (e.g. for `Referer` or `Origin` headers).
         */
        const val DEFAULT_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/120.0.0.0 Safari/537.36"

        /**
         * Browser-like HTTP headers sent with every [get] and [post] request.
         *
         * These match what Chrome 120 sends for a top-level page navigation (user
         * typing a URL directly into the address bar). Cloudflare and similar CDN/WAF
         * products fingerprint HTTP headers at multiple levels and issue a TCP RST when a
         * request does not look sufficiently browser-like.
         *
         * ### Why each group is here
         *
         * **`sec-ch-ua` / `sec-ch-ua-Mobile` / `sec-ch-ua-Platform`** — Chrome Client
         * Hints that identify the browser brand and version. Their *absence* is an
         * immediate Cloudflare bot signal for requests that claim Chrome via User-Agent.
         *
         * **`Accept`** — Exact Chrome 120 value for a top-level document navigation.
         * The [com.dreamstream.core.data.network.HttpClientFactory] intentionally omits
         * Ktor's ContentNegotiation plugin to prevent it from appending `application/json`
         * here and corrupting this value.
         *
         * **`Sec-Fetch-Dest` / `Sec-Fetch-Mode` / `Sec-Fetch-Site` / `Sec-Fetch-User`**
         * — Fetch metadata headers. Chrome sends these for every navigation. Cloudflare
         * checks them as part of its H1 header fingerprint. Values here reflect a direct
         * navigation (`none` site, user-activated). Providers making XHR-like sub-requests
         * should override `Sec-Fetch-Site` with `same-origin` or `cross-site` and drop
         * `Sec-Fetch-User` via their own `headers` map argument.
         *
         * **`Connection`** is intentionally omitted — it is a hop-by-hop header forbidden
         * in HTTP/2 (RFC 7540 §8.1.2.2). Sending it over an HTTP/2 connection causes a
         * PROTOCOL_ERROR stream reset.
         *
         * **`Accept-Encoding`** is limited to `gzip, deflate`. Adding `br` (Brotli)
         * requires the `com.squareup.okhttp3:okhttp-brotli` interceptor; without it,
         * a Brotli-encoded response body would arrive undecodable. OkHttp handles gzip
         * and deflate transparently.
         */
        val BROWSER_HEADERS: Map<String, String> = mapOf(
            "User-Agent" to DEFAULT_USER_AGENT,
            // Chrome Client Hints — absence flags non-browser to Cloudflare.
            "sec-ch-ua" to "\"Not_A Brand\";v=\"8\", \"Chromium\";v=\"120\", \"Google Chrome\";v=\"120\"",
            "sec-ch-ua-Mobile" to "?0",
            "sec-ch-ua-Platform" to "\"Windows\"",
            "Upgrade-Insecure-Requests" to "1",
            "Accept" to "text/html,application/xhtml+xml,application/xml;" + "q=0.9,image/avif,image/webp,image/apng,*/*;" + "q=0.8,application/signed-exchange;v=b3;q=0.7",
            // Sec-Fetch metadata — Chrome sends these for every navigation.
            "Sec-Fetch-Site" to "none",
            "Sec-Fetch-Mode" to "navigate",
            "Sec-Fetch-User" to "?1",
            "Sec-Fetch-Dest" to "document",
            "Accept-Encoding" to "gzip, deflate",
            "Accept-Language" to "en-US,en;q=0.9",
            "Cache-Control" to "max-age=0",
        )
    }
}

