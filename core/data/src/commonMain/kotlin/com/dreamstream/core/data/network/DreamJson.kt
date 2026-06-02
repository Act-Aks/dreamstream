package com.dreamstream.core.data.network

import kotlinx.serialization.json.Json

internal val DreamJson = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
    explicitNulls = false
    coerceInputValues = true
    allowStructuredMapKeys = true
    allowSpecialFloatingPointValues = true
    prettyPrint = false
}
