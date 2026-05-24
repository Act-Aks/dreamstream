package com.dreamstream.core.model.plugin

import kotlinx.serialization.Serializable

/**
 * A plugin that has been installed on this device.
 *
 * [filePath] is the absolute path to the installed plugin artefact.
 * [installedAt] and [updatedAt] are Unix epoch timestamps in milliseconds.
 */
@Serializable
data class InstalledPlugin(
    val manifest: PluginManifest,
    val filePath: String,
    val isEnabled: Boolean = true,
    val installedAt: Long = 0L,
    val updatedAt: Long = 0L,
)
