package com.dreamstream.core.plugin.loader

import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.extensions.info
import com.dreamstream.core.domain.extensions.warn
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.model.plugin.InstalledPlugin
import com.dreamstream.core.domain.model.plugin.PluginManifest
import com.dreamstream.core.domain.model.plugin.PluginRepository
import com.dreamstream.core.domain.model.plugin.PluginStatus
import com.dreamstream.core.domain.model.plugin.RepositoryManifest
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.Result
import com.dreamstream.plugin.api.extractor.Extractor
import com.dreamstream.plugin.api.plugin.DreamPlugin
import com.dreamstream.plugin.api.plugin.PluginApiVersion
import com.dreamstream.plugin.api.provider.ContentProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okio.Path
import okio.Path.Companion.toPath
import kotlin.time.Clock

class PluginManager(
    private val pluginClassLoader: PluginClassLoader,
    private val pluginInstaller: PluginInstaller,
    private val pluginRegistry: PluginRegistry,
    private val httpClient: HttpClient,
    private val json: Json,
    private val pluginContextFactory: PluginContextFactory,
    private val storageProvider: AppStorageProvider,
    loggerFactory: LoggerFactory,
) {
    private val logger = loggerFactory.get("PluginManager")
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Repository state
    private val _repositories = MutableStateFlow<List<PluginRepository>>(emptyList())
    val repositories: StateFlow<List<PluginRepository>> = _repositories.asStateFlow()

    // Installed plugins state
    private val _installedPlugins = MutableStateFlow<List<InstalledPlugin>>(emptyList())
    val installedPlugins: StateFlow<List<InstalledPlugin>> = _installedPlugins.asStateFlow()

    // Available plugins from all repos
    val availablePlugins: Flow<List<PluginManifest>> = _repositories.map { repos ->
        repos.filter { it.isEnabled }.flatMap { it.plugins }.distinctBy { it.id }
    }

    // Loaded plugins
    val loadedPlugins = pluginRegistry.plugins

    // ---- Repository Management ----

    suspend fun addRepository(url: String): Result<PluginRepository, DreamError> =
        withContext(Dispatchers.Default) {
            runCatching {
                val trimmedUrl = url.trim()
                val response = httpClient.get(trimmedUrl)
                if (!response.status.isSuccess()) {
                    return@withContext Result.Error(
                        DreamError.RepositoryFetchFailed(trimmedUrl)
                    )
                }

                val repoManifest = json.decodeFromString<RepositoryManifest>(
                    response.bodyAsText()
                )

                val repository = PluginRepository(
                    url = trimmedUrl,
                    name = repoManifest.name,
                    description = repoManifest.description,
                    plugins = repoManifest.plugins.map { plugin ->
                        plugin.copy(
                            repositoryUrl = trimmedUrl,
                            repositoryName = repoManifest.name,
                            status = getPluginStatus(plugin),
                        )
                    },
                    addedAt = currentTimeMillis(),
                    lastFetched = currentTimeMillis(),
                )

                _repositories.update { current ->
                    // Replace if exists, add if new
                    val filtered = current.filter { it.url != trimmedUrl }
                    filtered + repository
                }

                logger.info { "Added repository: ${repository.name}" }
                Result.Success(repository)
            }.getOrElse {
                logger.error(it) { "Failed to add repository: $url" }
                Result.Error(DreamError.RepositoryFetchFailed(url.trim(), it))
            }
        }

    suspend fun removeRepository(url: String) {
        _repositories.update { current -> current.filter { it.url != url } }
    }

    suspend fun refreshRepositories() {
        _repositories.value.forEach { repo ->
            addRepository(repo.url)
        }
    }

    suspend fun setRepositoryEnabled(url: String, enabled: Boolean) {
        _repositories.update { current ->
            current.map { if (it.url == url) it.copy(isEnabled = enabled) else it }
        }
    }

    // ---- Plugin Installation ----

    fun installPlugin(manifest: PluginManifest): Flow<InstallState> =
        pluginInstaller.install(manifest).also { flow ->
            scope.launch {
                flow.collect { state ->
                    if (state is InstallState.Success) {
                        val filePath =
                            (storageProvider.pluginsDir / "${manifest.id}$PLUGIN_FILE_EXTENSION").toString()
                        val installed = InstalledPlugin(
                            manifest = manifest,
                            filePath = filePath,
                            isEnabled = true,
                            installedAt = currentTimeMillis(),
                            updatedAt = currentTimeMillis(),
                        )
                        _installedPlugins.update { current ->
                            val filtered = current.filter { it.manifest.id != manifest.id }
                            filtered + installed
                        }
                        // Autoload after install
                        loadPlugin(installed)
                    }
                }
            }
        }

    suspend fun uninstallPlugin(pluginId: String): Result<Unit, DreamError> {
        // Unload from registry
        pluginRegistry.unregister(pluginId)
        pluginClassLoader.release(pluginId)

        // Remove from installer
        val result = pluginInstaller.uninstall(pluginId)

        // Remove from state
        _installedPlugins.update { current ->
            current.filter { it.manifest.id != pluginId }
        }

        return result
    }

    suspend fun setPluginEnabled(pluginId: String, enabled: Boolean) {
        _installedPlugins.update { current ->
            current.map {
                if (it.manifest.id == pluginId) it.copy(isEnabled = enabled) else it
            }
        }
        pluginRegistry.setEnabled(pluginId, enabled)
    }

    // ---- Plugin Loading ----

    /**
     * Load all installed and enabled plugins at app startup.
     */
    suspend fun loadInstalledPlugins(installedPlugins: List<InstalledPlugin>) {
        _installedPlugins.update { installedPlugins }
        installedPlugins.filter { it.isEnabled }.forEach { plugin ->
            loadPlugin(plugin)
        }
    }

    private fun loadPlugin(installedPlugin: InstalledPlugin) {
        if (pluginRegistry.isPluginLoaded(installedPlugin.manifest.id)) return

        runCatching {
            val pluginFile: Path = installedPlugin.filePath.toPath()

            // Read manifest to get main class
            val fileManifest = readFileManifest(pluginFile)
                ?: error("Cannot read manifest from ${installedPlugin.filePath}")

            // Check API compatibility
            if (!PluginApiVersion.isCompatible(fileManifest.requiresAppVersion)) {
                logger.warn { "Plugin ${installedPlugin.manifest.id} requires API " + "${fileManifest.requiresAppVersion}, skipping" }
                return
            }

            // Load plugin class
            val dreamPlugin: DreamPlugin =
                pluginClassLoader.load(pluginFile, fileManifest.mainClass)

            // Create context and initialize
            val context = pluginContextFactory.create(installedPlugin.manifest.id)
            dreamPlugin.initialize(context)

            // Get providers and inject HTTP client
            val providers = dreamPlugin.registerProviders().onEach { provider ->
                provider.inject(httpClient, context)
            }
            val extractors = dreamPlugin.registerExtractors()

            // Register in registry
            pluginRegistry.register(
                LoadedPlugin(
                    id = installedPlugin.manifest.id,
                    plugin = dreamPlugin,
                    providers = providers,
                    extractors = extractors,
                    isEnabled = installedPlugin.isEnabled,
                )
            )

            logger.info { "Loaded plugin: ${installedPlugin.manifest.id} " + "with ${providers.size} providers" }
        }.onFailure { throwable ->
            logger.error(throwable) { "Failed to load plugin: ${installedPlugin.manifest.id}" }
        }
    }

    // ---- Provider & Extractor Access ----

    fun getAllProviders(): List<ContentProvider> = pluginRegistry.getAllProviders()

    fun getAllExtractors(): List<Extractor> = pluginRegistry.getAllExtractors()

    fun getExtractorForUrl(url: String): Extractor? = pluginRegistry.getExtractorForUrl(url)

    fun getProviderById(providerId: String): ContentProvider? =
        pluginRegistry.getProviderById(providerId)

    // ---- Private Helpers ----

    private fun getPluginStatus(manifest: PluginManifest): PluginStatus {
        val installed = _installedPlugins.value.find { it.manifest.id == manifest.id }
            ?: return PluginStatus.Available

        return if (installed.manifest.version < manifest.version) {
            PluginStatus.UpdateAvailable
        } else {
            PluginStatus.Installed
        }
    }

    private fun readFileManifest(filePath: Path): PluginFileManifest? = runCatching {
        val file = java.io.File(filePath.toString())
        java.util.zip.ZipFile(file).use { zip ->
            val entry = zip.getEntry("manifest.json") ?: return@runCatching null
            val content = zip.getInputStream(entry).bufferedReader().readText()
            json.decodeFromString<PluginFileManifest>(content)
        }
    }.getOrNull()

    private fun currentTimeMillis(): Long = Clock.System.now().toEpochMilliseconds()
}
