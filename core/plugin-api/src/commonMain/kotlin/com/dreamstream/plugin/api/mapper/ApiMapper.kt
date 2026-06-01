package com.dreamstream.plugin.api.mapper

import com.dreamstream.core.model.detail.AnimeDetail
import com.dreamstream.core.model.detail.ContentDetail
import com.dreamstream.core.model.detail.MovieDetail
import com.dreamstream.core.model.detail.SeriesDetail
import com.dreamstream.core.model.search.AnimeResult
import com.dreamstream.core.model.search.LiveResult
import com.dreamstream.core.model.search.MovieResult
import com.dreamstream.core.model.search.SearchResult
import com.dreamstream.core.model.search.SeriesResult
import com.dreamstream.plugin.api.model.detail.ApiAnimeDetail
import com.dreamstream.plugin.api.model.detail.ApiContentDetail
import com.dreamstream.plugin.api.model.detail.ApiMovieDetail
import com.dreamstream.plugin.api.model.detail.ApiSeriesDetail
import com.dreamstream.plugin.api.model.search.ApiAnimeResult
import com.dreamstream.plugin.api.model.search.ApiLiveResult
import com.dreamstream.plugin.api.model.search.ApiMovieResult
import com.dreamstream.plugin.api.model.search.ApiSearchResult
import com.dreamstream.plugin.api.model.search.ApiSeriesResult

/**
 * Maps plugin API models to core domain models.
 * This is the bridge between the plugin world and the app world.
 */
object ApiMapper {

    fun ApiSearchResult.toCoreModel(providerId: String): SearchResult = when (this) {
        is ApiMovieResult -> MovieResult(
            name = name,
            url = url,
            posterUrl = posterUrl,
            providerId = providerId,
            year = year,
            quality = quality,
            rating = rating,
        )

        is ApiSeriesResult -> SeriesResult(
            name = name,
            url = url,
            posterUrl = posterUrl,
            providerId = providerId,
            year = year,
            totalSeasons = totalSeasons,
            rating = rating,
        )

        is ApiAnimeResult -> AnimeResult(
            name = name,
            url = url,
            posterUrl = posterUrl,
            providerId = providerId,
            year = year,
            dubAvailable = dubAvailable,
            subAvailable = subAvailable,
        )

        is ApiLiveResult -> LiveResult(
            name = name,
            url = url,
            posterUrl = posterUrl,
            providerId = providerId,
            lang = lang,
            tags = tags,
        )
    }

    fun ApiContentDetail.toCoreModel(providerId: String): ContentDetail = when (this) {
        is ApiMovieDetail -> MovieDetail(
            name = name,
            url = url,
            dataUrl = dataUrl,
            posterUrl = posterUrl,
            backgroundPosterUrl = backgroundUrl,
            year = year,
            plot = plot,
            rating = rating,
            tags = tags,
            actors = actors,
            recommendations = recommendations.map { it.toCoreModel(providerId) },
            providerId = providerId,
            trailerUrl = trailerUrl,
            duration = duration,
            comingSoon = comingSoon,
        )

        is ApiSeriesDetail -> SeriesDetail(
            name = name,
            url = url,
            posterUrl = posterUrl,
            backgroundPosterUrl = backgroundUrl,
            year = year,
            plot = plot,
            rating = rating,
            tags = tags,
            actors = actors,
            recommendations = recommendations.map { it.toCoreModel(providerId) },
            providerId = providerId,
            trailerUrl = trailerUrl,
            episodes = episodes,
            seasons = seasons,
            showStatus = showStatus,
        )

        is ApiAnimeDetail -> AnimeDetail(
            name = name,
            url = url,
            posterUrl = posterUrl,
            backgroundPosterUrl = backgroundUrl,
            year = year,
            plot = plot,
            rating = rating,
            tags = tags,
            actors = actors,
            recommendations = recommendations.map { it.toCoreModel(providerId) },
            providerId = providerId,
            trailerUrl = trailerUrl,
            subEpisodes = subEpisodes,
            dubEpisodes = dubEpisodes,
            showStatus = showStatus,
            japaneseTitle = japaneseTitle,
        )
    }
}
