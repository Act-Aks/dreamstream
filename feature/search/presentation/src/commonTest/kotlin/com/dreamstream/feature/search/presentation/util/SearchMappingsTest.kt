package com.dreamstream.feature.search.presentation.util

import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.dreamstream.core.model.catalog.ContentType
import com.dreamstream.core.model.search.AnimeResult
import com.dreamstream.core.model.search.LiveResult
import com.dreamstream.core.model.search.MovieResult
import com.dreamstream.core.model.search.SeriesResult
import com.dreamstream.core.presentation.ui.UiText
import com.dreamstream.feature.search.domain.error.SearchError
import com.dreamstream.feature.search.presentation.resources.Res
import com.dreamstream.feature.search.presentation.resources.search_error_search_failed
import kotlin.test.Test

class SearchMappingsTest {

    // ── SearchResult → SearchResultUi ─────────────────────────────────────────

    @Test
    fun `MovieResult maps id to url`() {
        val result = MovieResult(
            name = "Cosmic Drift", url = "t1", providerId = "local", year = 2024, rating = 8.2f
        )
        assertThat(result.toSearchResultUi().id).isEqualTo("t1")
    }

    @Test
    fun `MovieResult maps title to name`() {
        val result = MovieResult(
            name = "Cosmic Drift", url = "t1", providerId = "local", year = 2024, rating = 8.2f
        )
        assertThat(result.toSearchResultUi().title).isEqualTo("Cosmic Drift")
    }

    @Test
    fun `MovieResult maps typeName to localized Movie`() {
        val result = MovieResult(name = "X", url = "x", providerId = "local")
        assertThat(result.toSearchResultUi().typeName).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `MovieResult with Documentary type maps typeName to localized Documentary`() {
        val result =
            MovieResult(name = "X", url = "x", providerId = "local", type = ContentType.Documentary)
        assertThat(result.toSearchResultUi().typeName).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `MovieResult formats year as string`() {
        val result = MovieResult(name = "X", url = "x", providerId = "local", year = 2024)
        assertThat(result.toSearchResultUi().year).isEqualTo("2024")
    }

    @Test
    fun `MovieResult with null year produces empty year string`() {
        val result = MovieResult(name = "X", url = "x", providerId = "local", year = null)
        assertThat(result.toSearchResultUi().year).isEmpty()
    }

    @Test
    fun `MovieResult formats rating to one decimal place`() {
        val result = MovieResult(name = "X", url = "x", providerId = "local", rating = 8.2f)
        assertThat(result.toSearchResultUi().rating).isEqualTo("8.2")
    }

    @Test
    fun `MovieResult with null rating produces empty rating string`() {
        val result = MovieResult(name = "X", url = "x", providerId = "local", rating = null)
        assertThat(result.toSearchResultUi().rating).isEmpty()
    }

    @Test
    fun `SeriesResult maps typeName to localized TV Series`() {
        val result = SeriesResult(name = "X", url = "x", providerId = "local")
        assertThat(result.toSearchResultUi().typeName).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `AnimeResult maps typeName to localized Anime`() {
        val result = AnimeResult(name = "X", url = "x", providerId = "local")
        assertThat(result.toSearchResultUi().typeName).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `LiveResult maps typeName to localized Live`() {
        val result = LiveResult(name = "X", url = "x", providerId = "local")
        assertThat(result.toSearchResultUi().typeName).isInstanceOf(UiText.StringResourceId::class)
    }

    @Test
    fun `LiveResult produces empty year string`() {
        val result = LiveResult(name = "X", url = "x", providerId = "local")
        assertThat(result.toSearchResultUi().year).isEmpty()
    }

    @Test
    fun `LiveResult produces empty rating string`() {
        val result = LiveResult(name = "X", url = "x", providerId = "local")
        assertThat(result.toSearchResultUi().rating).isEmpty()
    }

    @Test
    fun `SeriesResult formats year correctly`() {
        val result =
            SeriesResult(name = "X", url = "x", providerId = "local", year = 2023, rating = 7.8f)
        assertThat(result.toSearchResultUi().year).isEqualTo("2023")
    }

    @Test
    fun `AnimeResult formats rating to one decimal place`() {
        val result =
            AnimeResult(name = "X", url = "x", providerId = "local", year = 2024, rating = 8.6f)
        assertThat(result.toSearchResultUi().rating).isEqualTo("8.6")
    }

    @Test
    fun `thumbnailUrl is mapped from posterUrl`() {
        val result = MovieResult(
            name = "X", url = "x", providerId = "local", posterUrl = "https://example.com/thumb.jpg"
        )
        assertThat(result.toSearchResultUi().thumbnailUrl).isEqualTo("https://example.com/thumb.jpg")
    }

    @Test
    fun `null posterUrl produces null thumbnailUrl`() {
        val result = MovieResult(name = "X", url = "x", providerId = "local", posterUrl = null)
        assertThat(result.toSearchResultUi().thumbnailUrl).isEqualTo(null)
    }

    // ── SearchError → UiText ─────────────────────────────────────────────────

    @Test
    fun `SearchFailed maps to string resource`() {
        val text = SearchError.SearchFailed.toUiText()
        assertThat(text).isEqualTo(UiText.StringResourceId(Res.string.search_error_search_failed))
    }
}
