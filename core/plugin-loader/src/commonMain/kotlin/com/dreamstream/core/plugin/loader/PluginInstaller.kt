package com.dreamstream.core.plugin.loader

import com.dreamstream.core.domain.extensions.debug
import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.extensions.info
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.model.plugin.PluginManifest
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.Result
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import okio.Path
import okio.buffer

sealed interface InstallState {
    data object Downloading : InstallState
    data class Progress(val percent: Int) : InstallState
    data object Verifying : InstallState
    data object Installing : InstallState
    data object Success : InstallState
    data class Error(val error: DreamError) : InstallState
}

class PluginInstaller(
    private val httpClient: HttpClient,
    private val json: Json,
    private val storageProvider: AppStorageProvider,
    private val verifier: PluginVerifier,
    loggerFactory: LoggerFactory,
) {

    private val fileSystem = storageProvider.fileSystem
    private val pluginsDir = storageProvider.pluginsDir
    private val logger = loggerFactory.get("PluginInstaller")

    /**
     * Download, verify and install a plugin from its manifest.
     * Emits [InstallState] progress updates.
     */
    fun install(manifest: PluginManifest): Flow<InstallState> = flow {
        emit(InstallState.Downloading)
        logger.debug { "Downloading plugin: ${manifest.id} from ${manifest.url}" }

        // Download plugin file
        val pluginBytes = runCatching {
            val response = httpClient.get(manifest.url)
            if (!response.status.isSuccess()) {
                logger.error { "Download failed with status ${response.status}" }
                emit(InstallState.Error(DreamError.Network(code = response.status.value)))
                return@flow
            }
            response.bodyAsBytes()
        }.getOrElse { throwable ->
            logger.error(throwable) { "Download failed for ${manifest.id}" }
            emit(
                InstallState.Error(
                    DreamError.Network(
                        message = throwable.message, cause = throwable
                    )
                )
            )
            return@flow
        }

        logger.debug { "Downloaded ${pluginBytes.size} bytes for ${manifest.id}" }

        // Save to temp file
        val tempFile = pluginsDir / "${manifest.id}.tmp"
        val targetFile = pluginsDir / "${manifest.id}$PLUGIN_FILE_EXTENSION"

        runCatching {
            fileSystem.sink(tempFile).buffer().use { sink ->
                sink.write(pluginBytes)
            }
        }.getOrElse { throwable ->
            logger.error(throwable) { "Failed to save temp file for ${manifest.id}" }
            emit(InstallState.Error(DreamError.Unknown(throwable)))
            return@flow
        }

        // Verify checksum
        emit(InstallState.Verifying)
        logger.debug { "Verifying checksum for ${manifest.id}" }
        when (val checksumResult = verifier.verifyChecksum(tempFile, manifest.sha256)) {
            is Result.Success -> Unit
            is Result.Error -> {
                fileSystem.delete(tempFile)
                logger.error { "Checksum verification failed for ${manifest.id}" }
                emit(InstallState.Error(checksumResult.error))
                return@flow
            }
        }

        // Read and verify embedded manifest
        val fileManifest = runCatching {
            readManifestFromPlugin(tempFile)
        }.getOrElse { throwable ->
            fileSystem.delete(tempFile)
            logger.error(throwable) { "Failed to read manifest from ${manifest.id}" }
            emit(InstallState.Error(DreamError.PluginLoadFailed(manifest.id, throwable)))
            return@flow
        }

        when (val manifestResult = verifier.verifyManifest(fileManifest)) {
            is Result.Success -> Unit
            is Result.Error -> {
                fileSystem.delete(tempFile)
                emit(InstallState.Error(manifestResult.error))
                return@flow
            }
        }

        // Move to final location
        emit(InstallState.Installing)
        logger.debug { "Installing ${manifest.id} to $targetFile" }
        runCatching {
            if (fileSystem.exists(targetFile)) {
                fileSystem.delete(targetFile)
            }
            fileSystem.atomicMove(tempFile, targetFile)
        }.getOrElse { throwable ->
            logger.error(throwable) { "Failed to move ${manifest.id} to final location" }
            emit(InstallState.Error(DreamError.Unknown(throwable)))
            return@flow
        }

        logger.info { "Plugin installed: ${manifest.id} -> $targetFile" }
        emit(InstallState.Success)
    }

    /**
     * Remove an installed plugin file.
     */
    fun uninstall(pluginId: String): Result<Unit, DreamError> = runCatching {
        val pluginFile = pluginsDir / "$pluginId$PLUGIN_FILE_EXTENSION"
        if (fileSystem.exists(pluginFile)) {
            fileSystem.delete(pluginFile)
            logger.info { "Plugin uninstalled: $pluginId" }
        }
        Result.Success(Unit)
    }.getOrElse { throwable ->
        logger.error(throwable) { "Failed to uninstall $pluginId" }
        Result.Error(DreamError.Unknown(throwable))
    }

    private fun readManifestFromPlugin(filePath: Path): PluginFileManifest {
        val file = java.io.File(filePath.toString())
        java.util.zip.ZipFile(file).use { zip ->
            val entry =
                zip.getEntry("manifest.json") ?: error("Plugin missing manifest.json: $filePath")
            val content = zip.getInputStream(entry).bufferedReader().readText()
            return json.decodeFromString(content)
        }
    }
}
