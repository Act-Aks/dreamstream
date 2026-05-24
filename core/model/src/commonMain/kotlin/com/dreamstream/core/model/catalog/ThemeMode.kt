package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/** App-wide theme preference. */
@Serializable
enum class ThemeMode {
    Light,
    Dark,
    System,
}
