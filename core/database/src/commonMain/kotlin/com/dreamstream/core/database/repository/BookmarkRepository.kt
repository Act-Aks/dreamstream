package com.dreamstream.core.database.repository

import com.dreamstream.core.database.dao.BookmarkDao
import com.dreamstream.core.database.entity.BookmarkEntity
import com.dreamstream.core.model.user.Bookmark
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepository(private val dao: BookmarkDao) {
    fun observeAll(): Flow<List<Bookmark>> =
        dao.observeAll().map { entities ->
            entities.map { it.toDomain() }
        }

    fun observeByCategory(category: String): Flow<List<Bookmark>> =
        dao.observeByCategory(category).map { entities ->
            entities.map { it.toDomain() }
        }

    fun observeWatchlist(): Flow<List<Bookmark>> = observeByCategory("Watchlist")

    suspend fun isBookmarked(url: String, providerId: String) = dao.isBookmarked(url, providerId)

    suspend fun getByUrl(url: String, providerId: String) =
        dao.getByUrl(url, providerId)?.toDomain()

    suspend fun insert(bookmark: Bookmark) = dao.insert(bookmark.toEntity())

    suspend fun insertOrUpdate(bookmark: Bookmark) {
        if (isBookmarked(bookmark.url, bookmark.providerId)) {
            getByUrl(bookmark.url, bookmark.providerId)?.let {
                dao.updateCategory(it.id, bookmark.category)
            } ?: dao.insert(bookmark.toEntity())
        } else {
            dao.insert(bookmark.toEntity())
        }
    }

    suspend fun updateCategory(id: String, category: String) = dao.updateCategory(id, category)

    suspend fun deleteById(id: String) = dao.deleteById(id)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun countByCategory() = dao.countByCategory().associate { it.category to it.count }

    private fun BookmarkEntity.toDomain() = Bookmark(
        id = id,
        providerId = providerId,
        url = url,
        title = title,
        posterUrl = posterUrl,
        type = contentType,
        category = category,
        createdAt = createdAt
    )

    private fun Bookmark.toEntity() = BookmarkEntity(
        id = id,
        providerId = providerId,
        url = url,
        title = title,
        posterUrl = posterUrl,
        contentType = type,
        category = category,
        createdAt = createdAt
    )
}
