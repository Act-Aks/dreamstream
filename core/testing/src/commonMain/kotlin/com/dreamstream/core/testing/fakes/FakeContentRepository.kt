package com.dreamstream.core.testing.fakes

import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.core.domain.model.catalog.CatalogResponse
import com.dreamstream.core.domain.model.catalog.CatalogSection
import com.dreamstream.core.domain.model.detail.ContentDetail
import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.domain.model.media.Subtitle
import com.dreamstream.core.domain.model.search.SearchResult
import com.dreamstream.core.domain.repository.ContentRepository
import com.dreamstream.core.domain.repository.StreamResult
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.testing.Fixtures
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeContentRepository : ContentRepository {

    var searchResult: Result<List<SearchResult>, DreamError> =
        Result.Success(listOf(Fixtures.movieResult()))

    var homePageResult: Result<CatalogResponse, DreamError> = Result.Success(
        CatalogResponse(
            sections = listOf(
                CatalogSection(
                    name = "Trending",
                    items = listOf(Fixtures.movieResult()),
                )
            )
        )
    )

    var loadContentResult: Result<ContentDetail, DreamError> =
        Result.Success(Fixtures.movieDetail())

    var streamLinks: List<StreamLink> = listOf(Fixtures.streamLink())
    var subtitles: List<Subtitle> = listOf(Fixtures.subtitle())
    var shouldFailStreaming: Boolean = false

    var searchCallCount = 0
    var loadCallCount = 0

    override suspend fun search(
        query: String,
        providerId: String?,
    ): Result<List<SearchResult>, DreamError> {
        searchCallCount++
        return searchResult
    }

    override fun getHomePage(
        providerId: String,
        page: Int,
        request: CatalogRequest,
    ): Flow<Result<CatalogResponse, DreamError>> = flow {
        emit(homePageResult)
    }

    override suspend fun loadContent(
        url: String,
        providerId: String,
    ): Result<ContentDetail, DreamError> {
        loadCallCount++
        return loadContentResult
    }

    override fun getStreamLinks(
        data: String,
        providerId: String,
        isCasting: Boolean,
    ): Flow<StreamResult> = flow {
        if (shouldFailStreaming) {
            emit(StreamResult.ErrorResult(DreamError.NoSources(data)))
            return@flow
        }
        subtitles.forEach { emit(StreamResult.SubtitleFound(it)) }
        streamLinks.forEach { emit(StreamResult.Link(it)) }
        emit(StreamResult.Complete)
    }
}
