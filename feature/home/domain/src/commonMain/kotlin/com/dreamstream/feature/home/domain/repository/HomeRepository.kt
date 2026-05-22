package com.dreamstream.feature.home.domain.repository

import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.HomeSection

/**
 * Contract for fetching the home screen's content feed.
 *
 * Defined in domain so the presentation layer can depend on this interface
 * without knowing about the data layer implementation.
 */
interface HomeRepository {
    suspend fun getHomeSections(): Result<List<HomeSection>, HomeError>
}
