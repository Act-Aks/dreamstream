package com.dreamstream.plugin.api.plugin

import io.ktor.client.HttpClient

/**
 * Context provided to plugins when they are loaded.
 * Gives plugins access to host-provided services without
 * depending on the full app internals.
 */
interface PluginContext {
    /** Configured HTTP client with cookie handling and Cloudflare bypass */
    val httpClient: HttpClient

    /** Plugin-specific storage directory path */
    val storageDir: String

    /** Current app API version */
    val apiVersion: Int

    /** Get a stored preference value */
    fun getString(key: String): String?

    /** Store a preference value */
    fun putString(key: String, value: String)

    /** App-level logger */
    fun log(tag: String, message: String, level: LogLevel = LogLevel.DEBUG)
}

enum class LogLevel { VERBOSE, DEBUG, INFO, WARNING, ERROR }
