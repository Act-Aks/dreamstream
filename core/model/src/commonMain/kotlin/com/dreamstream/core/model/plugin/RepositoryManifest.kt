package com.dreamstream.core.model.plugin

import kotlinx.serialization.Serializable

/** The root manifest document served by a plugin repository endpoint. */
@Serializable
data class RepositoryManifest(
    val name: String,
    val description: String? = null,
    val manifestVersion: Int = 1,
    val plugins: List<PluginManifest> = emptyList(),
)
