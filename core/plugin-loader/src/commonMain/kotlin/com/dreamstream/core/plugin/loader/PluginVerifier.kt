package com.dreamstream.core.plugin.loader

import com.dreamstream.core.domain.extensions.debug
import com.dreamstream.core.domain.extensions.error
import com.dreamstream.core.domain.extensions.warn
import com.dreamstream.core.domain.logger.LoggerFactory
import com.dreamstream.core.domain.system.AppStorageProvider
import com.dreamstream.core.domain.util.DreamError
import com.dreamstream.core.domain.util.Result
import com.dreamstream.core.domain.util.Result.Error
import com.dreamstream.core.domain.util.Result.Success
import com.dreamstream.plugin.api.plugin.PluginApiVersion
import okio.HashingSink
import okio.Path
import okio.blackholeSink
import okio.buffer


class PluginVerifier(
    storageProvider: AppStorageProvider,
    loggerFactory: LoggerFactory
) {
    private val fileSystem = storageProvider.fileSystem
    private val logger = loggerFactory.get("PluginVerifier")


    /**
     * Verify SHA-256 checksum of a plugin file.
     */
    fun verifyChecksum(filePath: Path, expectedSha256: String): Result<Unit, DreamError> {
        if (expectedSha256.isBlank()) {
            logger.warn { "No checksum provided, skipping verification" }
            return Success(Unit)
        }

        return runCatching {
            val hashingSink = HashingSink.sha256(blackholeSink())
            fileSystem.source(filePath).buffer().use { source ->
                hashingSink.buffer().use { sink ->
                    sink.writeAll(source)
                }
            }
            val actualHash = hashingSink.hash.hex()
            logger.debug { "Plugin hash: $actualHash (expected: $expectedSha256)" }

            if (actualHash.equals(expectedSha256, ignoreCase = true)) {
                Success(Unit)
            } else {
                Error(
                    DreamError.ChecksumMismatch(actualHash)
                )
            }
        }.getOrElse { throwable ->
            logger.error(throwable) { "Failed to verify checksum for $filePath" }
            Error(DreamError.Unknown(throwable))
        }
    }

    /**
     * Verify the plugin manifest is readable and compatible.
     */
    fun verifyManifest(manifest: PluginFileManifest): Result<Unit, DreamError> {
        // Check API version compatibility
        if (!PluginApiVersion.isCompatible(manifest.requiresAppVersion)) {
            val message =
                "Plugin ${manifest.id} requires API version ${manifest.requiresAppVersion}, " +
                    "but app supports ${PluginApiVersion.MIN_SUPPORTED}..${PluginApiVersion.CURRENT}"
            logger.error { message }
            return Error(
                DreamError.PluginLoadFailed(
                    pluginId = manifest.id,
                    cause = IllegalStateException(message)
                )
            )
        }

        // Check main class is present
        if (manifest.mainClass.isBlank()) {
            val message = "Plugin manifest missing mainClass for ${manifest.id}"
            logger.error { message }
            return Error(
                DreamError.PluginLoadFailed(
                    pluginId = manifest.id,
                    cause = IllegalStateException(message)
                )
            )
        }

        logger.debug { "Plugin manifest verified: ${manifest.id} v${manifest.versionName}" }
        return Success(Unit)
    }
}
