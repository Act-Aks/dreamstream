package com.dreamstream.core.presentation.ui

import com.dreamstream.core.domain.util.DataError

/**
 * Maps a [DataError] to a user-facing [UiText].
 *
 * The current mapping uses [UiText.DynamicString] placeholders so this module
 * does not yet require a Compose resources bundle. Once shared
 * `composeResources/` strings are introduced (e.g. `error_no_internet`,
 * `error_server`, ...) migrate each branch to
 * `UiText.StringResourceId(Res.string.<key>)`.
 *
 * Keep this mapping exhaustive: when a new [DataError.Network] or
 * [DataError.Local] entry is added the compiler will flag the missing branch
 * here.
 */
fun DataError.toUiText(): UiText = when (this) {
    DataError.Network.BAD_REQUEST -> UiText.DynamicString("Bad request.")
    DataError.Network.REQUEST_TIMEOUT -> UiText.DynamicString("Request timed out. Try again.")
    DataError.Network.UNAUTHORIZED -> UiText.DynamicString("You are not authorized.")
    DataError.Network.FORBIDDEN -> UiText.DynamicString("Access is forbidden.")
    DataError.Network.NOT_FOUND -> UiText.DynamicString("Resource not found.")
    DataError.Network.CONFLICT -> UiText.DynamicString("Conflicting state. Try again.")
    DataError.Network.TOO_MANY_REQUESTS -> UiText.DynamicString("Too many requests. Slow down.")
    DataError.Network.NO_INTERNET -> UiText.DynamicString("No internet connection.")
    DataError.Network.PAYLOAD_TOO_LARGE -> UiText.DynamicString("Payload too large.")
    DataError.Network.SERVER_ERROR -> UiText.DynamicString("Server error. Try again later.")
    DataError.Network.SERVICE_UNAVAILABLE -> UiText.DynamicString("Service is unavailable.")
    DataError.Network.SERIALIZATION -> UiText.DynamicString("Failed to parse the response.")
    DataError.Network.UNKNOWN -> UiText.DynamicString("Unknown network error.")
    DataError.Local.DISK_FULL -> UiText.DynamicString("Storage is full.")
    DataError.Local.NOT_FOUND -> UiText.DynamicString("Not found locally.")
    DataError.Local.UNKNOWN -> UiText.DynamicString("Unknown local error.")
}
