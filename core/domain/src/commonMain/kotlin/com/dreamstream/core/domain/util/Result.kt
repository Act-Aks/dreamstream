package com.dreamstream.core.domain.util

/**
 * Generic typed result wrapper used across every layer of the app.
 *
 * Prefer [Result] over thrown exceptions for *expected* failures (network
 * errors, validation failures, missing local data, etc). Reserve exceptions
 * for programmer errors or truly exceptional cases that should crash the
 * caller.
 *
 * @param D the success payload type.
 * @param E the typed error type, constrained to [Error] so the failure surface
 * stays explicit and exhaustive.
 */
sealed interface Result<out D, out E : Error> {

    /** Successful outcome carrying [data]. */
    data class Success<out D>(val data: D) : Result<D, Nothing>

    /** Failure outcome carrying a typed [error]. */
    data class Error<out E : com.dreamstream.core.domain.util.Error>(
        val error: E,
    ) : Result<Nothing, E>
}

/**
 * Convenience alias for results that only signal success/failure without a
 * payload (e.g. validation, fire-and-forget commands).
 */
typealias EmptyResult<E> = Result<Unit, E>
