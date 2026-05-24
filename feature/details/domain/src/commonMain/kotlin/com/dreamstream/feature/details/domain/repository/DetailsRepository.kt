package com.dreamstream.feature.details.domain.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.model.detail.ContentDetail
import com.dreamstream.feature.details.domain.error.DetailsError

/**
 * Contract for fetching the full detail record for a single piece of content.
 *
 * [contentId] is the provider-side URL from the [com.dreamstream.core.model.search.SearchResult]
 * that was tapped on the home screen — i.e. [com.dreamstream.core.model.search.SearchResult.url].
 */
interface DetailsRepository {
    suspend fun getContentDetail(contentId: String): Result<ContentDetail, DetailsError>
}
