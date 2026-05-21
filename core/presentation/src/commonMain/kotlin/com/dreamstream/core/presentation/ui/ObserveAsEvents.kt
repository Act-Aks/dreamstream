package com.dreamstream.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

/**
 * Collects one-time events from a [Flow] in a lifecycle-aware fashion so
 * navigation/snackbar/toast side-effects are not redelivered after
 * configuration changes or while the host is paused.
 *
 * Re-keyed on [events] and the current [LocalLifecycleOwner] so callers can
 * safely swap the source flow without leaking the previous collection.
 *
 * Uses [repeatOnLifecycle] with [Lifecycle.State.STARTED] so collection is
 * paused while the screen is not visible and resumed when it returns.
 */
@Composable
fun <T> ObserveAsEvents(
    events: Flow<T>,
    onEvent: (T) -> Unit,
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(events, lifecycleOwner.lifecycle) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(coroutineContext) {
                events.collect(onEvent)
            }
        }
    }
}
