package com.dreamstream.core.domain.util

/**
 * Transform the success payload of this [Result] without touching the error
 * branch. Errors are propagated unchanged.
 */
inline fun <T, E : Error, R> Result<T, E>.map(
    transform: (T) -> R,
): Result<R, E> = when (this) {
    is Result.Error -> Result.Error(error)
    is Result.Success -> Result.Success(transform(data))
}

/**
 * Run [action] for its side effects when this result is a [Result.Success].
 * Returns `this` so calls can be chained fluently.
 */
inline fun <T, E : Error> Result<T, E>.onSuccess(
    action: (T) -> Unit,
): Result<T, E> = apply {
    if (this is Result.Success) action(data)
}

/**
 * Run [action] for its side effects when this result is a [Result.Error].
 * Returns `this` so calls can be chained fluently.
 */
inline fun <T, E : Error> Result<T, E>.onFailure(
    action: (E) -> Unit,
): Result<T, E> = apply {
    if (this is Result.Error) action(error)
}

/**
 * Drop the success payload, preserving only success/failure semantics.
 * Useful for commands whose caller only cares whether the operation worked.
 */
fun <T, E : Error> Result<T, E>.asEmptyResult(): EmptyResult<E> = map { }
