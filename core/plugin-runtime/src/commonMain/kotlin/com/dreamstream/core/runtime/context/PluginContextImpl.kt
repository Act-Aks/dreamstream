package com.dreamstream.core.runtime.context

import com.dreamstream.plugin.api.plugin.LogLevel
import com.dreamstream.plugin.api.plugin.PluginApiVersion
import com.dreamstream.plugin.api.plugin.PluginContext
import io.ktor.client.HttpClient

/**
 * Default [PluginContext] implementation used by the plugin host.
 *
 * Provides:
 * - The shared [HttpClient] for all provider HTTP calls.
 * - An in-memory key/value store for per-plugin preferences (DataStore integration is a future improvement).
 * - Simple println-based logging (Kermit integration is a future improvement).
 *
 * @param httpClient Shared Ktor [HttpClient] from Koin's `networkModule`.
 * @param storageDir Writable directory path for this plugin (empty string = no persistent storage).
 * @param apiVersion Host API version reported to plugins.
 */
class PluginContextImpl(
    override val httpClient: HttpClient,
    override val storageDir: String = "",
    override val apiVersion: Int = PluginApiVersion.CURRENT,
) : PluginContext {

    private val prefs = mutableMapOf<String, String>()

    override fun getString(key: String): String? = prefs[key]

    override fun putString(key: String, value: String) {
        prefs[key] = value
    }

    override fun log(tag: String, message: String, level: LogLevel) {
        println("[${level.name}] $tag: $message")
    }
}
