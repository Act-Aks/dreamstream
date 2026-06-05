package com.dreamstream.core.data.repository

import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.model.user.Bookmark
import com.dreamstream.core.domain.repository.BookmarkRepository
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import com.dreamstream.core.database.repository.BookmarkRepository as DbBookmarkRepository

class BookmarkRepositoryImpl(
    private val repository: DbBookmarkRepository,
    loggerFactory: LoggerFactory
) : BookmarkRepository {

    private val logger = loggerFactory.get("BookmarkRepositoryImpl")

    override fun observeAll(): Flow<List<Bookmark>> = repository.observeAll()

    override fun observeByCategory(category: String): Flow<List<Bookmark>> =
        repository.observeByCategory(category)

    override suspend fun getByUrl(
        url: String,
        providerId: String,
    ): Bookmark? = repository.getByUrl(url, providerId)

    override suspend fun isBookmarked(
        url: String,
        providerId: String,
    ): Boolean = runCatching {
        repository.isBookmarked(url, providerId)
    }.getOrDefault(false)

    override suspend fun addBookmark(bookmark: Bookmark): EmptyResult<DreamError> = try {
        repository.insert(bookmark)
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to add bookmark" }
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun removeBookmark(id: String): EmptyResult<DreamError> = try {
        repository.deleteById(id)
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to remove bookmark" }
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun updateCategory(id: String, category: String): EmptyResult<DreamError> =
        try {
            repository.updateCategory(id, category)
            Result.Success(Unit)
        } catch (e: Throwable) {
            logger.error(e) { "Failed to update bookmark category" }
            Result.Error(DreamError.Unknown(e))
        }
}
