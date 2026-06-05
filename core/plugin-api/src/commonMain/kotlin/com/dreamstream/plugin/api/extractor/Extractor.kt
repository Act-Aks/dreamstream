package com.dreamstream.plugin.api.extractor

import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.domain.model.media.Subtitle
import io.ktor.client.HttpClient

/**
 * Base class for video host extractors.
 *
 * An extractor takes a URL from a video hosting site
 * (e.g. streamtape.com, doodstream.com) and extracts
 * the actual direct video URL.
 *
 * Example:
 * ```kotlin
 * class MyExtractor : Extractor() {
 *     override val name = "MyHost"
 *     override val mainUrl = "https://myhost.com"
 *
 *     override suspend fun extract(
 *         url: String,
 *         referer: String?,
 *         subtitleCallback: (ApiSubtitle) -> Unit,
 *         linkCallback: (ApiStreamLink) -> Unit
 *     ) {
 *         // Scrape video URL from the page
 *         val videoUrl = ...
 *         linkCallback(ApiStreamLink(url = videoUrl, quality = ApiQuality.HD))
 *     }
 * }
 * ```
 */
abstract class Extractor {
    abstract val name: String
    abstract val mainUrl: String

    open val requiresReferer: Boolean = false

    /** Injected by host */
    lateinit var client: HttpClient
        internal set

    /**
     * Returns true if this extractor can handle [url].
     * Default: checks if URL contains [mainUrl]
     */
    open fun canHandle(url: String): Boolean = url.contains(mainUrl, ignoreCase = true)

    /**
     * Extract direct video links from [url].
     *
     * @param url The video host URL to extract from
     * @param referer Optional referer header value
     * @param subtitleCallback Call for each subtitle found
     * @param linkCallback Call for each direct video link found
     */
    abstract suspend fun extract(
        url: String,
        referer: String? = null,
        subtitleCallback: (Subtitle) -> Unit,
        linkCallback: (StreamLink) -> Unit,
    )

    protected fun getTrimmedMainUrl(): String = mainUrl.trimEnd('/')
}
