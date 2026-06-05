package com.dreamstream.core.domain.repository

import com.dreamstream.core.domain.model.user.Bookmark
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    fun observeAll(): Flow<List<Bookmark>>
    fun observeByCategory(category: String): Flow<List<Bookmark>>

    suspend fun isBookmarked(url: String, providerId: String): Boolean

    suspend fun getByUrl(url: String, providerId: String): Bookmark?

    suspend fun addBookmark(bookmark: Bookmark): EmptyResult<DreamError>

    suspend fun removeBookmark(id: String): EmptyResult<DreamError>

    suspend fun updateCategory(id: String, category: String): EmptyResult<DreamError>
}
