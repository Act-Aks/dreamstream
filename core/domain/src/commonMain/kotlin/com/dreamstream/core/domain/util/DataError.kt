package com.dreamstream.core.domain.util

/**
 * Shared data-layer error taxonomy.
 *
 * Network and Local errors are deliberately split: a repository that fans out
 * to several data sources can still expose `Result<T, DataError>` thanks to
 * the common supertype, while individual data sources keep their specific
 * subtypes.
 *
 * Source/parser specific failures (e.g. provider HTML changed, captcha
 * detected, stream not resolvable) should live closer to the feature/source
 * module that owns them rather than being added here.
 */
sealed interface DataError : Error {

    /** Errors that originate from network I/O or HTTP responses. */
    enum class Network : DataError {
        BAD_REQUEST,
        REQUEST_TIMEOUT,
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        CONFLICT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        SERVER_ERROR,
        SERVICE_UNAVAILABLE,
        SERIALIZATION,
        UNKNOWN,
    }

    /** Errors that originate from local storage (DB, disk, preferences). */
    enum class Local : DataError {
        DISK_FULL,
        NOT_FOUND,
        UNKNOWN,
    }
}
