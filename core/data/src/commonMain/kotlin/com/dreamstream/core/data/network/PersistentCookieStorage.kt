package com.dreamstream.core.data.network

import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Thread-safe in-memory cookie storage with optional persistence.
 * Respects cookie expiry and domain matching.
 */
class PersistentCookieStorage : CookiesStorage {

    private val storage = mutableMapOf<String, MutableMap<String, Cookie>>()
    private val mutex = Mutex()

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        val now = getTimeMillis()
        val result = mutableListOf<Cookie>()

        storage.forEach { (domain, cookies) ->
            if (requestUrl.host.endsWith(domain) || requestUrl.host == domain) {
                cookies.values.filter { cookie ->
                    val notExpired = cookie.expires?.timestamp?.let { it > now } ?: true
                    val pathMatches = requestUrl.encodedPath.startsWith(cookie.path ?: "/")
                    val secureOk = !cookie.secure || requestUrl.protocol == URLProtocol.HTTPS
                    notExpired && pathMatches && secureOk
                }.forEach { result.add(it) }
            }
        }
        result
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie): Unit = mutex.withLock {
        val domain = cookie.domain?.removePrefix(".") ?: requestUrl.host
        val domainCookies = storage.getOrPut(domain) { mutableMapOf() }

        // Check if expired
        val now = getTimeMillis()
        val isExpired = cookie.expires?.timestamp?.let { it <= now } ?: false

        if (isExpired) {
            domainCookies.remove(cookie.name)
        } else {
            domainCookies[cookie.name] = cookie
        }
    }

    override fun close() {
        storage.clear()
    }

    fun clearAll() {
        storage.clear()
    }

    fun clearDomain(domain: String) {
        storage.remove(domain)
    }

    fun getCookieCount(): Int = storage.values.sumOf { it.size }
}
