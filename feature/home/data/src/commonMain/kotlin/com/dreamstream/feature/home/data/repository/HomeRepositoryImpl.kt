package com.dreamstream.feature.home.data.repository

import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.core.domain.repository.ContentRepository
import com.dreamstream.core.domain.util.Result
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.HomeSection
import com.dreamstream.feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.last

/**
 * Production [HomeRepository] backed by [ContentRepository].
 *
 * Calls [ContentRepository.getHomePage] for the given [providerId], collects
 * the final result from the flow, and maps [CatalogSection]s to [HomeSection]s.
 *
 * @param contentRepository Domain repository that fans out to plugin providers.
 * @param providerId        Provider to load the home page from (e.g. `"movieboxonline"`).
 *                          Set via Koin at app assembly time so this class has no
 *                          compile-time dependency on any specific plugin module.
 */
class HomeRepositoryImpl(
    private val contentRepository: ContentRepository,
    private val providerId: String,
) : HomeRepository {

    override suspend fun getHomeSections(page: Int?): Result<List<HomeSection>, HomeError> {
        return try {
            // getHomePage emits an initial empty result (loading signal) then
            // the real result before completing; last() returns the final value.
            when (
                val catalogResult = contentRepository
                    .getHomePage(
                        providerId = providerId,
                        page = page ?: 1,
                        request = CatalogRequest()
                    )
                    .last()
            ) {
                is Result.Success -> {
                    val sections = catalogResult.data.sections.mapIndexed { index, section ->
                        HomeSection(
                            id = "${section.name}_$index",
                            title = section.name,
                            items = section.items,
                        )
                    }
                    if (sections.isEmpty()) {
                        Result.Error(HomeError.NoContentAvailable)
                    } else {
                        Result.Success(sections)
                    }
                }

                is Result.Error -> Result.Error(HomeError.LoadFailed)
            }
        } catch (e: Exception) {
            Result.Error(HomeError.LoadFailed)
        }
    }
}
