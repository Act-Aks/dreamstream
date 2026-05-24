package com.dreamstream.core.model.plugin

import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.Serializable

/**
 * Metadata describing a provider plugin available in a repository.
 *
 * [id] is the stable identifier used for installation tracking and updates.
 * [sha256] is used to verify the downloaded plugin file before loading.
 * [isAdult] signals that parental controls should gate access to this plugin.
 */
@Serializable
data class PluginManifest(
    val id: String,
    val name: String,
    val version: Int,
    val versionName: String,
    val description: String,
    val authors: List<String> = emptyList(),
    val iconUrl: String? = null,
    val language: String = "en",
    val contentTypes: List<ContentType> = emptyList(),
    /** Download URL for the plugin artefact. */
    val url: String,
    val repositoryUrl: String,
    val fileSize: Long = 0L,
    val sha256: String = "",
    val requiresAppVersion: Int = 1,
    val changelog: String? = null,
    val repositoryName: String? = null,
    val isAdult: Boolean = false,
    val status: PluginStatus = PluginStatus.Available,
)
