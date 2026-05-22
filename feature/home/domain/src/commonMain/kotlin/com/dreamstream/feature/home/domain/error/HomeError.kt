package com.dreamstream.feature.home.domain.error

import com.dreamstream.core.domain.util.Error

/** Typed errors that can occur when loading the home feed. */
sealed interface HomeError : Error {
    /** The source returned no content — e.g. empty catalogue. */
    data object NoContentAvailable : HomeError

    /** A transient or network-level failure prevented loading. */
    data object LoadFailed : HomeError
}
