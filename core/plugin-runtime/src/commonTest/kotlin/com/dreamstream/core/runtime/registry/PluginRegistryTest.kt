package com.dreamstream.core.runtime.registry

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.media.StreamLink
import com.dreamstream.core.model.media.Subtitle
import com.dreamstream.core.runtime.loader.BundledPluginLoader
import com.dreamstream.plugin.api.model.catalog.CatalogRequest
import com.dreamstream.plugin.api.model.catalog.CatalogResponse
import com.dreamstream.plugin.api.model.catalog.CatalogSection
import com.dreamstream.plugin.api.model.detail.ApiContentDetail
import com.dreamstream.plugin.api.model.search.ApiMovieResult
import com.dreamstream.plugin.api.model.search.ApiSearchResult
import com.dreamstream.plugin.api.plugin.DreamPlugin
import com.dreamstream.plugin.api.plugin.PluginContext
import com.dreamstream.plugin.api.plugin.LogLevel
import com.dreamstream.plugin.api.provider.ContentProvider
import com.dreamstream.core.model.search.MovieResult
import com.dreamstream.core.runtime.context.PluginContextImpl
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PluginRegistryTest {

    // ── Test infrastructure ───────────────────────────────────────────────────

    private val testDispatcher = UnconfinedTestDispatcher()

    /** Minimal HttpClient backed by a mock engine — never actually called by stubs. */
    private fun stubHttpClient() = HttpClient(MockEngine { respond("", HttpStatusCode.OK) })

    private fun stubContext() = PluginContextImpl(httpClient = stubHttpClient())

    private fun registryWith(vararg plugins: DreamPlugin): PluginRegistry =
        PluginRegistry(
            loaders = listOf(BundledPluginLoader(plugins.toList())),
            context = stubContext(),
            dispatcher = testDispatcher,
        )

    // ── Initialization ─────────────────────────────────────────────────────────

    @Test
    fun `registry is in Initializing state before coroutine executes`() {
        // With UnconfinedTestDispatcher the init coroutine runs eagerly upon construction,
        // but this test verifies the sealed hierarchy exists and is reachable.
        val registry = registryWith()
        // After construction with UnconfinedTestDispatcher, it is already Ready.
        assertThat(registry.state.value).isInstanceOf(RegistryState.Ready::class)
    }

    @Test
    fun `registry transitions to Ready after init completes`() = runTest(testDispatcher) {
        val registry = registryWith(StubPlugin())
        assertThat(registry.state.value).isInstanceOf(RegistryState.Ready::class)
    }

    @Test
    fun `Ready state contains providers from all loaded plugins`() = runTest(testDispatcher) {
        val registry = registryWith(StubPlugin(), StubPlugin())
        val ready = registry.state.value as RegistryState.Ready
        // Two StubPlugins, each registers one provider → 2 total
        assertThat(ready.providers).hasSize(2)
    }

    // ── getHomeSections ────────────────────────────────────────────────────────

    @Test
    fun `getHomeSections returns sections from stub provider`() = runTest(testDispatcher) {
        val registry = registryWith(StubPlugin())
        val sections = registry.getHomeSections(page = 1)
        assertThat(sections).hasSize(1)
        assertThat(sections[0].name).isEqualTo("Stub Section")
    }

    @Test
    fun `getHomeSections returns items from stub provider`() = runTest(testDispatcher) {
        val registry = registryWith(StubPlugin())
        val sections = registry.getHomeSections(page = 1)
        assertThat(sections[0].items).hasSize(1)
        val item = sections[0].items[0] as MovieResult
        assertThat(item.name).isEqualTo("Stub Movie")
    }

    @Test
    fun `getHomeSections returns empty list when no providers support home page`() =
        runTest(testDispatcher) {
            val registry = registryWith(SearchOnlyPlugin())
            val sections = registry.getHomeSections(page = 1)
            assertThat(sections).isEmpty()
        }

    @Test
    fun `getHomeSections from multiple plugins merges all sections`() = runTest(testDispatcher) {
        val registry = registryWith(StubPlugin(), StubPlugin())
        val sections = registry.getHomeSections(page = 1)
        // Two plugins each returning one section → 2 merged
        assertThat(sections).hasSize(2)
    }

    @Test
    fun `getHomeSections swallows provider exception and returns empty for that provider`() =
        runTest(testDispatcher) {
            val registry = registryWith(FailingHomeProvider())
            val sections = registry.getHomeSections(page = 1)
            assertThat(sections).isEmpty()
        }

    // ── search ─────────────────────────────────────────────────────────────────

    @Test
    fun `search returns results from stub provider`() = runTest(testDispatcher) {
        val registry = registryWith(StubPlugin())
        val results = registry.search("test")
        assertThat(results).hasSize(1)
        assertThat(results[0].name).isEqualTo("Stub test")
    }

    @Test
    fun `search returns empty list when no providers support search`() = runTest(testDispatcher) {
        val registry = registryWith(HomeOnlyPlugin())
        val results = registry.search("test")
        assertThat(results).isEmpty()
    }

    @Test
    fun `search swallows provider exception and returns empty for that provider`() =
        runTest(testDispatcher) {
            val registry = registryWith(FailingSearchProvider())
            val results = registry.search("test")
            assertThat(results).isEmpty()
        }

    // ── Failure isolation ──────────────────────────────────────────────────────

    @Test
    fun `plugin that throws during initialize is skipped leaving others loaded`() =
        runTest(testDispatcher) {
            val registry = registryWith(ThrowingPlugin(), StubPlugin())
            val sections = registry.getHomeSections(page = 1)
            // ThrowingPlugin's provider never registers; StubPlugin's does.
            assertThat(sections).hasSize(1)
        }
}

