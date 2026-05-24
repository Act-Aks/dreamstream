package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Base type for all search results returned by a provider plugin.
 *
 * [SearchResult] is a lightweight list-level model — it carries just enough
 * information to render a content card in search results, home sections, and
 * recommendation rows. For full metadata, load the corresponding [com.dreamstream.core.model.detail.ContentDetail].
 *
 * Use [url] as the provider-side lookup key when navigating to the detail
 * screen. Use [id] as the stable in-app identifier for diffing and caching.
 *
 * Provider-specific DTOs should be mapped to subtypes of [SearchResult] in
 * the data layer before reaching the domain or presentation layers.
 *
 * @see MovieResult
 * @see SeriesResult
 * @see AnimeResult
 * @see LiveResult
 */
@Serializable
sealed class SearchResult {
    abstract val name: String
    abstract val url: String
    abstract val posterUrl: String?
    abstract val type: ContentType
    abstract val providerId: String

    /**
     * Stable composite identifier: `"${providerId}_${url.hashCode()}"`.
     *
     * Suitable as a lazy-list key or cache key within a single provider's
     * result set. Never use this as the navigation key — use [url] instead.
     */
    val id: String get() = "${providerId}_${url.hashCode()}"
}
