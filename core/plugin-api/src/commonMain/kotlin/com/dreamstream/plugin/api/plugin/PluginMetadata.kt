/**
 * API types for the Dreamstream plugin system.
 *
 * This package contains annotation and other API surface types that plugins
 * implement to expose metadata consumed by the host application at runtime.
 * The host uses values such as `id`, `version`, `requiresAppVersion` and
 * `contentTypes` to validate, categorize and display plugins before loading
 * them.
 *
 * Guidelines:
 * - Prefer a reverse-domain style for [id] (for global uniqueness).
 * - Increment [version] for programmatic compatibility changes; change
 *   [versionName] for human-readable releases.
 * - Keep [description] short and user-facing.
 */
package com.dreamstream.plugin.api.plugin

/**
 * Declares metadata for a Dreamstream plugin implementation.
 *
 * Apply this annotation to your plugin's main class. Values are retained at
 * runtime so the host can discover plugin properties via reflection and use
 * them for identification, display, compatibility checks and filtering.
 *
 * Example usage:
 * ```kotlin
 * val authors = arrayOf("alice", "bob")
 *
 * @PluginMetadata(
 *     id = "com.example.myplugin",
 *     name = "My Plugin",
 *     version = 2,
 *     versionName = "2.0.0",
 *     description = "Adds a custom streaming source",
 *     authors = authors,
 *     repositoryUrl = "https://github.com/example/myplugin",
 *     iconUrl = "https://example.com/icon.png",
 *     language = "en",
 *     contentTypes = arrayOf("video", "audio"),
 *     requiresAppVersion = 3,
 *     isAdult = false
 * )
 * class MyPlugin : Plugin { /* ... */ }
 * ```
 *
 * Notes:
 * - [id] should be unique across plugins; reverse-domain notation is recommended.
 * - [version] is an integer for programmatic compatibility checks. Bump it for
 *   incompatible changes. [versionName] is a human-facing string for display.
 * - [requiresAppVersion] expresses the minimum host app version required.
 *
 * @property id Unique identifier for the plugin (reverse domain recommended).
 * @property name Human-readable plugin name shown in the UI.
 * @property version Numeric plugin version used for compatibility checks.
 * @property versionName Human-readable version string for release/display.
 * @property description Short, user-facing description of plugin functionality.
 * @property authors Array of author names, handles, or maintainers. Default empty.
 * @property repositoryUrl Optional URL to the plugin's source or issue tracker.
 * @property iconUrl Optional URL to an icon used to represent the plugin.
 * @property language Primary language code used by the plugin (ISO 639-1). Default "en".
 * @property contentTypes Array of content categories the plugin provides (e.g., [com.dreamstream.core.model.catalog.ContentType]). Default empty.
 * @property requiresAppVersion Minimum required host application version to load this plugin. Default 1.
 * @property isAdult True if the plugin exposes adult-restricted content; the host may filter these. Default false.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PluginMetadata(
    val id: String,
    val name: String,
    val version: Int,
    val versionName: String,
    val description: String,
    val authors: Array<String> = [],
    val repositoryUrl: String = "",
    val iconUrl: String = "",
    val language: String = "en",
    val contentTypes: Array<String> = [],
    val requiresAppVersion: Int = 1,
    val isAdult: Boolean = false,
)
