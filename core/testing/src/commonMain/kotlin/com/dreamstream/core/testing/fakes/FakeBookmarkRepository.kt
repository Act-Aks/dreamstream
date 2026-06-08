package com.dreamstream.core.testing.fakes

import com.dreamstream.core.domain.model.user.Bookmark
import com.dreamstream.core.domain.repository.BookmarkRepository
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeBookmarkRepository : BookmarkRepository {

    private val _bookmarks = MutableStateFlow<List<Bookmark>>(emptyList())

    override fun observeAll(): Flow<List<Bookmark>> = _bookmarks

    override fun observeByCategory(category: String): Flow<List<Bookmark>> =
        _bookmarks.map { it.filter { b -> b.category == category } }

    override suspend fun isBookmarked(
        url: String,
        providerId: String,
    ): Boolean = _bookmarks.value.any {
        it.url == url && it.providerId == providerId
    }

    override suspend fun getByUrl(
        url: String, providerId: String
    ): Bookmark? = _bookmarks.value.find {
        it.url == url && it.providerId == providerId
    }

    override suspend fun addBookmark(bookmark: Bookmark): EmptyResult<DreamError> = tryThis {
        _bookmarks.update { current ->
            if (current.any { it.url == bookmark.url && it.providerId == bookmark.providerId }) {
                current
            } else {
                current + bookmark
            }
        }
    }

    override suspend fun removeBookmark(id: String): EmptyResult<DreamError> = tryThis {
        _bookmarks.update { current -> current.filter { it.id != id } }
    }

    override suspend fun updateCategory(id: String, category: String): EmptyResult<DreamError> =
        tryThis {
            _bookmarks.update { current ->
                current.map { if (it.id == id) it.copy(category = category) else it }
            }
        }

    fun seedBookmarks(vararg bookmarks: Bookmark) {
        _bookmarks.update { bookmarks.toList() }
    }

    private fun tryThis(command: () -> Unit): EmptyResult<DreamError> = try {
        command()
        com.dreamstream.core.domain.util.Result.Success(Unit)
    } catch (e: Throwable) {
        Result.Error(DreamError.Unknown(e))
    }
}

