package com.dreamstream.feature.details.presentation.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.domain.model.DetailContent
import com.dreamstream.feature.details.domain.model.DetailMediaType
import kotlin.test.Test

class DetailsMappingsTest {

    // ── DetailContent.toDetailContentUi() ────────────────────────────────────

    @Test
    fun `toDetailContentUi maps contentId, title, synopsis, thumbnailUrl, backdropUrl`() {
        val content = content(
            contentId = "r2",
            title = "Void Protocol",
            synopsis = "An AI gains sentience.",
            thumbnailUrl = "https://example.com/thumb.jpg",
            backdropUrl = "https://example.com/backdrop.jpg",
        )

        val ui = content.toDetailContentUi()

        assertThat(ui.contentId).isEqualTo("r2")
        assertThat(ui.title).isEqualTo("Void Protocol")
        assertThat(ui.synopsis).isEqualTo("An AI gains sentience.")
        assertThat(ui.thumbnailUrl).isEqualTo("https://example.com/thumb.jpg")
        assertThat(ui.backdropUrl).isEqualTo("https://example.com/backdrop.jpg")
    }

    @Test
    fun `toDetailContentUi maps null thumbnailUrl and backdropUrl to null`() {
        val ui = content(thumbnailUrl = null, backdropUrl = null).toDetailContentUi()
        assertThat(ui.thumbnailUrl).isNull()
        assertThat(ui.backdropUrl).isNull()
    }

    @Test
    fun `toDetailContentUi formats year as string`() {
        assertThat(content(year = 2023).toDetailContentUi().year).isEqualTo("2023")
    }

    @Test
    fun `toDetailContentUi formats null year as empty string`() {
        assertThat(content(year = null).toDetailContentUi().year).isEqualTo("")
    }

    @Test
    fun `toDetailContentUi formats rating to one decimal place`() {
        assertThat(content(rating = 9.1f).toDetailContentUi().rating).isEqualTo("9.1")
    }

    @Test
    fun `toDetailContentUi formats null rating as empty string`() {
        assertThat(content(rating = null).toDetailContentUi().rating).isEqualTo("")
    }

    @Test
    fun `toDetailContentUi passes genres list through unchanged`() {
        val genres = listOf("Sci-Fi", "Drama", "Thriller")
        assertThat(content(genres = genres).toDetailContentUi().genres).isEqualTo(genres)
    }

    // ── Type display names ────────────────────────────────────────────────────

    @Test
    fun `toDetailContentUi maps Movie type to display name`() {
        assertThat(content(type = DetailMediaType.Movie).toDetailContentUi().typeName).isEqualTo("Movie")
    }

    @Test
    fun `toDetailContentUi maps Series type to display name`() {
        assertThat(content(type = DetailMediaType.Series).toDetailContentUi().typeName).isEqualTo("Series")
    }

    @Test
    fun `toDetailContentUi maps Anime type to display name`() {
        assertThat(content(type = DetailMediaType.Anime).toDetailContentUi().typeName).isEqualTo("Anime")
    }

    @Test
    fun `toDetailContentUi maps Documentary type to display name`() {
        assertThat(content(type = DetailMediaType.Documentary).toDetailContentUi().typeName).isEqualTo("Documentary")
    }

    @Test
    fun `toDetailContentUi maps Short type to display name`() {
        assertThat(content(type = DetailMediaType.Short).toDetailContentUi().typeName).isEqualTo("Short")
    }

    // ── Duration formatting ───────────────────────────────────────────────────

    @Test
    fun `toDetailContentUi formats null duration as empty string`() {
        assertThat(content(durationMinutes = null).toDetailContentUi().duration).isEqualTo("")
    }

    @Test
    fun `toDetailContentUi formats duration under one hour as minutes only`() {
        assertThat(content(durationMinutes = 45).toDetailContentUi().duration).isEqualTo("45m")
    }

    @Test
    fun `toDetailContentUi formats duration of exactly one hour`() {
        assertThat(content(durationMinutes = 60).toDetailContentUi().duration).isEqualTo("1h")
    }

    @Test
    fun `toDetailContentUi formats duration over one hour as hours and minutes`() {
        assertThat(content(durationMinutes = 127).toDetailContentUi().duration).isEqualTo("2h 7m")
    }

    @Test
    fun `toDetailContentUi formats duration of exactly two hours`() {
        assertThat(content(durationMinutes = 120).toDetailContentUi().duration).isEqualTo("2h")
    }

    // ── DetailsError.toUiText() ───────────────────────────────────────────────

    @Test
    fun `toUiText maps NotFound to dynamic string`() {
        assertThat(DetailsError.NotFound.toUiText())
            .isEqualTo(UiText.DynamicString("Content not found."))
    }

    @Test
    fun `toUiText maps LoadFailed to dynamic string`() {
        assertThat(DetailsError.LoadFailed.toUiText())
            .isEqualTo(UiText.DynamicString("Failed to load content. Please try again."))
    }

    @Test
    fun `toUiText NotFound and LoadFailed produce different messages`() {
        assertThat(DetailsError.NotFound.toUiText() == DetailsError.LoadFailed.toUiText())
            .isEqualTo(false)
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun content(
        contentId: String = "test-id",
        title: String = "Test Title",
        synopsis: String = "A test synopsis.",
        thumbnailUrl: String? = null,
        backdropUrl: String? = null,
        type: DetailMediaType = DetailMediaType.Movie,
        year: Int? = 2024,
        rating: Float? = 8.0f,
        genres: List<String> = listOf("Drama"),
        durationMinutes: Int? = 120,
    ): DetailContent = DetailContent(
        contentId = contentId,
        title = title,
        synopsis = synopsis,
        thumbnailUrl = thumbnailUrl,
        backdropUrl = backdropUrl,
        type = type,
        year = year,
        rating = rating,
        genres = genres,
        durationMinutes = durationMinutes,
    )
}
