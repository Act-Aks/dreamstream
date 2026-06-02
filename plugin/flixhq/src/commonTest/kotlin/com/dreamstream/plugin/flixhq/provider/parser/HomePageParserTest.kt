package com.dreamstream.plugin.flixhq.provider.parser

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlin.test.Test

class HomePageParserTest {

    // ── Two-section happy-path fixture ────────────────────────────────────────

    private val MULTI_SECTION_HTML = """
        <html><body>
        <div class="block_area">
          <h2 class="cat-heading">Trending Movies</h2>
          <div class="flw-item">
            <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/movie1.jpg" /></div>
            <div class="film-detail">
              <h3 class="film-name"><a href="/movie/cosmic-odyssey-123">Cosmic Odyssey</a></h3>
              <div class="fd-infor"><span class="fdi-item">2024</span></div>
            </div>
          </div>
          <div class="flw-item">
            <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/show1.jpg" /></div>
            <div class="film-detail">
              <div class="film-name"><a href="/tv/neon-city-456">Neon City</a></div>
              <div class="fd-infor"><span class="fdi-item">2023</span></div>
            </div>
          </div>
        </div>
        <div class="block_area">
          <h2 class="cat-heading">New Arrivals</h2>
          <div class="flw-item">
            <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/movie2.jpg" /></div>
            <div class="film-detail">
              <h3 class="film-name"><a href="/movie/dark-horizon-789">Dark Horizon</a></h3>
              <div class="fd-infor"><span class="fdi-item">2022</span></div>
            </div>
          </div>
        </div>
        </body></html>
    """.trimIndent()

    // ── Section structure ─────────────────────────────────────────────────────

    @Test
    fun `parse with two sections returns both sections`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections).hasSize(2)
    }

    @Test
    fun `section titles are parsed correctly`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].name).isEqualTo("Trending Movies")
        assertThat(sections[1].name).isEqualTo("New Arrivals")
    }

    @Test
    fun `section item count is correct`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items).hasSize(2)
        assertThat(sections[1].items).hasSize(1)
    }

    // ── Content type detection ─────────────────────────────────────────────────

    @Test
    fun `item with movie path segment sets isMovie true`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        // "/movie/cosmic-odyssey-123"
        assertThat(sections[0].items[0].isMovie).isTrue()
    }

    @Test
    fun `item with tv path segment sets isMovie false`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        // "/tv/neon-city-456"
        assertThat(sections[0].items[1].isMovie).isFalse()
    }

    // ── Title and URL ──────────────────────────────────────────────────────────

    @Test
    fun `title is extracted from h3 film-name anchor`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].title).isEqualTo("Cosmic Odyssey")
    }

    @Test
    fun `title is extracted from div film-name anchor`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        // second item uses <div class="film-name">
        assertThat(sections[0].items[1].title).isEqualTo("Neon City")
    }

    @Test
    fun `relative URL is prefixed with baseUrl`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML, baseUrl = "https://flixhq.to")
        assertThat(sections[0].items[0].url).isEqualTo("https://flixhq.to/movie/cosmic-odyssey-123")
    }

    @Test
    fun `absolute URL in href is kept as-is`() {
        val html = """
            <div class="block_area">
              <h2 class="cat-heading">Test</h2>
              <div class="flw-item">
                <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/x.jpg" /></div>
                <div class="film-detail">
                  <h3 class="film-name"><a href="https://other.cdn/movie/abc">Title</a></h3>
                </div>
              </div>
            </div>
        """.trimIndent()
        val sections = HomePageParser.parse(html, baseUrl = "https://flixhq.to")
        assertThat(sections[0].items[0].url).isEqualTo("https://other.cdn/movie/abc")
    }

    // ── Year ───────────────────────────────────────────────────────────────────

    @Test
    fun `year is parsed from fdi-item span`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].year).isEqualTo(2024)
    }

    @Test
    fun `year is null when span contains non-numeric text`() {
        val html = """
            <div class="block_area">
              <h2 class="cat-heading">Test</h2>
              <div class="flw-item">
                <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/x.jpg" /></div>
                <div class="film-detail">
                  <h3 class="film-name"><a href="/movie/abc">Title</a></h3>
                  <div class="fd-infor"><span class="fdi-item">Unknown</span></div>
                </div>
              </div>
            </div>
        """.trimIndent()
        val sections = HomePageParser.parse(html)
        assertThat(sections[0].items[0].year).isNull()
    }

    // ── Poster URL ─────────────────────────────────────────────────────────────

    @Test
    fun `poster URL is extracted from data-src attribute`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].posterUrl).isEqualTo("https://cdn.flixhq.to/img/movie1.jpg")
    }

    @Test
    fun `poster URL falls back to src when data-src is absent`() {
        val html = """
            <div class="block_area">
              <h2 class="cat-heading">Test</h2>
              <div class="flw-item">
                <div class="film-poster"><img src="https://cdn.flixhq.to/img/fallback.jpg" /></div>
                <div class="film-detail">
                  <h3 class="film-name"><a href="/movie/abc">Title</a></h3>
                </div>
              </div>
            </div>
        """.trimIndent()
        val sections = HomePageParser.parse(html)
        assertThat(sections[0].items[0].posterUrl).isEqualTo("https://cdn.flixhq.to/img/fallback.jpg")
    }

    // ── Edge cases ─────────────────────────────────────────────────────────────

    @Test
    fun `empty HTML returns empty section list`() {
        assertThat(HomePageParser.parse("")).isEmpty()
    }

    @Test
    fun `section without h2 cat-heading is excluded`() {
        val html = """
            <div class="block_area">
              <div class="flw-item">
                <div class="film-poster"><img data-src="x.jpg" /></div>
                <div class="film-detail">
                  <h3 class="film-name"><a href="/movie/abc">Title</a></h3>
                </div>
              </div>
            </div>
        """.trimIndent()
        assertThat(HomePageParser.parse(html)).isEmpty()
    }

    @Test
    fun `item missing the title anchor is excluded from section`() {
        val html = """
            <div class="block_area">
              <h2 class="cat-heading">Test</h2>
              <div class="flw-item">
                <div class="film-poster"><img data-src="x.jpg" /></div>
                <!-- no film-name anchor -->
                <div class="film-detail"></div>
              </div>
              <div class="flw-item">
                <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/valid.jpg" /></div>
                <div class="film-detail">
                  <h3 class="film-name"><a href="/movie/valid">Valid Title</a></h3>
                </div>
              </div>
            </div>
        """.trimIndent()
        val sections = HomePageParser.parse(html)
        assertThat(sections).hasSize(1)
        assertThat(sections[0].items).hasSize(1)
        assertThat(sections[0].items[0].title).isEqualTo("Valid Title")
    }

    @Test
    fun `section with all invalid items is excluded`() {
        val html = """
            <div class="block_area">
              <h2 class="cat-heading">Broken Section</h2>
              <div class="flw-item">
                <!-- no film-poster, no film-name -->
              </div>
            </div>
        """.trimIndent()
        assertThat(HomePageParser.parse(html)).isEmpty()
    }
}
