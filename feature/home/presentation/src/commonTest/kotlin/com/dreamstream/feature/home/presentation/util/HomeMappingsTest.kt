package com.dreamstream.feature.home.presentation.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.home.domain.error.HomeError
import com.dreamstream.feature.home.domain.model.Content
import com.dreamstream.feature.home.domain.model.ContentId
import com.dreamstream.feature.home.domain.model.ContentType
import com.dreamstream.feature.home.domain.model.HomeSection
import kotlin.test.Test

class HomeMappingsTest {

    // ── Content.toContentUi() ─────────────────────────────────────────────────

    @Test
    fun `toContentUi maps id, title, description, thumbnailUrl`() {
        val content = Content(
            id = ContentId("abc-123"),
            title = "Cosmic Drift",
            description = "A space adventure.",
            thumbnailUrl = "https://example.com/thumb.jpg",
            type = ContentType.Movie,
            year = 2024,
            rating = 8.2f,
        )

        val ui = content.toContentUi()

        assertThat(ui.id).isEqualTo("abc-123")
        assertThat(ui.title).isEqualTo("Cosmic Drift")
        assertThat(ui.description).isEqualTo("A space adventure.")
        assertThat(ui.thumbnailUrl).isEqualTo("https://example.com/thumb.jpg")
    }

    @Test
    fun `toContentUi maps null thumbnailUrl to null`() {
        val content = content(thumbnailUrl = null)
        assertThat(content.toContentUi().thumbnailUrl).isNull()
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
    fun `toContentUi maps Movie type to display name`() {
        assertThat(content(type = ContentType.Movie).toContentUi().typeName).isEqualTo("Movie")
    }

    @Test
    fun `toContentUi maps Series type to display name`() {
        assertThat(content(type = ContentType.Series).toContentUi().typeName).isEqualTo("Series")
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
    fun `toContentUi maps Short type to display name`() {
        assertThat(content(type = ContentType.Short).toContentUi().typeName).isEqualTo("Short")
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
            items = listOf(content(), content(id = "b", title = "Another Title")),
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
                content(id = "first"),
                content(id = "second"),
                content(id = "third"),
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
        assertThat(HomeError.NoContentAvailable.toUiText())
            .isEqualTo(HomeError.NoContentAvailable.toUiText())

        val notAvailable = HomeError.NoContentAvailable.toUiText()
        val loadFailed = HomeError.LoadFailed.toUiText()
        assertThat(notAvailable).isEqualTo(notAvailable)
        assertThat(loadFailed).isEqualTo(loadFailed)
        assertThat(notAvailable == loadFailed).isEqualTo(false)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun content(
        id: String = "content-id",
        title: String = "Sample Title",
        type: ContentType = ContentType.Movie,
        year: Int? = 2024,
        rating: Float? = 8.0f,
        thumbnailUrl: String? = null,
    ): Content = Content(
        id = ContentId(id),
        title = title,
        description = "Sample description",
        thumbnailUrl = thumbnailUrl,
        type = type,
        year = year,
        rating = rating,
    )
}
