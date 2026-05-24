package com.dreamstream.core.model.plugin

import kotlinx.serialization.Serializable

@Serializable
enum class PluginStatus {
    Available,
    Installed,
    UpdateAvailable,
    Incompatible,
    Broken,
}
