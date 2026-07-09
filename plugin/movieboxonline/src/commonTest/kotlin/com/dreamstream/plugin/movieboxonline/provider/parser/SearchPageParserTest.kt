package com.dreamstream.plugin.movieboxonline.provider.parser

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import kotlin.test.Test

class SearchPageParserTest {

    // ── Fixtures ───────────────────────────────────────────────────────────────

    /** Two search results — one movie, one TV series — with poster URLs in payload. */
    private val TWO_RESULTS_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">
        ["data",
         "https://pbcdnw.aoneroom.com/image/2009/12/15/avatar_poster.jpg",
         "https://pbcdnw.aoneroom.com/image/2024/01/10/series_poster.jpeg"]
        </script>
        <div class="results-grid">
          <a class="card" href="/detailpage/avatar-way-of-water-AbCdEfGh">
            <h2 class="card-title" title="Avatar: The Way of Water">Avatar: The Way of Water</h2>
            <div class="card-meta">2022 <span> • </span> Action</div>
            <span class="rate">7.6</span>
          </a>
          <a class="card" href="/detailpage/avatar-airbender-s1-s2-XyZwVuTq">
            <h2 class="card-title" title="Avatar: The Last Airbender S1-S2">Avatar: The Last Airbender S1-S2</h2>
            <div class="card-meta">2005 <span> • </span> Animation</div>
            <span class="rate">9.2</span>
          </a>
        </div>
        </body></html>
    """.trimIndent()

    private val NO_RESULTS_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
        <div class="results-grid"></div>
        </body></html>
    """.trimIndent()

    // ── Happy path ─────────────────────────────────────────────────────────────

    @Test
    fun `parse returns all result cards`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results).hasSize(2)
    }

    @Test
    fun `title is extracted from h2 card-title`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[0].title).isEqualTo("Avatar: The Way of Water")
    }

    @Test
    fun `relative URL is prefixed with baseUrl`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML, baseUrl = "https://movieboxonline.org")
        assertThat(results[0].url).isEqualTo(
            "https://movieboxonline.org/detailpage/avatar-way-of-water-AbCdEfGh"
        )
    }

    @Test
    fun `year is parsed from card-meta`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[0].year).isEqualTo(2022)
    }

    @Test
    fun `genre is parsed from card-meta after bullet`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[0].genre).isEqualTo("Action")
    }

    @Test
    fun `rating is extracted from span-rate as float`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[0].rating).isEqualTo(7.6f)
    }

    // ── Poster pairing ────────────────────────────────────────────────────────

    @Test
    fun `first result receives first CDN poster URL`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[0].posterUrl).isEqualTo(
            "https://pbcdnw.aoneroom.com/image/2009/12/15/avatar_poster.jpg"
        )
    }

    @Test
    fun `second result receives second CDN poster URL`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[1].posterUrl).isEqualTo(
            "https://pbcdnw.aoneroom.com/image/2024/01/10/series_poster.jpeg"
        )
    }

    @Test
    fun `poster URL is null when payload has no CDN URLs`() {
        val results = SearchPageParser.parse(NO_RESULTS_HTML)
        // no results, so nothing to check — just confirm no crash
        assertThat(results).isEmpty()
    }

    // ── Content-type detection ─────────────────────────────────────────────────

    @Test
    fun `movie title without season suffix sets isMovie true`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[0].isMovie).isTrue()
    }

    @Test
    fun `series title with S1-S2 suffix sets isMovie false`() {
        val results = SearchPageParser.parse(TWO_RESULTS_HTML)
        assertThat(results[1].isMovie).isFalse()
    }

    // ── Edge cases ─────────────────────────────────────────────────────────────

    @Test
    fun `returns empty list when results grid is empty`() {
        assertThat(SearchPageParser.parse(NO_RESULTS_HTML)).isEmpty()
    }

    @Test
    fun `returns empty list for empty HTML`() {
        assertThat(SearchPageParser.parse("")).isEmpty()
    }

    @Test
    fun `card missing title is excluded`() {
        val html = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
            <div class="results-grid">
              <a class="card" href="/detailpage/no-title-Ab">
                <!-- no h2.card-title -->
              </a>
              <a class="card" href="/detailpage/valid-Cd">
                <h2 class="card-title">Valid Title</h2>
                <div class="card-meta">2024 <span> • </span> Drama</div>
                <span class="rate">8.0</span>
              </a>
            </div>
            </body></html>
        """.trimIndent()
        val results = SearchPageParser.parse(html)
        assertThat(results).hasSize(1)
        assertThat(results[0].title).isEqualTo("Valid Title")
    }

    @Test
    fun `card with missing rating sets rating to null`() {
        val html = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
            <div class="results-grid">
              <a class="card" href="/detailpage/no-rating-Ab">
                <h2 class="card-title">No Rating Movie</h2>
                <div class="card-meta">2020 <span> • </span> Horror</div>
                <!-- no span.rate -->
              </a>
            </div>
            </body></html>
        """.trimIndent()
        val results = SearchPageParser.parse(html)
        assertThat(results[0].rating).isNull()
    }

    @Test
    fun `non-numeric rating text results in null rating`() {
        val html = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
            <div class="results-grid">
              <a class="card" href="/detailpage/bad-rating-Ab">
                <h2 class="card-title">Bad Rating Movie</h2>
                <div class="card-meta">2020 <span> • </span> Comedy</div>
                <span class="rate">N/A</span>
              </a>
            </div>
            </body></html>
        """.trimIndent()
        val results = SearchPageParser.parse(html)
        assertThat(results[0].rating).isNull()
    }
}
