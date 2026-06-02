package com.dreamstream.core.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.isSuccess

suspend inline fun <reified T> HttpResponse.bodyOrNull(): T? = runCatching {
    if (status.isSuccess()) body<T>() else null
}.getOrNull()

fun HttpResponse.isSuccessful(): Boolean = status.isSuccess()

fun Map<String, String>.toHeaders(): Headers = Headers.build {
    this@toHeaders.forEach { (key, value) -> append(key, value) }
}

// URL Builder helpers
fun buildUrl(base: String, vararg path: String): String = buildString {
    append(base.trimEnd('/'))
    path.forEach { segment ->
        append('/')
        append(segment.trimStart('/'))
    }
}

fun String.addQueryParams(params: Map<String, String>): String {
    if (params.isEmpty()) return this
    val separator = if (contains('?')) '&' else '?'
    return "$this$separator${params.entries.joinToString("&") { "${it.key}=${it.value}" }}"
}
