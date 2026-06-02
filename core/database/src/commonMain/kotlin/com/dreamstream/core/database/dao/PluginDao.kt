package com.dreamstream.core.database.dao

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.OnConflictStrategy
import androidx.room3.Query
import androidx.room3.Update
import com.dreamstream.core.database.entity.InstalledPluginEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PluginDao {
    @Query("SELECT * FROM InstalledPlugins ORDER BY name ASC")
    fun observeAll(): Flow<List<InstalledPluginEntity>>

    @Query("SELECT * FROM InstalledPlugins WHERE is_enabled = 1")
    fun observeEnabled(): Flow<List<InstalledPluginEntity>>

    @Query("SELECT * FROM InstalledPlugins WHERE id = :id")
    suspend fun getById(id: String): InstalledPluginEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: InstalledPluginEntity)

    @Update
    suspend fun update(entity: InstalledPluginEntity)

    @Query("UPDATE InstalledPlugins SET is_enabled = :enabled WHERE id = :id")
    suspend fun setEnabled(id: String, enabled: Boolean)

    @Query("UPDATE InstalledPlugins SET version = :version, version_name = :versionName, file_path = :filePath, updated_at = :updatedAt WHERE id = :id")
    suspend fun updateVersion(
        id: String,
        version: Int,
        versionName: String,
        filePath: String,
        updatedAt: Long
    )

    @Query("DELETE FROM InstalledPlugins WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM InstalledPlugins")
    suspend fun deleteAll()

    @Query("SELECT COUNT(*) FROM InstalledPlugins")
    suspend fun countAll(): Int
}
