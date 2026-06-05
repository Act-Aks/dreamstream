package com.dreamstream.core.domain.repository

import com.dreamstream.core.domain.model.plugin.InstalledPlugin
import com.dreamstream.core.domain.model.plugin.PluginManifest
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import com.dreamstream.core.domain.model.plugin.PluginRepository as PluginRepositoryModel

interface PluginRepository {
    fun observeInstalledPlugins(): Flow<List<InstalledPlugin>>
    fun observeRepositories(): Flow<List<PluginRepositoryModel>>

    suspend fun addRepository(url: String): Result<PluginRepositoryModel, DreamError>

    suspend fun removeRepository(url: String): EmptyResult<DreamError>

    suspend fun refreshRepositories(): EmptyResult<DreamError>

    suspend fun installPlugin(manifest: PluginManifest): EmptyResult<DreamError>

    suspend fun uninstallPlugin(pluginId: String): EmptyResult<DreamError>

    suspend fun setPluginEnabled(pluginId: String, enabled: Boolean): EmptyResult<DreamError>

    suspend fun setRepositoryEnabled(url: String, enabled: Boolean): EmptyResult<DreamError>
}
