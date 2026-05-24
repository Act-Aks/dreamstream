package com.dreamstream.feature.home.presentation.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.search.MovieResult
import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.HomeSection
import kotlin.test.Test

class HomeMappingsTest {

    // ── SearchResult.toContentUi() ─────────────────────────────────────────

    @Test
    fun `toContentUi maps url as id, name as title, posterUrl as thumbnailUrl`() {
        val response = MovieResult(
            name = "Cosmic Drift",
            url = "abc-123",
            providerId = "test",
            posterUrl = "https://example.com/thumb.jpg",
            year = 2024,
            rating = 8.2f,
        )

        val ui = response.toContentUi()

        assertThat(ui.id).isEqualTo("abc-123")
        assertThat(ui.title).isEqualTo("Cosmic Drift")
        assertThat(ui.thumbnailUrl).isEqualTo("https://example.com/thumb.jpg")
    }

    @Test
    fun `toContentUi maps null posterUrl to null thumbnailUrl`() {
        val ui = content(posterUrl = null).toContentUi()
        assertThat(ui.thumbnailUrl).isNull()
    }

    @Test
    fun `toContentUi formats year as string`() {
        assertThat(content(year = 2023).toContentUi().year).isEqualTo("2023")
    }

    @Test
    fun `toContentUi formats null year as empty string`() {
        assertThat(content(year = null).toContentUi().year).isEqualTo("")
    }

    @Test
    fun `toContentUi formats rating to one decimal place`() {
        assertThat(content(rating = 8.2f).toContentUi().rating).isEqualTo("8.2")
    }

    @Test
    fun `toContentUi formats null rating as empty string`() {
        assertThat(content(rating = null).toContentUi().rating).isEqualTo("")
    }

    @Test
    fun `toContentUi maps Movie type to singular display name`() {
        assertThat(content(type = ContentType.Movie).toContentUi().typeName).isEqualTo("Movie")
    }

    @Test
    fun `toContentUi maps TvSeries type to display name`() {
        assertThat(content(type = ContentType.TvSeries).toContentUi().typeName).isEqualTo("TV Series")
    }

    @Test
    fun `toContentUi maps Anime type to display name`() {
        assertThat(content(type = ContentType.Anime).toContentUi().typeName).isEqualTo("Anime")
    }

    @Test
    fun `toContentUi maps Documentary type to display name`() {
        assertThat(content(type = ContentType.Documentary).toContentUi().typeName).isEqualTo("Documentary")
    }

    @Test
    fun `toContentUi maps Others type to display name`() {
        assertThat(content(type = ContentType.Others).toContentUi().typeName).isEqualTo("Others")
    }

    @Test
    fun `toContentUi maps AnimeMovie type to display name`() {
        assertThat(content(type = ContentType.AnimeMovie).toContentUi().typeName).isEqualTo("Anime Movie")
    }

    @Test
    fun `toContentUi maps Live type to display name`() {
        assertThat(content(type = ContentType.Live).toContentUi().typeName).isEqualTo("Live")
    }

    @Test
    fun `toContentUi maps Music type to display name`() {
        assertThat(content(type = ContentType.Music).toContentUi().typeName).isEqualTo("Music")
    }

    // ── HomeSection.toHomeSectionUi() ─────────────────────────────────────────

    @Test
    fun `toHomeSectionUi maps id and title`() {
        val section = HomeSection(id = "trending", title = "Trending Now", items = emptyList())

        val ui = section.toHomeSectionUi()

        assertThat(ui.id).isEqualTo("trending")
        assertThat(ui.title).isEqualTo("Trending Now")
    }

    @Test
    fun `toHomeSectionUi maps all items`() {
        val section = HomeSection(
            id = "trending",
            title = "Trending Now",
            items = listOf(
                content(url = "content-id"),
                content(url = "b", name = "Another Title"),
            ),
        )

        val ui = section.toHomeSectionUi()

        assertThat(ui.items.size).isEqualTo(2)
        assertThat(ui.items[0].id).isEqualTo("content-id")
        assertThat(ui.items[1].id).isEqualTo("b")
    }

    @Test
    fun `toHomeSectionUi preserves item order`() {
        val section = HomeSection(
            id = "section",
            title = "Section",
            items = listOf(
                content(url = "first"),
                content(url = "second"),
                content(url = "third"),
            ),
        )

        val ids = section.toHomeSectionUi().items.map { it.id }

        assertThat(ids).isEqualTo(listOf("first", "second", "third"))
    }

    // ── HomeError.toUiText() ──────────────────────────────────────────────────

    @Test
    fun `toUiText maps NoContentAvailable to dynamic string`() {
        val uiText = HomeError.NoContentAvailable.toUiText()
        assertThat(uiText).isEqualTo(UiText.DynamicString("No content available right now."))
    }

    @Test
    fun `toUiText maps LoadFailed to dynamic string`() {
        val uiText = HomeError.LoadFailed.toUiText()
        assertThat(uiText).isEqualTo(UiText.DynamicString("Failed to load content. Please try again."))
    }

    @Test
    fun `toUiText NoContentAvailable and LoadFailed produce different messages`() {
        val notAvailable = HomeError.NoContentAvailable.toUiText()
        val loadFailed = HomeError.LoadFailed.toUiText()
        assertThat(notAvailable == loadFailed).isEqualTo(false)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun content(
        url: String = "content-id",
        name: String = "Sample Title",
        type: ContentType = ContentType.Movie,
        year: Int? = 2024,
        rating: Float? = 8.0f,
        posterUrl: String? = null,
    ): SearchResult = MovieResult(
        name = name,
        url = url,
        providerId = "test",
        type = type,
        year = year,
        rating = rating,
        posterUrl = posterUrl,
    )
}
