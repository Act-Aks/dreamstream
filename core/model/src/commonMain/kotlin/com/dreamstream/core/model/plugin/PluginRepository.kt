package com.dreamstream.core.model.plugin

import kotlinx.serialization.Serializable

/**
 * A remote plugin repository that the app can fetch and browse.
 *
 * [lastFetched] is a Unix epoch timestamp in milliseconds. Null means the
 * repository has been added but never fetched.
 */
@Serializable
data class PluginRepository(
    val url: String,
    val name: String,
    val description: String? = null,
    val manifestVersion: Int = 1,
    val plugins: List<PluginManifest> = emptyList(),
    val isEnabled: Boolean = true,
    val lastFetched: Long? = null,
    val addedAt: Long = 0L,
) {
    val pluginCount: Int get() = plugins.size
    val displayName: String get() = name.ifBlank { url }
}
