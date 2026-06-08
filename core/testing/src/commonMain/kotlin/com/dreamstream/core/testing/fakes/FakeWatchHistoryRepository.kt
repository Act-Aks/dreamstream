package com.dreamstream.core.testing.fakes

import com.dreamstream.core.domain.model.user.WatchHistory
import com.dreamstream.core.domain.repository.WatchHistoryRepository
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeWatchHistoryRepository : WatchHistoryRepository {

    private val _entries = MutableStateFlow<List<WatchHistory>>(emptyList())

    override fun observeAll(): Flow<List<WatchHistory>> = _entries

    override fun observeRecent(limit: Int): Flow<List<WatchHistory>> =
        _entries.map { it.take(limit) }

    override suspend fun getByUrl(
        url: String,
        providerId: String,
    ): WatchHistory? = _entries.value.find {
        it.url == url && it.providerId == providerId
    }

    override suspend fun insert(entry: WatchHistory): EmptyResult<DreamError> = tryThis {
        _entries.update { current ->
            val filtered = current.filter { it.id != entry.id }
            listOf(entry) + filtered
        }
    }

    override suspend fun updateProgress(
        id: String,
        positionMs: Long,
        durationMs: Long,
    ): EmptyResult<DreamError> = try {
        _entries.update { current ->
            current.map { entry ->
                if (entry.id == id) {
                    entry.copy(
                        watchPositionMs = positionMs,
                        totalDurationMs = durationMs,
                    )
                } else entry
            }
        }
        Result.Success(Unit)
    } catch (e: Throwable) {
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun deleteById(id: String): EmptyResult<DreamError> = tryThis {
        _entries.update { current -> current.filter { it.id != id } }
    }

    override suspend fun clearAll(): EmptyResult<DreamError> = tryThis {
        _entries.update { emptyList() }
    }

    // Test helpers
    fun seedEntries(vararg entries: WatchHistory) {
        _entries.update { entries.toList() }
    }

    private fun tryThis(command: () -> Unit): EmptyResult<DreamError> = try {
        command()
        Result.Success(Unit)
    } catch (e: Throwable) {
        Result.Error(DreamError.Unknown(e))
    }
}
