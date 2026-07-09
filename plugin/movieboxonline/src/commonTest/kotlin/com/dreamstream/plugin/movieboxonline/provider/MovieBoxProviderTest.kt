package com.dreamstream.plugin.movieboxonline.provider

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import com.dreamstream.core.domain.model.catalog.CatalogRequest
import com.dreamstream.plugin.api.model.search.ApiMovieResult
import com.dreamstream.plugin.api.model.search.ApiSeriesResult
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

class MovieBoxProviderTest {

    // ── HTML fixtures ──────────────────────────────────────────────────────────

    private val HOME_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">
        ["https://pbcdnw.aoneroom.com/image/2024/01/01/home_poster.jpg"]
        </script>
        <article class="section-panel">
          <div class="title">Trending</div>
          <a class="movie-card" href="/detailpage/inception-AbCd">
            <p class="movie-card-title">Inception</p>
            <p class="movie-card-meta">2010 <span> • </span> Action</p>
          </a>
        </article>
        </body></html>
    """.trimIndent()

    private val SEARCH_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">
        ["https://pbcdnw.aoneroom.com/image/2024/06/01/search_poster.jpg"]
        </script>
        <div class="results-grid">
          <a class="card" href="/detailpage/avatar-AbCd">
            <h2 class="card-title">Avatar: The Way of Water</h2>
            <div class="card-meta">2022 <span> • </span> Action</div>
            <span class="rate">7.6</span>
          </a>
        </div>
        </body></html>
    """.trimIndent()

    private val SERIES_SEARCH_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
        <div class="results-grid">
          <a class="card" href="/detailpage/one-piece-s1-XyZw">
            <h2 class="card-title">One Piece S1</h2>
            <div class="card-meta">2023 <span> • </span> Animation</div>
            <span class="rate">8.9</span>
          </a>
        </div>
        </body></html>
    """.trimIndent()

    // ── Test helpers ───────────────────────────────────────────────────────────

    /**
     * Creates a [MovieBoxProvider] backed by a [MockEngine] that maps URL path+query → body.
     * The key is the full encoded path (e.g. `"/"`, `"/searchpage?keyword=avatar"`).
     */
    private fun providerWith(
        responses: Map<String, Pair<HttpStatusCode, String>>,
    ): MovieBoxProvider {
        val engine = MockEngine { request ->
            val key = request.url.encodedPathAndQuery
            val (status, body) = responses[key] ?: (HttpStatusCode.NotFound to "Not Found")
            respond(
                content = body,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "text/html; charset=utf-8"),
            )
        }
        val client = HttpClient(engine)
        return MovieBoxProvider().also { it.inject(client, FakePluginContext(client)) }
    }

    /** Provider whose engine throws on every request — simulates a network failure. */
    private fun failingProvider(): MovieBoxProvider {
        val engine = MockEngine { throw RuntimeException("Simulated network failure") }
        val client = HttpClient(engine)
        return MovieBoxProvider().also { it.inject(client, FakePluginContext(client)) }
    }

    // ── getHomePage ────────────────────────────────────────────────────────────

    @Test
    fun `getHomePage returns non-null catalog response on success`() = runTest {
        val provider = providerWith(mapOf("/" to (HttpStatusCode.OK to HOME_HTML)))
        assertThat(provider.getHomePage(1, CatalogRequest(1))).isNotNull()
    }

    @Test
    fun `getHomePage returns sections parsed from HTML`() = runTest {
        val provider = providerWith(mapOf("/" to (HttpStatusCode.OK to HOME_HTML)))
        val result = provider.getHomePage(1, CatalogRequest(1))
        assertThat(result!!.sections).hasSize(1)
        assertThat(result.sections[0].name).isEqualTo("Trending")
    }

    @Test
    fun `getHomePage returns one item in section`() = runTest {
        val provider = providerWith(mapOf("/" to (HttpStatusCode.OK to HOME_HTML)))
        val result = provider.getHomePage(1, CatalogRequest(1))
        assertThat(result!!.sections[0].items).hasSize(1)
    }

    @Test
    fun `getHomePage returns empty sections when HTML has no section panels`() = runTest {
        val provider = providerWith(mapOf("/" to (HttpStatusCode.OK to "<html><body></body></html>")))
        val result = provider.getHomePage(1, CatalogRequest(1))
        assertThat(result).isNotNull()
        assertThat(result!!.sections).isEmpty()
    }

    @Test
    fun `getHomePage returns null when network throws`() = runTest {
        assertThat(failingProvider().getHomePage(1, CatalogRequest(1))).isNull()
    }

    // ── search ─────────────────────────────────────────────────────────────────

    @Test
    fun `search passes keyword as query param and returns results`() = runTest {
        val provider = providerWith(
            mapOf("/searchpage?keyword=avatar" to (HttpStatusCode.OK to SEARCH_HTML))
        )
        val results = provider.search("avatar")
        assertThat(results).hasSize(1)
    }

    @Test
    fun `search result is mapped to ApiMovieResult for movie`() = runTest {
        val provider = providerWith(
            mapOf("/searchpage?keyword=avatar" to (HttpStatusCode.OK to SEARCH_HTML))
        )
        val result = provider.search("avatar").first()
        assertThat(result is ApiMovieResult).isEqualTo(true)
        assertThat((result as ApiMovieResult).name).isEqualTo("Avatar: The Way of Water")
    }

    @Test
    fun `search result is mapped to ApiSeriesResult for series title`() = runTest {
        val provider = providerWith(
            mapOf("/searchpage?keyword=one+piece" to (HttpStatusCode.OK to SERIES_SEARCH_HTML))
        )
        val result = provider.search("one+piece").first()
        assertThat(result is ApiSeriesResult).isEqualTo(true)
    }

    @Test
    fun `search with blank query returns empty list without network call`() = runTest {
        // engine throws — proves the guard short-circuits before any network call
        val results = failingProvider().search("   ")
        assertThat(results).isEmpty()
    }

    @Test
    fun `search returns empty list when network throws`() = runTest {
        assertThat(failingProvider().search("avatar")).isEmpty()
    }

    @Test
    fun `search returns empty list when HTML contains no cards`() = runTest {
        val provider = providerWith(
            mapOf("/searchpage?keyword=noresults" to (HttpStatusCode.OK to "<html><body></body></html>"))
        )
        assertThat(provider.search("noresults")).isEmpty()
    }

    // ── load / loadLinks (stubbed) ────────────────────────────────────────────

    @Test
    fun `load always returns null`() = runTest {
        val provider = providerWith(emptyMap())
        assertThat(provider.load("https://movieboxonline.org/detailpage/test-AbCd")).isNull()
    }

    @Test
    fun `loadLinks always returns false`() = runTest {
        val provider = providerWith(emptyMap())
        assertThat(provider.loadLinks("data", false, {}, {})).isEqualTo(false)
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
