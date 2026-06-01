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
        CONFLICT,
        FORBIDDEN,
        NOT_FOUND,
        NO_INTERNET,
        PAYLOAD_TOO_LARGE,
        REQUEST_TIMEOUT,
        SERIALIZATION,
        SERVER_ERROR,
        SERVICE_UNAVAILABLE,
        TOO_MANY_REQUESTS,
        UNAUTHORIZED,
        UNKNOWN,
    }

    /** Errors that originate from local storage (DB, disk, preferences). */
    enum class Local : DataError {
        CORRUPTED,
        DISK_FULL,
        NOT_FOUND,
        PERMISSION_DENIED,
        UNKNOWN,
    }

    /** Errors that originate from plugin. */
    enum class Plugin : DataError {
        CLASS_NOT_FOUND,
        INCOMPATIBLE_VERSION,
        INVALID_MANIFEST,
        LOAD_FAILED,
        SIGNATURE_INVALID,
        UNKNOWN
    }
}
