package com.dreamstream.core.model.plugin

import kotlinx.serialization.Serializable

/**
 * A remote plugin repository that the app can fetch and browse.
 *
 * [PluginRepository] represents a source of provider plugins (a "plugin store")
 * that DreamStream can download, parse, and display in the **Plugin Store** screen.
 * It is managed by ***`PluginManager`*** and fetched from a remote manifest URL.
 *
 * This class contains:
 * - **Identity**: [url], [name], [description]
 * - **State**: [isEnabled], [lastFetched], [addedAt], [manifestVersion]
 * - **Contents**: [plugins] (list of [PluginManifest]), [pluginCount] (computed)
 *
 * ## Key Properties:
 * - [url]: Repository manifest URL (***required, unique***)
 * - [name]: Display name (***required***)
 * - [plugins]: List of available plugins (***computed from remote manifest***)
 * - [lastFetched]: Last fetch timestamp (***optional, null = never fetched***)
 * - [isEnabled]: Active state (***optional, defaults to true***)
 *
 * ## Timestamps:
 * - [lastFetched]: Unix epoch timestamp in **milliseconds**. `null` means the
 *   repository was added but never fetched.
 *   Example: `1716945600000L` = May 29, 2024 00:00:00 UTC
 * - [addedAt]: Unix epoch timestamp when the repository was added (defaults to `0L`)
 *
 * ## Usage:
 * ```kotlin
 * val repo = PluginRepository(
 *     url = "https://repo.example.com/manifest.json",
 *     name = "Official Repository",
 *     description = "Official DreamStream plugins",
 *     manifestVersion = 2,
 *     plugins = listOf(
 *         PluginManifest(id = "anime-provider", name = "Anime Provider", ...),
 *         PluginManifest(id = "movie-provider", name = "Movie Provider", ...)
 *     ),
 *     isEnabled = true,
 *     lastFetched = 1716945600000L,
 *     addedAt = 1716859200000L
 * )
 *
 * if (repo.isEnabled) {
 *     pluginManager.loadPluginsFrom(repo)
 * }
 * ```
 *
 * ## Repository Flow:
 * 1. User adds repository URL → create [PluginRepository] with `lastFetched = null`
 * 2. Fetch manifest from [url] → parse into list of [PluginManifest]
 * 3. Update [plugins] and set [lastFetched] = current timestamp
 * 4. Display [plugins] in the Plugin Store screen
 * 5. User installs plugin → download from [PluginManifest.url]
 *
 * ## Fetching Example:
 * ```kotlin
 * suspend fun fetchRepository(url: String): PluginRepository {
 *     val response = httpGet(url)
 *     val manifest = json.decodeFromString<RepositoryManifest>(response.body)
 *
 *     return PluginRepository(
 *         url = url,
 *         name = manifest.name,
 *         description = manifest.description,
 *         manifestVersion = manifest.version,
 *         plugins = manifest.plugins,
 *         isEnabled = true,
 *         lastFetched = System.currentTimeMillis(),
 *         addedAt = System.currentTimeMillis()
 *     )
 * }
 * ```
 *
 * ## UI Representation:
 * - Displayed in the **Repository Settings** screen
 * - [name] shown as repository title
 * - [description] shown as subtitle
 * - [pluginCount] shown as "X plugins available"
 * - [lastFetched] shown as "Last fetched: May 29, 2024"
 * - [isEnabled] controls the toggle switch
 * - "Fetch" button triggers re-download of manifest
 * - "Remove" button deletes the repository
 *
 * ## Computed Property:
 * [pluginCount] returns the number of plugins in this repository:
 * ```kotlin
 * val count = repo.pluginCount // Equivalent to repo.plugins.size
 * ```
 *
 * ## Repository Management:
 * ```kotlin
 * val repos = pluginManager.getRepositories()
 * val enabledRepos = repos.filter { it.isEnabled }
 * val staleRepos = enabledRepos.filter {
 *     it.lastFetched == null || (System.currentTimeMillis() - it.lastFetched) > 7 * 24 * 60 * 60 * 1000
 * }
 * staleRepos.forEach { pluginManager.fetchRepository(it.url) }
 * ```
 *
 * ## Related:
 * - Plugin manifest: [PluginManifest]
 * - Installed plugin: [InstalledPlugin]
 * - Manager: ***`PluginManager`***
 * - Provider: ***`ContentProvider`***
 * - Error: [com.dreamstream.core.domain.util.DreamError.RepositoryFetchFailed]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class PluginRepository(
    /**
     * Repository manifest URL.
     *
     * ***Required, unique***. Direct link to the JSON manifest file containing
     * the list of [PluginManifest] entries. Used to fetch and refresh the repository.
     *
     * Example: `"https://repo.example.com/manifest.json"`
     */
    val url: String,

    /**
     * Human-readable repository name.
     *
     * ***Required***. Displayed in the repository settings and plugin store.
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
     */
    val manifestVersion: Int = 1,

    /**
     * List of plugins available in this repository.
     *
     * ***Computed from remote manifest***. Defaults to empty list.
     * Populated after fetching the repository URL. Contains [PluginManifest]
     * entries for all available plugins.
     */
    val plugins: List<PluginManifest> = emptyList(),

    /**
     * Whether the repository is currently enabled.
     *
     * ***Optional***. Defaults to `true`. Set to `false` to temporarily disable
     * a repository without removing it. Disabled repositories are skipped during
     * plugin updates and content loading.
     */
    val isEnabled: Boolean = true,

    /**
     * Last fetch timestamp (Unix epoch, milliseconds).
     *
     * ***Optional***. `null` means the repository was added but never fetched.
     * Example: `1716945600000L` = May 29, 2024 00:00:00 UTC.
     * Updated after successfully downloading the manifest.
     */
    val lastFetched: Long? = null,

    /**
     * Timestamp when the repository was added (Unix epoch, milliseconds).
     *
     * ***Optional***. Defaults to `0L` (not set). Same format as [lastFetched].
     * Used to display "Added on: ..." in repository details.
     */
    val addedAt: Long = 0L,
) {
    /**
     * Number of plugins in this repository.
     *
     * ***Computed***. Returns the size of [plugins].
     * Displayed in the UI as "X plugins available".
     */
    val pluginCount: Int get() = plugins.size
}
