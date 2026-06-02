package com.dreamstream.core.model.plugin

import kotlinx.serialization.Serializable

/**
 * The root manifest document served by a plugin repository endpoint.
 *
 * [RepositoryManifest] is the JSON document fetched from a ***`PluginRepository`*** URL
 * (e.g., `https://repo.example.com/manifest.json`). It contains repository metadata
 * and the complete list of available [PluginManifest] entries.
 *
 * This class contains:
 * - **Identity**: [name], [description]
 * - **Versioning**: [manifestVersion] (schema version)
 * - **Contents**: [plugins] (list of all available plugins)
 *
 * ## Key Properties:
 * - [name]: Repository display name (***required***)
 * - [plugins]: List of plugin manifests (***required, defaults to empty***)
 * - [manifestVersion]: Schema version (***optional, defaults to 1***)
 *
 * ## Usage:
 * ```kotlin
 * // Fetch and decode repository manifest
 * val response = httpGet("https://repo.example.com/manifest.json")
 * val repositoryManifest = json.decodeFromString<RepositoryManifest>(response.body)
 *
 * // Create PluginRepository with fetched data
 * val repo = PluginRepository(
 *     url = "https://repo.example.com/manifest.json",
 *     name = repositoryManifest.name,
 *     description = repositoryManifest.description,
 *     manifestVersion = repositoryManifest.manifestVersion,
 *     plugins = repositoryManifest.plugins,
 *     isEnabled = true,
 *     lastFetched = System.currentTimeMillis(),
 *     addedAt = System.currentTimeMillis()
 * )
 * ```
 *
 * ## JSON Example:
 * ```json
 * {
 *   "name": "Official Repository",
 *   "description": "Official DreamStream plugins maintained by the team",
 *   "manifestVersion": 2,
 *   "plugins": [
 *     {
 *       "id": "anime-provider",
 *       "name": "Anime Provider",
 *       "version": 2,
 *       "versionName": "2.0.0",
 *       "description": "Watch anime from multiple sources",
 *       "url": "https://repo.example.com/plugins/anime-provider-v2.jar",
 *       "repositoryUrl": "https://repo.example.com/manifest.json",
 *       "sha256": "abc123...",
 *       "contentTypes": ["ANIME"],
 *       "isAdult": false,
 *       "status": "Available"
 *     },
 *     {
 *       "id": "movie-provider",
 *       "name": "Movie Provider",
 *       "version": 1,
 *       "versionName": "1.0.0",
 *       "description": "Watch movies",
 *       "url": "https://repo.example.com/plugins/movie-provider-v1.jar",
 *       "repositoryUrl": "https://repo.example.com/manifest.json",
 *       "sha256": "def456...",
 *       "contentTypes": ["MOVIE"],
 *       "isAdult": false,
 *       "status": "Available"
 *     }
 *   ]
 * }
 * ```
 *
 * ## Repository Flow:
 * 1. User adds repository URL → store URL in ***`PluginRepository`***
 * 2. Fetch manifest from URL → decode as [RepositoryManifest]
 * 3. Extract [name], [description], [plugins]
 * 4. Create/update [PluginRepository] with fetched data
 * 5. Display [plugins] in the Plugin Store screen
 *
 * ## Validation:
 * ```kotlin
 * fun validateManifest(manifest: RepositoryManifest): Boolean {
 *     if (manifest.name.isBlank()) return false
 *     if (manifest.manifestVersion < 1) return false
 *     if (manifest.plugins.any { it.id.isBlank() || it.url.isBlank() }) return false
 *     return true
 * }
 * ```
 *
 * ## Related:
 * - Plugin manifest: [PluginManifest]
 * - Repository model: ***`PluginRepository`***
 * - Manager: ***`PluginManager`***
 * - Error: [com.dreamstream.core.domain.util.DreamError.RepositoryFetchFailed]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class RepositoryManifest(
    /**
     * Repository display name.
     *
     * ***Required***. Human-readable name shown in the repository settings and plugin store.
     * Example: `"Official Repository"`, `"Community Plugins"`
     */
    val name: String,

    /**
     * Repository description.
     *
     * ***Optional***. Null if not provided. Shown below the name in the UI.
     * Example: `"Official DreamStream plugins maintained by the team"`
     */
    val description: String? = null,

    /**
     * Manifest schema version.
     *
     * ***Optional***. Defaults to `1`. Used to detect breaking changes in
     * the repository manifest format. Increment when the schema changes.
     * Example: `1`, `2`, `3`
     */
    val manifestVersion: Int = 1,

    /**
     * List of plugins available in this repository.
     *
     * ***Required***. Defaults to empty list. Contains [PluginManifest] entries
     * for all plugins published in this repository. Populated after fetching
     * the manifest from the repository URL.
     */
    val plugins: List<PluginManifest> = emptyList(),
)
