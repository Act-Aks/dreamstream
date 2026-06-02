package com.dreamstream.core.database.repository

import com.dreamstream.core.database.dao.PluginDao
import com.dreamstream.core.database.entity.InstalledPluginEntity
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.plugin.InstalledPlugin
import com.dreamstream.core.model.plugin.PluginManifest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

class PluginRepository(private val dao: PluginDao) {
    fun observeAll(): Flow<List<InstalledPlugin>> = dao.observeAll().map { entities ->
        entities.map { it.toDomain() }
    }

    fun observeEnabled(): Flow<List<InstalledPlugin>> = dao.observeEnabled().map { entities ->
        entities.map { it.toDomain() }
    }

    suspend fun getById(id: String) = dao.getById(id)?.toDomain()
    suspend fun insert(plugin: InstalledPlugin) = dao.insert(plugin.toEntity())
    suspend fun update(plugin: InstalledPlugin) = dao.update(plugin.toEntity())
    suspend fun setEnabled(id: String, enabled: Boolean) = dao.setEnabled(id, enabled)
    suspend fun updateVersion(
        id: String,
        version: Int,
        versionName: String,
        filePath: String,
        updatedAt: Long
    ) = dao.updateVersion(id, version, versionName, filePath, updatedAt)

    suspend fun deleteById(id: String) = dao.deleteById(id)
    suspend fun deleteAll() = dao.deleteAll()
    suspend fun countAll(): Int = dao.countAll()

    private fun InstalledPluginEntity.toDomain(): InstalledPlugin {
        val contentTypesJson = Json.decodeFromString<List<String>>(contentTypes)
        val contentTypes = contentTypesJson.mapNotNull {
            runCatching { ContentType.valueOf(it) }.getOrNull()
        }
        val authors = Json.decodeFromString<List<String>>(authors)

        return InstalledPlugin(
            manifest = PluginManifest(
                id = id,
                name = name,
                version = version,
                versionName = versionName,
                description = description,
                authors = authors,
                iconUrl = iconUrl,
                language = language,
                contentTypes = contentTypes,
                url = filePath,  // Note: PluginManifest.url = file_path
                repositoryUrl = repositoryUrl,
                requiresAppVersion = requiresAppVersion,
                isAdult = isAdult
            ),
            filePath = filePath,
            isEnabled = isEnabled,
            installedAt = installedAt,
            updatedAt = updatedAt
        )
    }

    private fun InstalledPlugin.toEntity(): InstalledPluginEntity {
        val contentTypes = Json.encodeToString(manifest.contentTypes.map { it.name })
        val authors = Json.encodeToString(manifest.authors)

        return InstalledPluginEntity(
            id = manifest.id,
            name = manifest.name,
            version = manifest.version,
            versionName = manifest.versionName,
            description = manifest.description,
            authors = authors,
            iconUrl = manifest.iconUrl,
            language = manifest.language,
            contentTypes = contentTypes,
            filePath = filePath,
            repositoryUrl = manifest.repositoryUrl,
            requiresAppVersion = manifest.requiresAppVersion,
            isEnabled = isEnabled,
            isAdult = manifest.isAdult,
            installedAt = installedAt,
            updatedAt = updatedAt
        )
    }
}
