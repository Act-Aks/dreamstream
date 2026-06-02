package com.dreamstream.core.data.network

import com.dreamstream.core.domain.logger.DreamLogger
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.cookies.AcceptAllCookiesStorage
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger as KtorLogger

class DreamHttpClientFactory(private val logger: DreamLogger) {

    fun create(
        engine: HttpClientEngine,
        cookieStorage: CookiesStorage = AcceptAllCookiesStorage(),
        json: Json = DreamJson,
    ): HttpClient = HttpClient(engine) {

        // Cookie handling
        install(HttpCookies) {
            storage = cookieStorage
        }

        // JSON Content Negotiation
        install(ContentNegotiation) {
            json(json)
        }

        // Compression
        install(ContentEncoding) {
            gzip(quality = 1.0f)
            deflate(quality = 0.9f)
            identity()
        }

        // Timeouts
        install(HttpTimeout) {
            requestTimeoutMillis = DEFAULT_TIMEOUT_MS
            connectTimeoutMillis = CONNECT_TIMEOUT_MS
            socketTimeoutMillis = DEFAULT_TIMEOUT_MS
        }

        // Logging
        install(Logging) {
            // Bridge Ktor's Logger interface to DreamLogger so all HTTP traces
            // flow through Kermit on every platform instead of plain println.
            this.logger = object : KtorLogger {
                override fun log(message: String) =
                    this@DreamHttpClientFactory.logger.debug(message)
            }
            // HEADERS level: logs method, URL, status, and headers without
            // printing potentially large HTML or JSON bodies.
            level = LogLevel.HEADERS
            sanitizeHeader { header ->
                header == "Authorization" || header == "Cookie"
            }
        }

        // Default headers
        defaultRequest {
            headers.appendIfNameAbsent("User-Agent", DEFAULT_USER_AGENT)
            headers.appendIfNameAbsent("Accept-Language", DEFAULT_ACCEPT_LANGUAGE)
            headers.appendIfNameAbsent("Accept", DEFAULT_ACCEPT)
        }

        // Retry on transient failures
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 2)
            retryOnException(maxRetries = 2, retryOnTimeout = true)
            exponentialDelay(base = 2.0, maxDelayMs = 5_000L)
        }

        expectSuccess = false
    }

    companion object {

        private const val DEFAULT_USER_AGENT =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " + "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/120.0.0.0 Safari/537.36"
        private const val DEFAULT_ACCEPT =
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8"
        private const val DEFAULT_ACCEPT_LANGUAGE = "en-US,en;q=0.9"
        private const val DEFAULT_TIMEOUT_MS = 30_000L
        private const val CONNECT_TIMEOUT_MS = 15_000L
    }
}
