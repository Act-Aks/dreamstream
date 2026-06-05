package com.dreamstream.core.domain.repository

import com.dreamstream.core.domain.model.user.WatchHistory
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import kotlinx.coroutines.flow.Flow

interface WatchHistoryRepository {
    fun observeAll(): Flow<List<WatchHistory>>
    fun observeRecent(limit: Int = 20): Flow<List<WatchHistory>>

    suspend fun getByUrl(url: String, providerId: String): WatchHistory?

    suspend fun insert(entry: WatchHistory): EmptyResult<DreamError>

    suspend fun updateProgress(
        id: String,
        positionMs: Long,
        durationMs: Long,
    ): EmptyResult<DreamError>

    suspend fun deleteById(id: String): EmptyResult<DreamError>

    suspend fun clearAll(): EmptyResult<DreamError>
}
