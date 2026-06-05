package com.dreamstream.core.domain.model.catalog

import kotlinx.serialization.Serializable

/**
 * Request to load catalog data.
 *
 * Used for initial load ([page] = 1, no [sectionName])
 * and pagination ([page] > 1 or specific [sectionName]).
 */
@Serializable
data class CatalogRequest(
    /** Page number (1-based) */
    val page: Int = 1,

    /** Section name to load more items for (null for initial load) */
    val sectionName: String? = null,
)
