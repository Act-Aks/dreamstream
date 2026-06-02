package com.dreamstream.core.runtime.registry

import com.dreamstream.plugin.api.provider.ContentProvider

/** Represents the lifecycle state of [PluginRegistry]. */
sealed interface RegistryState {
    /** Plugins are still being loaded and initialised. */
    data object Initializing : RegistryState

    /** All plugins have been initialised. [providers] may be empty if loading failed. */
    data class Ready(val providers: List<ContentProvider>) : RegistryState
}
