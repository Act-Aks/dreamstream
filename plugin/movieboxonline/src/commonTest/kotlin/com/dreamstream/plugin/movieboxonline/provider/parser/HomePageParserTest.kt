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

class HomePageParserTest {

    // ── Fixtures ───────────────────────────────────────────────────────────────

    /** Two sections with 2 and 1 cards respectively; Nuxt payload has 3 matching poster URLs. */
    private val MULTI_SECTION_HTML = """
        <html><body>
        <script type="application/json" data-nuxt-data="nuxt-app">
        ["data",
         "https://pbcdnw.aoneroom.com/image/2024/01/01/poster1.jpg",
         "https://pbcdnw.aoneroom.com/image/2024/01/02/poster2.jpg",
         "https://pbcdnw.aoneroom.com/image/2024/01/03/poster3.jpg"]
        </script>
        <article class="section-panel">
          <div class="title">Trending Now</div>
          <div class="movie-card-list">
            <a class="movie-card" href="/detailpage/inception-AbCdEfGh">
              <p class="movie-card-title">Inception</p>
              <p class="movie-card-meta">2010 <span> • </span> Action</p>
            </a>
            <a class="movie-card" href="/detailpage/one-piece-s1-XyZwVuTq">
              <p class="movie-card-title">One Piece S1</p>
              <p class="movie-card-meta">2023 <span> • </span> Animation</p>
            </a>
          </div>
        </article>
        <article class="section-panel">
          <div class="title">Bollywood</div>
          <div class="movie-card-list">
            <a class="movie-card" href="/detailpage/pathaan-hindi-MnOpQrSt">
              <p class="movie-card-title">Pathaan</p>
              <p class="movie-card-meta">2023 <span> • </span> Action</p>
            </a>
          </div>
        </article>
        </body></html>
    """.trimIndent()

    // ── Section structure ─────────────────────────────────────────────────────