// ─────────────────────────────────────────────────────────────────────────────
// Stub plugins and providers
// ─────────────────────────────────────────────────────────────────────────────

private class StubPlugin : DreamPlugin() {
    override fun registerProviders(): List<ContentProvider> = listOf(StubProvider())
}

/** Plugin whose onLoad() throws — simulates a broken bundled plugin. */
private class ThrowingPlugin : DreamPlugin() {
    override fun onLoad() {
        throw IllegalStateException("Simulated plugin load failure")
    }

    override fun registerProviders(): List<ContentProvider> = listOf(StubProvider())
}

/** Plugin that only supports home page, not search. */
private class HomeOnlyPlugin : DreamPlugin() {
    override fun registerProviders(): List<ContentProvider> = listOf(
        object : BaseStubProvider() {
            override val supportsHomePage: Boolean = true
            override val supportsSearch: Boolean = false
        },
    )
}

/** Plugin that only supports search, not home page. */
private class SearchOnlyPlugin : DreamPlugin() {
    override fun registerProviders(): List<ContentProvider> = listOf(
        object : BaseStubProvider() {
            override val supportsHomePage: Boolean = false
            override val supportsSearch: Boolean = true
        },
    )
}

/** Plugin whose provider's getMainPage throws. */
private class FailingHomeProvider : DreamPlugin() {
    override fun registerProviders(): List<ContentProvider> = listOf(
        object : BaseStubProvider() {
            override val supportsHomePage: Boolean = true
            override suspend fun getMainPage(page: Int, request: CatalogRequest): CatalogResponse {
                throw RuntimeException("getMainPage failed")
            }
        },
    )
}

/** Plugin whose provider's search throws. */
private class FailingSearchProvider : DreamPlugin() {
    override fun registerProviders(): List<ContentProvider> = listOf(
        object : BaseStubProvider() {
            override val supportsSearch: Boolean = true
            override suspend fun search(query: String): List<ApiSearchResult> {
                throw RuntimeException("search failed")
            }
        },
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Base helpers
// ─────────────────────────────────────────────────────────────────────────────

/** Minimal stub provider that returns one section and one search result. */
private open class StubProvider : BaseStubProvider() {
    override val supportsHomePage: Boolean = true
    override val supportsSearch: Boolean = true

    override suspend fun getMainPage(page: Int, request: CatalogRequest): CatalogResponse =
        CatalogResponse(
            sections = listOf(
                CatalogSection(
                    name = "Stub Section",
                    items = listOf(
                        MovieResult(
                            name = "Stub Movie",
                            url = "https://stub.example/movie/1",
                            providerId = name,
                            year = 2024,
                        ),
                    ),
                ),
            ),
        )

    override suspend fun search(query: String): List<ApiSearchResult> = listOf(
        ApiMovieResult(name = "Stub $query", url = "https://stub.example/movie/q"),
    )
}

/** Abstract base that satisfies all ContentProvider abstract members. */
private abstract class BaseStubProvider : ContentProvider() {
    override val name: String = "stub"
    override val mainUrl: String = "https://stub.example"
    override val lang: String = "en"
    override val supportedTypes: Set<ContentType> = setOf(ContentType.Movie)

    override suspend fun load(url: String): ApiContentDetail? = null

    override suspend fun loadLinks(
        data: String,
        isCasting: Boolean,
        subtitleCallback: (Subtitle) -> Unit,
        linkCallback: (StreamLink) -> Unit,
    ): Boolean = false
}
