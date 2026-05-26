package com.dreamstream.core.presentation.ui

import com.dreamstream.core.domain.util.DataError
import com.dreamstream.core.presentation.resources.Res
import com.dreamstream.core.presentation.resources.error_bad_request
import com.dreamstream.core.presentation.resources.error_conflict
import com.dreamstream.core.presentation.resources.error_disk_full
import com.dreamstream.core.presentation.resources.error_forbidden
import com.dreamstream.core.presentation.resources.error_local_not_found
import com.dreamstream.core.presentation.resources.error_local_unknown
import com.dreamstream.core.presentation.resources.error_network_unknown
import com.dreamstream.core.presentation.resources.error_no_internet
import com.dreamstream.core.presentation.resources.error_not_found
import com.dreamstream.core.presentation.resources.error_payload_too_large
import com.dreamstream.core.presentation.resources.error_request_timeout
import com.dreamstream.core.presentation.resources.error_serialization
import com.dreamstream.core.presentation.resources.error_server_error
import com.dreamstream.core.presentation.resources.error_service_unavailable
import com.dreamstream.core.presentation.resources.error_too_many_requests
import com.dreamstream.core.presentation.resources.error_unauthorized

/**
 * Maps a [DataError] to a user-facing [UiText] backed by a localized string resource.
 *
 * All branches use [UiText.StringResourceId] so the message is resolved through
 * Compose Multiplatform's resource system and automatically adapts to the active
 * app locale — including in-app language changes via the Settings screen.
 *
 * Keep this mapping exhaustive: when a new [DataError.Network] or
 * [DataError.Local] entry is added the compiler will flag the missing branch here.
 */
fun DataError.toUiText(): UiText = when (this) {
    DataError.Network.BAD_REQUEST -> UiText.StringResourceId(Res.string.error_bad_request)
    DataError.Network.REQUEST_TIMEOUT -> UiText.StringResourceId(Res.string.error_request_timeout)
    DataError.Network.UNAUTHORIZED -> UiText.StringResourceId(Res.string.error_unauthorized)
    DataError.Network.FORBIDDEN -> UiText.StringResourceId(Res.string.error_forbidden)
    DataError.Network.NOT_FOUND -> UiText.StringResourceId(Res.string.error_not_found)
    DataError.Network.CONFLICT -> UiText.StringResourceId(Res.string.error_conflict)
    DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResourceId(Res.string.error_too_many_requests)
    DataError.Network.NO_INTERNET -> UiText.StringResourceId(Res.string.error_no_internet)
    DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResourceId(Res.string.error_payload_too_large)
    DataError.Network.SERVER_ERROR -> UiText.StringResourceId(Res.string.error_server_error)
    DataError.Network.SERVICE_UNAVAILABLE -> UiText.StringResourceId(Res.string.error_service_unavailable)
    DataError.Network.SERIALIZATION -> UiText.StringResourceId(Res.string.error_serialization)
    DataError.Network.UNKNOWN -> UiText.StringResourceId(Res.string.error_network_unknown)
    DataError.Local.DISK_FULL -> UiText.StringResourceId(Res.string.error_disk_full)
    DataError.Local.NOT_FOUND -> UiText.StringResourceId(Res.string.error_local_not_found)
    DataError.Local.UNKNOWN -> UiText.StringResourceId(Res.string.error_local_unknown)
}
