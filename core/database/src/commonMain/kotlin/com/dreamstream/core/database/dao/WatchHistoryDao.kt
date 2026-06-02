package com.dreamstream.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.dreamstream.core.database.entity.WatchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM WatchHistory ORDER BY last_watched_at DESC")
    fun observeAll(): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM WatchHistory ORDER BY last_watched_at DESC LIMIT :limit")
    fun observeRecent(limit: Int = 20): Flow<List<WatchHistoryEntity>>

    @Query("SELECT * FROM WatchHistory WHERE id = :id")
    suspend fun getById(id: String): WatchHistoryEntity?

    @Query("SELECT * FROM WatchHistory WHERE url = :url AND provider_id = :providerId")
    suspend fun getByUrl(url: String, providerId: String): WatchHistoryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: WatchHistoryEntity)

    @Update
    suspend fun update(entity: WatchHistoryEntity)

    @Query("UPDATE WatchHistory SET watch_position_ms = :positionMs, total_duration_ms = :durationMs, last_watched_at = :lastWatchedAt WHERE id = :id")
    suspend fun updateProgress(id: String, positionMs: Long, durationMs: Long, lastWatchedAt: Long)

    @Query("DELETE FROM WatchHistory WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM WatchHistory")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM WatchHistory")
    suspend fun countAll(): Int
}
