package com.dreamstream.core.plugin.loader

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Manifest embedded inside a [PLUGIN_FILE_EXTENSION] plugin file (manifest.json).
 * This is different from [com.dreamstream.core.domain.model.plugin.PluginManifest]
 * which is the repository-level manifest.
 */
@Serializable
data class PluginFileManifest(
    @SerialName("id")
    val id: String,

    @SerialName("name")
    val name: String,

    @SerialName("version")
    val version: Int,

    @SerialName("version_name")
    val versionName: String,

    @SerialName("description")
    val description: String = "",

    @SerialName("authors")
    val authors: List<String> = emptyList(),

    @SerialName("main_class")
    val mainClass: String,

    @SerialName("language")
    val language: String = "en",

    @SerialName("content_types")
    val contentTypes: List<String> = emptyList(),

    @SerialName("requires_app_version")
    val requiresAppVersion: Int = 1,

    @SerialName("icon_url")
    val iconUrl: String? = null,

    @SerialName("is_adult")
    val isAdult: Boolean = false,
)
