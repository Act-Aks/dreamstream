package com.dreamstream.core.domain.extensions

import com.dreamstream.core.domain.util.Error
import com.dreamstream.core.domain.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Converts a [Flow] into a [Result] flow.
 *
 * - Emits [Result.Success] for each value emitted by the flow
 * - Emits [Result.Error] on exception (wrapped as [E])
 *
 * @note Loading state is not included. Handle loading separately via [onStart] in your UI.
 */
fun <T, E : Error> Flow<T>.asResult(): Flow<Result<T, E>> = this
    .map { Result.Success(it) }
    .catch { e -> emit(Result.Error(e as E)) }

/**
 * Executes [action] when a [Result.Success] is emitted, preserving the original flow.
 *
 * Useful for side effects like navigation, showing toasts, or logging.
 */
fun <T, E : Error> Flow<Result<T, E>>.onSuccess(
    action: suspend (T) -> Unit,
): Flow<Result<T, E>> = map { result ->
    if (result is Result.Success) action(result.data)
    result
}

/**
 * Executes [action] when a [Result.Error] is emitted, preserving the original flow.
 *
 * Useful for side effects like showing error messages or logging.
 */
fun <T, E : Error> Flow<Result<T, E>>.onFailure(
    action: suspend (E) -> Unit,
): Flow<Result<T, E>> = map { result ->
    if (result is Result.Error) action(result.error)
    result
}

/**
 * Transforms the success data using [transform], preserving error states.
 *
 * Equivalent to calling [Result.map] on each emitted result.
 */
fun <T, R, E : Error> Flow<Result<T, E>>.mapResult(
    transform: suspend (T) -> R,
): Flow<Result<R, E>> = map { result ->
    when (result) {
        is Result.Success -> Result.Success(transform(result.data))
        is Result.Error -> result
    }
}

/**
 * Converts a [Flow] into a [Result] flow with explicit loading state.
 *
 * - Emits loading indicator at the start (via [onStart])
 * - Emits [Result.Success] for each value
 * - Emits [Result.Error] on exception
 *
 * @param loadingIndicator A value to emit at the start to signal loading state.
 *        Typically used with UI state wrappers.
 */
fun <T, E : Error> Flow<T>.asResultWithLoading(
    loadingIndicator: Flow<Unit> = flowOf(Unit),
): Flow<Result<T, E>> = this
    .asResult()
    .onStart { loadingIndicator.collect() }
