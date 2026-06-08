package com.dreamstream.core.testing.fakes

import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.core.domain.model.catalog.CatalogResponse
import com.dreamstream.core.domain.model.catalog.CatalogSection
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.domain.model.media.Subtitle
import com.dreamstream.plugin.api.mapper.ApiMapper.toCoreModel
import com.dreamstream.plugin.api.model.detail.ApiContentDetail
import com.dreamstream.plugin.api.model.search.ApiMovieResult
import com.dreamstream.plugin.api.model.search.ApiSearchResult
import com.dreamstream.plugin.api.provider.ContentProvider

class FakeContentProvider(
    override val name: String = "FakeProvider",
    override val mainUrl: String = "https://fake.example.com",
    override val lang: String = "en",
    override val supportedTypes: Set<ContentType> = setOf(ContentType.Movie),
    override val supportsHomePage: Boolean = true,
    override val supportsSearch: Boolean = true,
) : ContentProvider() {

    var searchResults: List<ApiMovieResult> = listOf(
        ApiMovieResult(
            name = "Fake Movie",
            url = "https://fake.example.com/movie/1",
        )
    )

    var loadResult: ApiContentDetail? = null
    var linksToEmit: List<StreamLink> = emptyList()
    var subtitlesToEmit: List<Subtitle> = emptyList()
    var shouldFailLoad: Boolean = false

    override suspend fun getHomePage(
        page: Int,
        request: CatalogRequest,
    ): CatalogResponse = CatalogResponse(
        sections = listOf(
            CatalogSection(
                name = "Trending",
                items = searchResults.map { it.toCoreModel(providerId = name) },
            )
        )
    )

    override suspend fun search(query: String): List<ApiSearchResult> = searchResults

    override suspend fun load(url: String): ApiContentDetail? {
        if (shouldFailLoad) throw Exception("Fake load failure")
        return loadResult
    }

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (Subtitle) -> Unit,
        linkCallback: (StreamLink) -> Unit,
    ): Boolean {
        subtitlesToEmit.forEach(subtitleCallback)
        linksToEmit.forEach(linkCallback)
        return linksToEmit.isNotEmpty()
    }
}
