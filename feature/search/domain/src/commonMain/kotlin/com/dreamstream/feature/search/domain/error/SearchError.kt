package com.dreamstream.feature.search.domain.error

import com.dreamstream.core.domain.util.Error

/** Typed errors that can occur when executing a search. */
sealed interface SearchError : Error {
    /** A transient or source-level failure prevented the search from completing. */
    data object SearchFailed : SearchError
}
