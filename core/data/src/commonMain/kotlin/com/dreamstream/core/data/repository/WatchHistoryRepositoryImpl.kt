package com.dreamstream.core.data.repository

import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.model.user.WatchHistory
import com.dreamstream.core.domain.repository.WatchHistoryRepository
import com.dreamstream.core.domain.system.TimeProvider
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import com.dreamstream.core.database.repository.WatchHistoryRepository as DbWatchHistoryRepository

class WatchHistoryRepositoryImpl(
    private val repository: DbWatchHistoryRepository,
    private val timeProvider: TimeProvider,
    loggerFactory: LoggerFactory
) : WatchHistoryRepository {

    private val logger = loggerFactory.get("WatchHistoryRepositoryImpl")

    override fun observeAll(): Flow<List<WatchHistory>> = repository.observeAll()

    override fun observeRecent(limit: Int): Flow<List<WatchHistory>> =
        repository.observeRecent(limit)

    override suspend fun getByUrl(
        url: String,
        providerId: String,
    ): WatchHistory? = repository.getByUrl(url, providerId)

    override suspend fun insert(entry: WatchHistory): EmptyResult<DreamError> = try {
        repository.insert(entry)
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to insert watch history" }
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun updateProgress(
        id: String,
        positionMs: Long,
        durationMs: Long,
    ): EmptyResult<DreamError> = try {
        repository.updateProgress(
            id = id,
            positionMs = positionMs,
            durationMs = durationMs,
            lastWatchedAt = timeProvider.currentTimeMillis(),
        )
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to update watch progress" }
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun deleteById(id: String): EmptyResult<DreamError> = try {
        repository.deleteById(id)
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to delete watch history" }
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun clearAll(): EmptyResult<DreamError> = try {
        repository.deleteAll()
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to clear watch history" }
        Result.Error(DreamError.Unknown(e))
    }
}
