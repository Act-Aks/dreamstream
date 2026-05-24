package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Search-level result for a live channel or stream.
 *
 * Live content does not carry a [year] or [rating] at the search level.
 * Metadata is typically minimal and channel-specific — expect most optional
 * fields to be null for live sources.
 *
 * @property lang BCP 47 language tag for the channel's primary language
 *   (e.g. `"en"`, `"ja"`), or null when not declared by the provider.
 * @property tags Provider-assigned category or genre tags for the channel
 *   (e.g. `["News", "Sports"]`).
 */
@Serializable
data class LiveResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.Live,
    override val providerId: String,
    val lang: String? = null,
    val tags: List<String> = emptyList(),
) : SearchResult()
