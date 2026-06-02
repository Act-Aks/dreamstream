package com.dreamstream.plugin.flixhq

import com.dreamstream.plugin.api.plugin.DreamPlugin
import com.dreamstream.plugin.api.plugin.PluginMetadata
import com.dreamstream.plugin.api.provider.ContentProvider
import com.dreamstream.plugin.flixhq.provider.FlixHqProvider

/**
 * DreamStream plugin for FlixHQ — a public streaming index providing movies and TV shows.
 *
 * Registers a single [FlixHqProvider] that scrapes FlixHQ's HTML pages using Ksoup.
 * Supports home page sections and keyword search. Stream link resolution (`loadLinks`)
 * is stubbed and will be implemented in a future release alongside the player module.
 */
@PluginMetadata(
    id = FlixHqConfig.PROVIDER_ID,
    name = "FlixHQ",
    version = 1,
    versionName = "1.0.0",
    description = "Movies and TV shows from FlixHQ",
    authors = ["DreamStream Team"],
    repositoryUrl = "https://github.com/dreamstream-app/dreamstream",
    language = "en",
    contentTypes = ["movie", "tv"],
    requiresAppVersion = 1,
)
class FlixHqPlugin : DreamPlugin() {
    override fun registerProviders(): List<ContentProvider> = listOf(FlixHqProvider())
}
