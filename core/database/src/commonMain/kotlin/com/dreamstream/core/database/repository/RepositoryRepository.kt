package com.dreamstream.core.database.repository

import com.dreamstream.core.database.dao.RepositoryDao
import com.dreamstream.core.database.entity.RepositoryEntity
import com.dreamstream.core.model.plugin.PluginRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class RepositoryRepository(private val dao: RepositoryDao) {
    fun observeAll(): Flow<List<PluginRepository>> = dao.observeAll().map { entities ->
        entities.map { it.toDomain() }
    }

    fun observeEnabled(): Flow<List<PluginRepository>> = dao.observeEnabled().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getAll() = dao.observeAll().first().map { it.toDomain() }
    suspend fun getByUrl(url: String) = dao.getByUrl(url)?.toDomain()
    suspend fun exists(url: String) = dao.exists(url)
    suspend fun insert(repository: PluginRepository) = dao.insert(repository.toEntity())
    suspend fun update(repository: PluginRepository) = dao.update(repository.toEntity())
    suspend fun setEnabled(url: String, enabled: Boolean) = dao.setEnabled(url, enabled)
    suspend fun updateLastFetched(
        url: String,
        lastFetched: Long,
        name: String,
        description: String
    ) = dao.updateLastFetched(lastFetched, name, description, url)

    suspend fun deleteByUrl(url: String) = dao.deleteByUrl(url)
    suspend fun deleteAll() = dao.deleteAll()

    private fun RepositoryEntity.toDomain() = PluginRepository(
        url = url,
        name = name,
        description = description,
        manifestVersion = manifestVersion,
        isEnabled = isEnabled,
        lastFetched = lastFetched,
        addedAt = addedAt
    )

    private fun PluginRepository.toEntity() = RepositoryEntity(
        url = url,
        name = name,
        description = description,
        manifestVersion = manifestVersion,
        isEnabled = isEnabled,
        lastFetched = lastFetched,
        addedAt = addedAt
    )
}
