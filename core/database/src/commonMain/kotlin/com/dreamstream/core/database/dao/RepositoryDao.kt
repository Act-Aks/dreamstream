package com.dreamstream.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.dreamstream.core.database.entity.RepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {
    @Query("SELECT * FROM Repositories ORDER BY added_at DESC")
    fun observeAll(): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM Repositories WHERE is_enabled = 1")
    fun observeEnabled(): Flow<List<RepositoryEntity>>

    @Query("SELECT * FROM Repositories WHERE url = :url")
    suspend fun getByUrl(url: String): RepositoryEntity?

    @Query("SELECT COUNT(*) > 0 FROM Repositories WHERE url = :url")
    suspend fun exists(url: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RepositoryEntity)

    @Update
    suspend fun update(entity: RepositoryEntity)

    @Query("UPDATE Repositories SET is_enabled = :enabled WHERE url = :url")
    suspend fun setEnabled(url: String, enabled: Boolean)

    @Query("UPDATE Repositories SET last_fetched = :lastFetched, name = :name, description = :description WHERE url = :url")
    suspend fun updateLastFetched(
        lastFetched: Long,
        name: String,
        description: String?,
        url: String
    )

    @Query("DELETE FROM Repositories WHERE url = :url")
    suspend fun deleteByUrl(url: String)

    @Query("DELETE FROM Repositories")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM Repositories")
    suspend fun countAll(): Int

}
