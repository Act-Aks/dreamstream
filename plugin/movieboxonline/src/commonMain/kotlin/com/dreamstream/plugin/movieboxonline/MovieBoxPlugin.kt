package com.dreamstream.plugin.movieboxonline

import com.dreamstream.plugin.api.plugin.DreamPlugin
import com.dreamstream.plugin.api.plugin.PluginMetadata
import com.dreamstream.plugin.api.provider.ContentProvider
import com.dreamstream.plugin.movieboxonline.provider.MovieBoxProvider

/**
 * DreamStream plugin for MovieBox Online — a streaming index providing movies and
 * TV series from https://movieboxonline.org.
 *
 * Registers a single [MovieBoxProvider] that scrapes server-rendered HTML pages
 * using Ksoup and extracts poster images from the embedded Nuxt SSR JSON payload.
 * Supports home page sections and keyword search. Stream link resolution
 * (`loadLinks`) is stubbed and will be implemented in a future release alongside
 * the player module.
 */
@PluginMetadata(
    id = MovieBoxConfig.PROVIDER_ID,
    name = "MovieBox Online",
    version = 1,
    versionName = "1.0.0",
    description = "Movies and TV series from MovieBox Online",
    authors = ["DreamStream Team"],
    repositoryUrl = "https://github.com/dreamstream-app/dreamstream",
    language = "en",
    contentTypes = ["movie", "tv"],
    requiresAppVersion = 1,
)
class MovieBoxPlugin : DreamPlugin() {
    override fun registerProviders(): List<ContentProvider> = listOf(MovieBoxProvider())
}
