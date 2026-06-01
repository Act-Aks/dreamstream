package com.dreamstream.core.presentation.ui

import com.dreamstream.core.domain.util.DataError
import com.dreamstream.core.presentation.resources.CoreRes
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
import com.dreamstream.core.presentation.resources.error_plugin_class_not_found
import com.dreamstream.core.presentation.resources.error_plugin_corrupted
import com.dreamstream.core.presentation.resources.error_plugin_incompatible_version
import com.dreamstream.core.presentation.resources.error_plugin_invalid_manifest
import com.dreamstream.core.presentation.resources.error_plugin_load_failed
import com.dreamstream.core.presentation.resources.error_plugin_permission_denied
import com.dreamstream.core.presentation.resources.error_plugin_signature_invalid
import com.dreamstream.core.presentation.resources.error_plugin_unknown
import com.dreamstream.core.presentation.resources.error_request_timeout
import com.dreamstream.core.presentation.resources.error_serialization
import com.dreamstream.core.presentation.resources.error_server_error
import com.dreamstream.core.presentation.resources.error_service_unavailable
import com.dreamstream.core.presentation.resources.error_too_many_requests
import com.dreamstream.core.presentation.resources.error_unauthorized
import com.dreamstream.core.presentation.ui.UiText.StringResourceId

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
    DataError.Network.BAD_REQUEST -> StringResourceId(CoreRes.string.error_bad_request)
    DataError.Network.CONFLICT -> StringResourceId(CoreRes.string.error_conflict)
    DataError.Network.FORBIDDEN -> StringResourceId(CoreRes.string.error_forbidden)
    DataError.Network.NOT_FOUND -> StringResourceId(CoreRes.string.error_not_found)
    DataError.Network.NO_INTERNET -> StringResourceId(CoreRes.string.error_no_internet)
    DataError.Network.PAYLOAD_TOO_LARGE -> StringResourceId(CoreRes.string.error_payload_too_large)
    DataError.Network.REQUEST_TIMEOUT -> StringResourceId(CoreRes.string.error_request_timeout)
    DataError.Network.SERIALIZATION -> StringResourceId(CoreRes.string.error_serialization)
    DataError.Network.SERVER_ERROR -> StringResourceId(CoreRes.string.error_server_error)
    DataError.Network.SERVICE_UNAVAILABLE -> StringResourceId(CoreRes.string.error_service_unavailable)
    DataError.Network.TOO_MANY_REQUESTS -> StringResourceId(CoreRes.string.error_too_many_requests)
    DataError.Network.UNAUTHORIZED -> StringResourceId(CoreRes.string.error_unauthorized)
    DataError.Network.UNKNOWN -> StringResourceId(CoreRes.string.error_network_unknown)

    DataError.Local.DISK_FULL -> StringResourceId(CoreRes.string.error_disk_full)
    DataError.Local.NOT_FOUND -> StringResourceId(CoreRes.string.error_local_not_found)
    DataError.Local.UNKNOWN -> StringResourceId(CoreRes.string.error_local_unknown)

    DataError.Local.CORRUPTED -> StringResourceId(CoreRes.string.error_plugin_corrupted)
    DataError.Local.PERMISSION_DENIED -> StringResourceId(CoreRes.string.error_plugin_permission_denied)
    DataError.Plugin.CLASS_NOT_FOUND -> StringResourceId(CoreRes.string.error_plugin_class_not_found)
    DataError.Plugin.INCOMPATIBLE_VERSION -> StringResourceId(CoreRes.string.error_plugin_incompatible_version)
    DataError.Plugin.INVALID_MANIFEST -> StringResourceId(CoreRes.string.error_plugin_invalid_manifest)
    DataError.Plugin.LOAD_FAILED -> StringResourceId(CoreRes.string.error_plugin_load_failed)
    DataError.Plugin.SIGNATURE_INVALID -> StringResourceId(CoreRes.string.error_plugin_signature_invalid)
    DataError.Plugin.UNKNOWN -> StringResourceId(CoreRes.string.error_plugin_unknown)
}
