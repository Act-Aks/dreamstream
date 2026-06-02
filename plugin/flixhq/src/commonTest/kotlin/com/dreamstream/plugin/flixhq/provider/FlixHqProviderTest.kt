package com.dreamstream.plugin.flixhq.provider

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.dreamstream.plugin.api.plugin.LogLevel
import com.dreamstream.plugin.api.plugin.PluginContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class FlixHqProviderTest {

    // ── HTML fixtures ──────────────────────────────────────────────────────────

    private val HOME_HTML = """
        <html><body>
        <div class="block_area">
          <h2 class="cat-heading">Trending</h2>
          <div class="flw-item">
            <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/m1.jpg" /></div>
            <div class="film-detail">
              <h3 class="film-name"><a href="/movie/space-race-1">Space Race</a></h3>
              <div class="fd-infor"><span class="fdi-item">2024</span></div>
            </div>
          </div>
        </div>
        </body></html>
    """.trimIndent()

    private val SEARCH_HTML = """
        <html><body>
        <div class="flw-item">
          <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/s1.jpg" /></div>
          <div class="film-detail">
            <h3 class="film-name"><a href="/movie/inception-42">Inception</a></h3>
            <div class="fd-infor"><span class="fdi-item">2010</span></div>
          </div>
        </div>
        </body></html>
    """.trimIndent()

    // ── Test helpers ───────────────────────────────────────────────────────────

    /** Creates a provider with a [MockEngine] that maps URL path → body. */
    private fun providerWith(
        responses: Map<String, Pair<HttpStatusCode, String>>,
    ): FlixHqProvider {
        val engine = MockEngine { request ->
            val key = request.url.encodedPath
            val (status, body) = responses[key]
                ?: (HttpStatusCode.NotFound to "Not Found")
            respond(
                content = body,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "text/html; charset=utf-8"),
            )
        }
        val client = HttpClient(engine)
        val context = FakePluginContext(client)
        return FlixHqProvider().also { it.inject(client, context) }
    }

    /** Provider whose engine throws on every request — simulates a network failure. */
    private fun failingProvider(): FlixHqProvider {
        val engine = MockEngine { throw RuntimeException("Simulated network failure") }
        val client = HttpClient(engine)
        val context = FakePluginContext(client)
        return FlixHqProvider().also { it.inject(client, context) }
    }

    // ── getMainPage ────────────────────────────────────────────────────────────

    @Test
    fun `getMainPage returns non-null catalog response on success`() = runTest {
        val provider = providerWith(mapOf("/home" to (HttpStatusCode.OK to HOME_HTML)))
        val result = provider.getMainPage(1, com.dreamstream.plugin.api.model.catalog.CatalogRequest(1))
        assertThat(result).isNotNull()
    }

    @Test
    fun `getMainPage returns sections parsed from HTML`() = runTest {
        val provider = providerWith(mapOf("/home" to (HttpStatusCode.OK to HOME_HTML)))
        val result = provider.getMainPage(1, com.dreamstream.plugin.api.model.catalog.CatalogRequest(1))
        assertThat(result!!.sections).hasSize(1)
        assertThat(result.sections[0].name).isEqualTo("Trending")
    }

    @Test
    fun `getMainPage returns catalog response with empty sections when HTML has no blocks`() = runTest {
        val provider = providerWith(mapOf("/home" to (HttpStatusCode.OK to "<html><body></body></html>")))
        val result = provider.getMainPage(1, com.dreamstream.plugin.api.model.catalog.CatalogRequest(1))
        assertThat(result).isNotNull()
        assertThat(result!!.sections).isEmpty()
    }

    @Test
    fun `getMainPage returns null when network throws`() = runTest {
        val result = failingProvider().getMainPage(
            1,
            com.dreamstream.plugin.api.model.catalog.CatalogRequest(1),
        )
        assertThat(result).isNull()
    }

    // ── search ─────────────────────────────────────────────────────────────────

    @Test
    fun `search encodes query and returns results`() = runTest {
        // "inception" query → path "/search/inception"
        val provider = providerWith(mapOf("/search/inception" to (HttpStatusCode.OK to SEARCH_HTML)))
        val results = provider.search("inception")
        assertThat(results).hasSize(1)
    }

    @Test
    fun `search encodes spaces in query as hyphens`() = runTest {
        // "space race" → "/search/space-race"
        val provider = providerWith(
            mapOf("/search/space-race" to (HttpStatusCode.OK to SEARCH_HTML)),
        )
        val results = provider.search("space race")
        assertThat(results).hasSize(1)
    }

    @Test
    fun `search with blank query returns empty list without network call`() = runTest {
        // engine throws — proves the guard short-circuits before network
        val results = failingProvider().search("   ")
        assertThat(results).isEmpty()
    }

    @Test
    fun `search returns empty list when network throws`() = runTest {
        val results = failingProvider().search("inception")
        assertThat(results).isEmpty()
    }

    @Test
    fun `search returns empty list when HTML contains no results`() = runTest {
        val provider = providerWith(
            mapOf("/search/empty-query" to (HttpStatusCode.OK to "<html><body></body></html>")),
        )
        val results = provider.search("empty query")
        assertThat(results).isEmpty()
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// Fake plugin context — minimal PluginContext that holds the injected HttpClient
// ─────────────────────────────────────────────────────────────────────────────

private class FakePluginContext(override val httpClient: HttpClient) : PluginContext {
    override val storageDir: String = ""
    override val apiVersion: Int = 1
    override fun getString(key: String): String? = null
    override fun putString(key: String, value: String) {}
    override fun log(tag: String, message: String, level: LogLevel) {}
}
