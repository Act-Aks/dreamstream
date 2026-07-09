package com.dreamstream.plugin.movieboxonline.provider.parser

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import kotlin.test.Test

class NuxtPayloadParserTest {

    // ── Fixtures ───────────────────────────────────────────────────────────────

    private val THREE_POSTER_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">
        ["some","data",
         "https://pbcdnw.aoneroom.com/image/2024/03/15/abc123def456.jpg",
         42,
         "https://pbcdnw.aoneroom.com/image/2023/11/01/xyz789ghi012.jpeg",
         "irrelevant_string",
         "https://pbcdnw.aoneroom.com/image/2025/06/22/mnop345qrs678.webp"
        ]
        </script>
        </body></html>
    """.trimIndent()

    private val DUPLICATE_POSTER_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">
        ["https://pbcdnw.aoneroom.com/image/2024/01/01/dup001.jpg",
         "https://pbcdnw.aoneroom.com/image/2024/01/01/dup001.jpg",
         "https://pbcdnw.aoneroom.com/image/2024/02/02/unique002.jpg"]
        </script>
        </body></html>
    """.trimIndent()

    private val NO_POSTER_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">
        ["just","some","strings",1,2,3]
        </script>
        </body></html>
    """.trimIndent()

    private val NO_SCRIPT_HTML = """
        <html><body><p>No nuxt payload here.</p></body></html>
    """.trimIndent()

    private val EMPTY_SCRIPT_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app"></script>
        </body></html>
    """.trimIndent()

    // ── Happy path ─────────────────────────────────────────────────────────────

    @Test
    fun `returns all CDN poster URLs in document order`() {
        val urls = NuxtPayloadParser.extractPosterUrls(THREE_POSTER_HTML)
        assertThat(urls).hasSize(3)
        assertThat(urls[0]).isEqualTo(
            "https://pbcdnw.aoneroom.com/image/2024/03/15/abc123def456.jpg"
        )
        assertThat(urls[1]).isEqualTo(
            "https://pbcdnw.aoneroom.com/image/2023/11/01/xyz789ghi012.jpeg"
        )
        assertThat(urls[2]).isEqualTo(
            "https://pbcdnw.aoneroom.com/image/2025/06/22/mnop345qrs678.webp"
        )
    }

    @Test
    fun `deduplicates repeated poster URLs keeping first occurrence`() {
        val urls = NuxtPayloadParser.extractPosterUrls(DUPLICATE_POSTER_HTML)
        assertThat(urls).hasSize(2)
        assertThat(urls[0]).isEqualTo("https://pbcdnw.aoneroom.com/image/2024/01/01/dup001.jpg")
        assertThat(urls[1]).isEqualTo("https://pbcdnw.aoneroom.com/image/2024/02/02/unique002.jpg")
    }

    // ── Edge cases ─────────────────────────────────────────────────────────────

    @Test
    fun `returns empty list when payload contains no CDN URLs`() {
        assertThat(NuxtPayloadParser.extractPosterUrls(NO_POSTER_HTML)).isEmpty()
    }

    @Test
    fun `returns empty list when script tag is absent`() {
        assertThat(NuxtPayloadParser.extractPosterUrls(NO_SCRIPT_HTML)).isEmpty()
    }

    @Test
    fun `returns empty list when script tag is empty`() {
        assertThat(NuxtPayloadParser.extractPosterUrls(EMPTY_SCRIPT_HTML)).isEmpty()
    }

    @Test
    fun `returns empty list for completely empty HTML`() {
        assertThat(NuxtPayloadParser.extractPosterUrls("")).isEmpty()
    }
}
