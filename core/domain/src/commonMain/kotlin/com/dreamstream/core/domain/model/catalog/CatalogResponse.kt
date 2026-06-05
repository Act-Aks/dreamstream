package com.dreamstream.core.domain.model.catalog

import kotlinx.serialization.Serializable

/**
 * Response containing catalog data (e.g., home page sections, search results).
 */
@Serializable
data class CatalogResponse(
    /** List of sections in the catalog (e.g., "Trending", "New Releases") */
    val sections: List<CatalogSection> = emptyList(),

    /** `true` if more pages are available */
    val hasNextPage: Boolean = false,
)
