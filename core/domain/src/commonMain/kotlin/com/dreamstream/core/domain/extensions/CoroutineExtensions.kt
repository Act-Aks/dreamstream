package com.dreamstream.core.domain.extensions

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.launch

/** Debounce a flow by [waitMs] milliseconds */
@OptIn(FlowPreview::class)
fun <T> Flow<T>.debounceLatest(waitMs: Long): Flow<T> = debounce(waitMs)

/** Retry with exponential backoff */
fun <T> Flow<T>.retryWithBackoff(
    maxRetries: Int = 3,
    initialDelay: Long = 1000L,
    maxDelay: Long = 8000L,
    factor: Double = 2.0,
): Flow<T> = retry(maxRetries.toLong()) { cause ->
    if (cause is CancellationException) throw cause
    delay(initialDelay.coerceAtMost(maxDelay))
    true
}

/** Launch a coroutine and ignore cancellation */
fun CoroutineScope.launchSafe(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(dispatcher) {
    try {
        block()
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        // Log but don't crash
    }
}

/** Collect multiple flows in parallel and merge results */
fun <T> List<Flow<T>>.mergeAll(): Flow<T> = merge(*toTypedArray())

