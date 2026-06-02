package com.dreamstream.plugin.flixhq.provider.parser

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import kotlin.test.Test

class SearchParserTest {

    private val SEARCH_RESULTS_HTML = """
        <html><body>
        <div class="flw-item">
          <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/inception.jpg" /></div>
          <div class="film-detail">
            <h3 class="film-name"><a href="/movie/inception-1">Inception</a></h3>
            <div class="fd-infor"><span class="fdi-item">2010</span></div>
          </div>
        </div>
        <div class="flw-item">
          <div class="film-poster"><img data-src="https://cdn.flixhq.to/img/bb.jpg" /></div>
          <div class="film-detail">
            <div class="film-name"><a href="/tv/breaking-bad-2">Breaking Bad</a></div>
            <div class="fd-infor"><span class="fdi-item">2008</span></div>
          </div>
        </div>
        </body></html>
    """.trimIndent()

    // ── Happy path ────────────────────────────────────────────────────────────

    @Test
    fun `parse returns one item per flw-item card`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML)
        assertThat(items).hasSize(2)
    }

    @Test
    fun `first result has correct title`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML)
        assertThat(items[0].title).isEqualTo("Inception")
    }

    @Test
    fun `second result has correct title`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML)
        assertThat(items[1].title).isEqualTo("Breaking Bad")
    }

    @Test
    fun `movie item sets isMovie true`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML)
        assertThat(items[0].isMovie).isTrue()
    }

    @Test
    fun `tv item sets isMovie false`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML)
        assertThat(items[1].isMovie).isFalse()
    }

    @Test
    fun `year is extracted correctly`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML)
        assertThat(items[0].year).isEqualTo(2010)
        assertThat(items[1].year).isEqualTo(2008)
    }

    @Test
    fun `poster URL is extracted from data-src`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML)
        assertThat(items[0].posterUrl).isEqualTo("https://cdn.flixhq.to/img/inception.jpg")
    }

    @Test
    fun `relative URL is prefixed with baseUrl`() {
        val items = SearchParser.parse(SEARCH_RESULTS_HTML, baseUrl = "https://flixhq.to")
        assertThat(items[0].url).isEqualTo("https://flixhq.to/movie/inception-1")
    }

    // ── Edge cases ─────────────────────────────────────────────────────────────

    @Test
    fun `empty HTML returns empty list`() {
        assertThat(SearchParser.parse("")).isEmpty()
    }

    @Test
    fun `HTML with no flw-item cards returns empty list`() {
        val html = "<html><body><div class='search-results'></div></body></html>"
        assertThat(SearchParser.parse(html)).isEmpty()
    }

    @Test
    fun `item missing title anchor is excluded`() {
        val html = """
            <div class="flw-item">
              <div class="film-poster"><img data-src="x.jpg" /></div>
              <!-- no film-name anchor -->
            </div>
        """.trimIndent()
        assertThat(SearchParser.parse(html)).isEmpty()
    }
}
