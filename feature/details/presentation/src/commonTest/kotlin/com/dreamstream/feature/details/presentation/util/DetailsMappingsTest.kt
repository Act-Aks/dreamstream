package com.dreamstream.feature.details.presentation.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import assertk.assertions.isNull
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.detail.ContentDetail
import com.dreamstream.core.model.detail.MovieDetail
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.details.domain.error.DetailsError
import com.dreamstream.feature.details.presentation.resources.Res
import com.dreamstream.feature.details.presentation.resources.details_error_load_failed
import com.dreamstream.feature.details.presentation.resources.details_error_not_found
import kotlin.test.Test

class DetailsMappingsTest {

    // ── ContentDetail.toDetailContentUi() ─────────────────────────────────────

    @Test
    fun `toDetailContentUi maps url as contentId, name as title, plot as synopsis, posterUrl, backgroundPosterUrl`() {
        val response = content(
            url = "r2",
            name = "Void Protocol",
            plot = "An AI gains sentience.",
            posterUrl = "https://example.com/thumb.jpg",
            backdropUrl = "https://example.com/backdrop.jpg",
        )

        val ui = response.toDetailContentUi()

        assertThat(ui.contentId).isEqualTo("r2")
        assertThat(ui.title).isEqualTo("Void Protocol")
        assertThat(ui.synopsis).isEqualTo("An AI gains sentience.")
        assertThat(ui.thumbnailUrl).isEqualTo("https://example.com/thumb.jpg")
        assertThat(ui.backdropUrl).isEqualTo("https://example.com/backdrop.jpg")
    }

    @Test
    fun `toDetailContentUi maps null posterUrl and backgroundPosterUrl to null`() {
        val ui = content(posterUrl = null, backdropUrl = null).toDetailContentUi()
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
    fun `toDetailContentUi passes tags through as genres`() {
        val tags = listOf("Sci-Fi", "Drama", "Thriller")
        assertThat(content(tags = tags).toDetailContentUi().genres).isEqualTo(tags)
    }

    // ── Type display names ────────────────────────────────────────────────────

    @Test
    fun `toDetailContentUi maps Movie type to localized display name`() {
        val result = content(type = ContentType.Movie).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `toDetailContentUi maps TvSeries type to localized display name`() {
        val result = content(type = ContentType.TvSeries).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `toDetailContentUi maps Anime type to localized display name`() {
        val result = content(type = ContentType.Anime).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `toDetailContentUi maps Documentary type to localized display name`() {
        val result = content(type = ContentType.Documentary).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `toDetailContentUi maps Others type to localized display name`() {
        val result = content(type = ContentType.Others).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `toDetailContentUi maps AnimeMovie type to localized display name`() {
        val result = content(type = ContentType.AnimeMovie).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `toDetailContentUi maps Live type to localized display name`() {
        val result = content(type = ContentType.Live).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `toDetailContentUi maps Music type to localized display name`() {
        val result = content(type = ContentType.Music).toDetailContentUi().typeName
        assertThat(result).isInstanceOf(UiText.StringResourceId::class)
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
    fun `toUiText maps NotFound to string resource`() {
        assertThat(DetailsError.NotFound.toUiText()).isEqualTo(UiText.StringResourceId(Res.string.details_error_not_found))
    }

    @Test
    fun `toUiText maps LoadFailed to string resource`() {
        assertThat(DetailsError.LoadFailed.toUiText()).isEqualTo(UiText.StringResourceId(Res.string.details_error_load_failed))
    }

    @Test
    fun `toUiText NotFound and LoadFailed produce different messages`() {
        assertThat(DetailsError.NotFound.toUiText() == DetailsError.LoadFailed.toUiText()).isEqualTo(
            false
        )
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers
    // ─────────────────────────────────────────────────────────────────────────

    private fun content(
        url: String = "test-id",
        name: String = "Test Title",
        plot: String? = "A test synopsis.",
        posterUrl: String? = null,
        backdropUrl: String? = null,
        type: ContentType = ContentType.Movie,
        year: Int? = 2024,
        rating: Float? = 8.0f,
        tags: List<String> = listOf("Drama"),
        durationMinutes: Int? = 120,
    ): ContentDetail = MovieDetail(
        name = name,
        url = url,
        dataUrl = "",
        providerId = "test",
        posterUrl = posterUrl,
        backgroundPosterUrl = backdropUrl,
        type = type,
        year = year,
        plot = plot,
        rating = rating,
        tags = tags,
        duration = durationMinutes,
    )
}
