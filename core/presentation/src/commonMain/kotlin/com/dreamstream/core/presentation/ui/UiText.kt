package com.dreamstream.core.presentation.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Wrapper around a user-facing string that may originate either from a
 * Compose Multiplatform string resource or from a dynamically built value.
 *
 * Use [UiText] for any string that *could* be localized or come from a
 * resource bundle - typically error messages, button labels emitted from a
 * ViewModel, etc. Use plain [String] for values that are inherently dynamic
 * and never localized (a username, a formatted date, a currency amount).
 */
sealed interface UiText {

    /** A literal string built at runtime (already localized or non-translatable). */
    data class DynamicString(val value: String) : UiText

    /**
     * A Compose Multiplatform string resource with optional format args.
     *
     * [args] are forwarded verbatim to [stringResource] at resolution time.
     */
    data class StringResourceId(
        val resource: StringResource,
        val args: List<Any> = emptyList(),
    ) : UiText

    /**
     * Resolve this [UiText] to a concrete [String] within a Compose scope.
     */
    @Composable
    fun asString(): String = when (this) {
        is DynamicString -> value
        is StringResourceId -> stringResource(resource, *args.toTypedArray())
    }
}