    @Test
    fun `parse returns both sections`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections).hasSize(2)
    }

    @Test
    fun `section titles are parsed correctly`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].name).isEqualTo("Trending Now")
        assertThat(sections[1].name).isEqualTo("Bollywood")
    }

    @Test
    fun `section item counts are correct`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items).hasSize(2)
        assertThat(sections[1].items).hasSize(1)
    }

    // ── Poster URLs from Nuxt payload ─────────────────────────────────────────

    @Test
    fun `first card receives first CDN poster URL`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].posterUrl).isEqualTo(
            "https://pbcdnw.aoneroom.com/image/2024/01/01/poster1.jpg"
        )
    }

    @Test
    fun `second card receives second CDN poster URL`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[1].posterUrl).isEqualTo(
            "https://pbcdnw.aoneroom.com/image/2024/01/02/poster2.jpg"
        )
    }

    @Test
    fun `poster URL is null when payload has fewer URLs than cards`() {
        val htmlNoPoster = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">["no","cdn","urls"]</script>
            <article class="section-panel">
              <div class="title">Test</div>
              <a class="movie-card" href="/detailpage/test-AbCdEfGh">
                <p class="movie-card-title">Test Movie</p>
                <p class="movie-card-meta">2024 <span> • </span> Drama</p>
              </a>
            </article>
            </body></html>
        """.trimIndent()
        val sections = HomePageParser.parse(htmlNoPoster)
        assertThat(sections[0].items[0].posterUrl).isNull()
    }

    // ── Content-type detection ─────────────────────────────────────────────────

    @Test
    fun `title without season suffix is treated as movie`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        // "Inception" — no S\d suffix
        assertThat(sections[0].items[0].isMovie).isTrue()
    }

    @Test
    fun `title with S-digit season suffix is treated as series`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        // "One Piece S1"
        assertThat(sections[0].items[1].isMovie).isFalse()
    }

    // ── Title, URL, year, genre ───────────────────────────────────────────────

    @Test
    fun `title is extracted from p movie-card-title`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].title).isEqualTo("Inception")
    }

    @Test
    fun `relative URL is prefixed with baseUrl`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML, baseUrl = "https://movieboxonline.org")
        assertThat(sections[0].items[0].url).isEqualTo(
            "https://movieboxonline.org/detailpage/inception-AbCdEfGh"
        )
    }

    @Test
    fun `absolute URL in href is kept as-is`() {
        val html = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
            <article class="section-panel">
              <div class="title">Test</div>
              <a class="movie-card" href="https://other.cdn/detailpage/abc">
                <p class="movie-card-title">Title</p>
                <p class="movie-card-meta">2024 <span> • </span> Drama</p>
              </a>
            </article>
            </body></html>
        """.trimIndent()
        val sections = HomePageParser.parse(html, baseUrl = "https://movieboxonline.org")
        assertThat(sections[0].items[0].url).isEqualTo("https://other.cdn/detailpage/abc")
    }

    @Test
    fun `year is parsed from meta text`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].year).isEqualTo(2010)
    }

    @Test
    fun `genre is parsed from meta text after bullet separator`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].genre).isEqualTo("Action")
    }

    @Test
    fun `rating is always null on home-page cards`() {
        val sections = HomePageParser.parse(MULTI_SECTION_HTML)
        assertThat(sections[0].items[0].rating).isNull()
    }

    // ── parseMeta helper ───────────────────────────────────────────────────────

    @Test
    fun `parseMeta splits year and genre correctly`() {
        val (year, genre) = HomePageParser.parseMeta("2024 • Thriller")
        assertThat(year).isEqualTo(2024)
        assertThat(genre).isEqualTo("Thriller")
    }

    @Test
    fun `parseMeta returns null year for non-numeric text`() {
        val (year, _) = HomePageParser.parseMeta("Unknown • Drama")
        assertThat(year).isNull()
    }

    @Test
    fun `parseMeta returns null genre when no separator`() {
        val (_, genre) = HomePageParser.parseMeta("2024")
        assertThat(genre).isNull()
    }

    @Test
    fun `parseMeta handles empty string`() {
        val (year, genre) = HomePageParser.parseMeta("")
        assertThat(year).isNull()
        assertThat(genre).isNull()
    }

    // ── isMovie helper ────────────────────────────────────────────────────────

    @Test
    fun `isMovie returns true for plain movie title`() {
        assertThat(HomePageParser.isMovie("Inception")).isTrue()
    }

    @Test
    fun `isMovie returns false for S1 season suffix`() {
        assertThat(HomePageParser.isMovie("One Piece S1")).isFalse()
    }

    @Test
    fun `isMovie returns false for S1-S3 range suffix`() {
        assertThat(HomePageParser.isMovie("Avatar: The Last Airbender S1-S3")).isFalse()
    }

    @Test
    fun `isMovie returns true when title contains S followed by non-digit`() {
        // "Stranger" starts with S but has no space+S+digit pattern
        assertThat(HomePageParser.isMovie("Stranger Things")).isTrue()
    }

    // ── Edge cases ─────────────────────────────────────────────────────────────

    @Test
    fun `parse returns empty list for empty HTML`() {
        assertThat(HomePageParser.parse("")).isEmpty()
    }

    @Test
    fun `section without div-title is excluded`() {
        val html = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
            <article class="section-panel">
              <!-- no div.title -->
              <a class="movie-card" href="/detailpage/test-Ab">
                <p class="movie-card-title">Test</p>
                <p class="movie-card-meta">2024 <span> • </span> Action</p>
              </a>
            </article>
            </body></html>
        """.trimIndent()
        assertThat(HomePageParser.parse(html)).isEmpty()
    }

    @Test
    fun `card missing title is excluded from its section`() {
        val html = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
            <article class="section-panel">
              <div class="title">Test Section</div>
              <a class="movie-card" href="/detailpage/no-title-Ab">
                <!-- no p.movie-card-title -->
              </a>
              <a class="movie-card" href="/detailpage/valid-Cd">
                <p class="movie-card-title">Valid Title</p>
                <p class="movie-card-meta">2024 <span> • </span> Drama</p>
              </a>
            </article>
            </body></html>
        """.trimIndent()
        val sections = HomePageParser.parse(html)
        assertThat(sections).hasSize(1)
        assertThat(sections[0].items).hasSize(1)
        assertThat(sections[0].items[0].title).isEqualTo("Valid Title")
    }

    @Test
    fun `section where all cards are invalid is excluded`() {
        val html = """
            <html><body>
            <script type="application/json" data-nuxt-data="nuxt-app">[]</script>
            <article class="section-panel">
              <div class="title">Broken Section</div>
              <a class="movie-card"><!-- no href, no title --></a>
            </article>
            </body></html>
        """.trimIndent()
        assertThat(HomePageParser.parse(html)).isEmpty()
    }

    @Test
    fun `parseHomeCard returns null for card with missing href`() {
        val doc = com.fleeksoft.ksoup.Ksoup.parse(
            """<a class="movie-card">
                 <p class="movie-card-title">Title</p>
               </a>"""
        )
        val element = doc.selectFirst("a.movie-card")!!
        assertThat(HomePageParser.parseHomeCard(element, "https://movieboxonline.org", null)).isNull()
    }
}
