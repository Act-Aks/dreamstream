package com.dreamstream.core.model.search

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Search-level result for an anime title.
 *
 * [subAvailable] and [dubAvailable] are provider-level availability flags
 * reported at search time. They may not be accurate for all sources — always
 * verify availability when resolving streams before showing track indicators
 * to the user.
 *
 * Full sub/dub episode lists are available on the corresponding
 * [com.dreamstream.core.model.detail.AnimeDetail].
 *
 * @property year Year the anime first aired, or null when unknown.
 * @property totalEpisodes Total episode count reported by the provider at
 *   search time, or null when not available.
 * @property dubAvailable True if a dubbed audio track is available for this title.
 * @property subAvailable True if a subtitled track is available for this title.
 * @property rating Raw provider rating on a 0–10 scale, or null when unavailable.
 */
@Serializable
data class AnimeResult(
    override val name: String,
    override val url: String,
    override val posterUrl: String? = null,
    override val type: ContentType = ContentType.Anime,
    override val providerId: String,
    val year: Int? = null,
    val totalEpisodes: Int? = null,
    val dubAvailable: Boolean = false,
    val subAvailable: Boolean = true,
    val rating: Float? = null,
) : SearchResult()
