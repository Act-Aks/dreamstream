package com.dreamstream.core.data.repository

import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.core.domain.model.catalog.CatalogResponse
import com.dreamstream.core.domain.model.catalog.CatalogSection
import com.dreamstream.core.domain.model.detail.ContentDetail
import com.dreamstream.core.domain.model.search.SearchResult
import com.dreamstream.core.domain.repository.ContentRepository
import com.dreamstream.core.domain.repository.StreamResult
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.plugin.loader.PluginManager
import com.dreamstream.plugin.api.mapper.ApiMapper.toCoreModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope

class ContentRepositoryImpl(
    private val pluginManager: PluginManager,
    loggerFactory: LoggerFactory
) : ContentRepository {

    private val logger = loggerFactory.get("ContentRepositoryImpl")

    override suspend fun search(
        query: String,
        providerId: String?,
    ): Result<List<SearchResult>, DreamError> = supervisorScope {
        val providers = if (providerId != null) {
            listOfNotNull(pluginManager.getProviderById(providerId))
        } else {
            pluginManager.getAllProviders().filter { it.supportsSearch }
        }

        if (providers.isEmpty()) {
            return@supervisorScope Result.Error(DreamError.NoPluginsInstalled)
        }

        val results = providers.map { provider ->
            async {
                runCatching {
                    provider.search(query).map { apiResponse ->
                        apiResponse.toCoreModel(provider.name)
                    }
                }.onFailure { throwable ->
                    logger.error(throwable) { "Search failed for provider: ${provider.name}" }
                }.getOrDefault(emptyList())
            }
        }.awaitAll().flatten()

        Result.Success(results)
    }

    override fun getHomePage(
        providerId: String,
        page: Int,
        request: CatalogRequest,
    ): Flow<Result<CatalogResponse, DreamError>> = flow {
        emit(Result.Success(CatalogResponse())) // or a Result.Loading-style state in UI

        val provider = pluginManager.getProviderById(providerId)
        if (provider == null) {
            emit(Result.Error(DreamError.PluginNotFound))
            return@flow
        }

        if (!provider.supportsHomePage) {
            emit(Result.Success(CatalogResponse()))
            return@flow
        }

        runCatching {
            val apiRequest = CatalogRequest(
                page = request.page,
                sectionName = request.sectionName,
            )
            val apiResponse = provider.getHomePage(page, apiRequest)

            val response = apiResponse?.let { res ->
                CatalogResponse(
                    sections = res.sections.map { section ->
                        CatalogSection(
                            name = section.name,
                            items = section.items,
                            hasNextPage = section.hasNextPage,
                        )
                    },
                    hasNextPage = res.hasNextPage,
                )
            } ?: CatalogResponse()

            emit(Result.Success(response))
        }.onFailure { throwable ->
            logger.error(throwable) { "GetHomePage failed: $providerId" }
            emit(
                Result.Error(
                    DreamError.Plugin(
                        pluginId = providerId,
                        message = throwable.message ?: "Unknown",
                    ),
                ),
            )
        }
    }

    override suspend fun loadContent(
        url: String,
        providerId: String,
    ): Result<ContentDetail, DreamError> {
        val provider = pluginManager.getProviderById(providerId)
            ?: return Result.Error(DreamError.PluginNotFound)

        return runCatching {
            val apiResponse = provider.load(url) ?: return Result.Error(
                DreamError.Plugin(
                    pluginId = providerId,
                    message = "Provider returned null for $url",
                ),
            )

            Result.Success(apiResponse.toCoreModel(providerId))
        }.getOrElse { throwable ->
            logger.error(throwable) { "LoadContent failed: $url" }
            Result.Error(
                DreamError.Plugin(
                    pluginId = providerId,
                    message = throwable.message ?: "Unknown",
                ),
            )
        }
    }

    override fun getStreamLinks(
        data: String,
        providerId: String,
        isCasting: Boolean,
    ): Flow<StreamResult> = flow {
        val provider = pluginManager.getProviderById(providerId)
        if (provider == null) {
            emit(StreamResult.ErrorResult(DreamError.PluginNotFound))
            return@flow
        }

        val linkChannel = Channel<StreamResult>(Channel.UNLIMITED)

        runCatching {
            val found = provider.loadLinks(
                data = data,
                isCasting = isCasting,
                subtitleCallback = { subtitle ->
                    linkChannel.trySend(StreamResult.SubtitleFound(subtitle))
                },
                linkCallback = { link ->
                    val extractor = pluginManager.getExtractorForUrl(link.url)
                    if (extractor != null) {
                        // Extract via extractor
                        runBlocking {
                            runCatching {
                                extractor.extract(
                                    url = link.url,
                                    referer = link.referer,
                                    subtitleCallback = { sub ->
                                        linkChannel.trySend(
                                            StreamResult.SubtitleFound(sub),
                                        )
                                    },
                                    linkCallback = { extractedLink ->
                                        linkChannel.trySend(
                                            StreamResult.Link(
                                                extractedLink.copy(
                                                    extractorName = extractor.name
                                                )
                                            ),
                                        )
                                    },
                                )
                            }.onFailure { throwable ->
                                logger.error(throwable) { "Extractor failed: ${extractor.name}" }
                                // Fallback: emit the raw link
                                linkChannel.trySend(StreamResult.Link(link))
                            }
                        }
                    } else {
                        linkChannel.trySend(StreamResult.Link(link))
                    }
                },
            )

            if (!found) {
                emit(StreamResult.ErrorResult(DreamError.NoSources(data)))
            }
        }.onFailure { throwable ->
            logger.error(throwable) { "GetStreamLinks failed: $data" }
            emit(
                StreamResult.ErrorResult(
                    DreamError.Plugin(
                        pluginId = providerId,
                        message = throwable.message ?: "Unknown",
                    ),
                ),
            )
        }

        linkChannel.close()
        for (result in linkChannel) {
            emit(result)
        }

        emit(StreamResult.Complete)
    }
}
