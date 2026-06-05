package com.dreamstream.core.database.repository

import com.dreamstream.core.database.dao.WatchHistoryDao
import com.dreamstream.core.database.entity.WatchHistoryEntity
import com.dreamstream.core.domain.model.user.WatchHistory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class WatchHistoryRepository(private val dao: WatchHistoryDao) {
    fun observeAll(): Flow<List<WatchHistory>> = dao.observeAll().map { entities ->
        entities.map { it.toDomain() }
    }

    fun observeRecent(limit: Int = 20): Flow<List<WatchHistory>> =
        dao.observeRecent(limit).map { entities ->
            entities.map { it.toDomain() }
        }

    suspend fun getById(id: String): WatchHistory? = dao.getById(id)?.toDomain()

    suspend fun getByUrl(url: String, providerId: String): WatchHistory? =
        dao.getByUrl(url, providerId)?.toDomain()

    suspend fun insert(entry: WatchHistory) = dao.insert(entry.toEntity())
    suspend fun update(entry: WatchHistory) = dao.update(entry.toEntity())
    suspend fun updateProgress(
        id: String, positionMs: Long, durationMs: Long, lastWatchedAt: Long
    ) = dao.updateProgress(id, positionMs, durationMs, lastWatchedAt)

    suspend fun deleteById(id: String) = dao.deleteById(id)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun countAll(): Int = dao.countAll()

    private fun WatchHistoryEntity.toDomain() = WatchHistory(
        id = id,
        providerId = providerId,
        url = url,
        title = title,
        posterUrl = posterUrl,
        type = contentType,
        episodeName = episodeName,
        season = season,
        episode = episode,
        episodeData = episodeData,
        watchPositionMs = watchPositionMs,
        totalDurationMs = totalDurationMs,
        lastWatchedAt = lastWatchedAt,
        createdAt = createdAt
    )

    private fun WatchHistory.toEntity() = WatchHistoryEntity(
        id = id,
        providerId = providerId,
        url = url,
        title = title,
        posterUrl = posterUrl,
        contentType = type,
        episodeName = episodeName,
        season = season,
        episode = episode,
        episodeData = episodeData,
        watchPositionMs = watchPositionMs,
        totalDurationMs = totalDurationMs,
        lastWatchedAt = lastWatchedAt,
        createdAt = createdAt
    )
}
