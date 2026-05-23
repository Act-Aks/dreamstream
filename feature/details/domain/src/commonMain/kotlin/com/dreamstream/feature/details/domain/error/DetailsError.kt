package com.dreamstream.feature.details.domain.error

import com.dreamstream.core.domain.util.Error

sealed interface DetailsError : Error {
    /** The requested content ID does not exist in any known source. */
    data object NotFound : DetailsError

    /** The content exists but could not be loaded (network failure, parse error, etc.). */
    data object LoadFailed : DetailsError
}
