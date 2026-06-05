package com.dreamstream.core.data.repository

import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.model.plugin.InstalledPlugin
import com.dreamstream.core.domain.model.plugin.PluginManifest
import com.dreamstream.core.domain.repository.PluginRepository
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.core.domain.system.TimeProvider
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.EmptyResult
import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.plugin.loader.InstallState
import com.dreamstream.core.plugin.loader.PLUGIN_FILE_EXTENSION
import com.dreamstream.core.plugin.loader.PluginManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import com.dreamstream.core.database.repository.PluginRepository as DbPluginRepository
import com.dreamstream.core.database.repository.RepositoryRepository as DbRepositoryRepository
import com.dreamstream.core.domain.model.plugin.PluginRepository as PluginRepositoryModel

class PluginRepositoryImpl(
    private val pluginManager: PluginManager,
    private val pluginRepository: DbPluginRepository,
    private val repositoryRepository: DbRepositoryRepository,
    private val storageProvider: AppStorageProvider,
    private val timeProvider: TimeProvider,
    loggerFactory: LoggerFactory
) : PluginRepository {

    private val logger = loggerFactory.get("PluginRepositoryImpl")

    override fun observeInstalledPlugins(): Flow<List<InstalledPlugin>> =
        pluginRepository.observeAll()

    override fun observeRepositories(): Flow<List<PluginRepositoryModel>> =
        repositoryRepository.observeAll().map { repos ->
            repos.map { repo ->
                // Merge with live plugin manager state for accurate statuses
                repo.copy(
                    plugins = pluginManager.repositories.value.find { it.url == repo.url }?.plugins
                        ?: repo.plugins
                )
            }
        }.catch { e ->
            logger.error(e) { "Error observing repositories" }
            emit(emptyList())
        }

    override suspend fun addRepository(
        url: String,
    ): Result<PluginRepositoryModel, DreamError> {
        val result = pluginManager.addRepository(url)

        if (result is Result.Success) {
            val repo = result.data
            // Persist to database
            runCatching {
                if (!repositoryRepository.exists(repo.url)) {
                    repositoryRepository.insert(repo)
                } else {
                    repositoryRepository.updateLastFetched(
                        url = repo.url,
                        lastFetched = timeProvider.currentTimeMillis(),
                        name = repo.name,
                        description = repo.description,
                    )
                }
            }.onFailure { logger.error(it) { "Failed to persist repository" } }
        }

        return result
    }

    override suspend fun removeRepository(url: String): EmptyResult<DreamError> = try {
        pluginManager.removeRepository(url)
        repositoryRepository.deleteByUrl(url)
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to remove repository" }
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun refreshRepositories(): EmptyResult<DreamError> = runCatching {
        pluginManager.refreshRepositories()

        // Update persisted repos
        repositoryRepository.getAll().forEach { repo ->
            repositoryRepository.updateLastFetched(
                url = repo.url,
                lastFetched = timeProvider.currentTimeMillis(),
                name = repo.name,
                description = repo.description,
            )
        }
        Result.Success(Unit)
    }.getOrElse {
        Result.Error(DreamError.Unknown(it))
    }

    override suspend fun installPlugin(
        manifest: PluginManifest,
    ): EmptyResult<DreamError> {
        var finalResult: Result<Unit, DreamError> = Result.Error(DreamError.Unknown())

        pluginManager.installPlugin(manifest).collect { state ->
            when (state) {
                is InstallState.Success -> {
                    // Persist to database
                    val filePath =
                        "${storageProvider.pluginsDir}/${manifest.id}$PLUGIN_FILE_EXTENSION"
                    val installed = InstalledPlugin(
                        manifest = manifest,
                        filePath = filePath,
                        isEnabled = true,
                        installedAt = timeProvider.currentTimeMillis(),
                        updatedAt = timeProvider.currentTimeMillis(),
                    )
                    runCatching { pluginRepository.insert(installed) }
                    finalResult = Result.Success(Unit)
                }

                is InstallState.Error -> {
                    finalResult = Result.Error(state.error)
                }

                else -> { /* Progress states */
                }
            }
        }

        return finalResult
    }

    override suspend fun uninstallPlugin(pluginId: String): EmptyResult<DreamError> {
        val result = pluginManager.uninstallPlugin(pluginId)
        if (result is Result.Success) {
            runCatching { pluginRepository.deleteById(pluginId) }
        }
        return result
    }

    override suspend fun setPluginEnabled(
        pluginId: String, enabled: Boolean
    ): EmptyResult<DreamError> = try {
        pluginManager.setPluginEnabled(pluginId, enabled)
        pluginRepository.setEnabled(pluginId, enabled)
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to enable plugin" }
        Result.Error(DreamError.Unknown(e))
    }

    override suspend fun setRepositoryEnabled(
        url: String, enabled: Boolean
    ): EmptyResult<DreamError> = try {
        pluginManager.setRepositoryEnabled(url, enabled)
        repositoryRepository.setEnabled(url, enabled)
        Result.Success(Unit)
    } catch (e: Throwable) {
        logger.error(e) { "Failed to enable repository" }
        Result.Error(DreamError.Unknown(e))
    }
}
