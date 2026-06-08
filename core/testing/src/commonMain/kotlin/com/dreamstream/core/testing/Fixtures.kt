package com.dreamstream.core.testing

import com.dreamstream.core.domain.model.catalog.Actor
import com.dreamstream.core.domain.model.catalog.ContentType
import com.dreamstream.core.domain.model.catalog.Episode
import com.dreamstream.core.domain.model.catalog.Quality
import com.dreamstream.core.domain.model.catalog.Season
import com.dreamstream.core.domain.model.catalog.SubtitleFormat
import com.dreamstream.core.domain.model.detail.MovieDetail
import com.dreamstream.core.domain.model.detail.SeriesDetail
import com.dreamstream.core.domain.model.media.StreamLink
import com.dreamstream.core.domain.model.media.Subtitle
import com.dreamstream.core.domain.model.search.MovieResult
import com.dreamstream.core.domain.model.search.SeriesResult
import com.dreamstream.core.domain.model.user.Bookmark
import com.dreamstream.core.domain.model.user.BookmarkCategory
import com.dreamstream.core.domain.model.user.WatchHistory

object Fixtures {

    const val PROVIDER_ID = "test_provider"
    const val URL = "https://example.com"

    fun movieResult(
        name: String = "Test Movie",
        url: String = "$URL/movie/1",
        posterUrl: String? = "$URL/poster.jpg",
        providerId: String = PROVIDER_ID,
        year: Int? = 2024,
    ) = MovieResult(
        name = name,
        url = url,
        posterUrl = posterUrl,
        providerId = providerId,
        year = year,
        quality = Quality.HD,
        rating = 8.0f,
    )

    fun seriesResult(
        name: String = "Test Series",
        url: String = "$URL/series/1",
        providerId: String = PROVIDER_ID,
    ) = SeriesResult(
        name = name,
        url = url,
        posterUrl = null,
        providerId = providerId,
        year = 2024,
        totalSeasons = 3,
        rating = 8.5f,
    )

    fun movieDetail(
        name: String = "Test Movie",
        url: String = "$URL/movie/1",
        providerId: String = PROVIDER_ID,
    ) = MovieDetail(
        name = name,
        url = url,
        dataUrl = "$url/watch",
        posterUrl = "$URL/poster.jpg",
        backgroundPosterUrl = "$URL/backdrop.jpg",
        year = 2024,
        plot = "A test movie about testing.",
        rating = 8.0f,
        tags = listOf("Action", "Drama"),
        actors = listOf(
            Actor(name = "Actor One", role = "Hero"),
            Actor(name = "Actor Two", role = "Villain"),
        ),
        providerId = providerId,
        duration = 120,
    )

    fun seriesDetail(
        name: String = "Test Series",
        url: String = "$URL/series/1",
        providerId: String = PROVIDER_ID,
        seasons: Int = 2,
        episodesPerSeason: Int = 10,
    ) = SeriesDetail(
        name = name,
        url = url,
        posterUrl = "$URL/poster.jpg",
        year = 2024,
        plot = "A test series about testing.",
        rating = 8.5f,
        tags = listOf("Drama"),
        providerId = providerId,
        seasons = (1..seasons).map { s ->
            Season(season = s, name = "Season $s", episodeCount = episodesPerSeason)
        },
        episodes = (1..seasons).flatMap { s ->
            (1..episodesPerSeason).map { e ->
                Episode(
                    data = "$url/s${s}e$e",
                    name = "S${s.toString().padStart(2, '0')}E${e.toString().padStart(2, '0')}",
                    season = s,
                    episode = e,
                )
            }
        },
    )

    fun streamLink(
        url: String = "$URL/video.mp4",
        name: String = "Main Server",
        quality: Quality = Quality.HD,
    ) = StreamLink(
        url = url,
        name = name,
        quality = quality,
        headers = mapOf("Referer" to URL),
    )

    fun subtitle(
        url: String = "$URL/sub.srt",
        lang: String = "en",
    ) = Subtitle(
        url = url,
        lang = lang,
        format = SubtitleFormat.SRT,
        isDefault = true,
    )

    fun watchHistory(
        id: String = "history_1",
        providerId: String = PROVIDER_ID,
        url: String = "$URL/movie/1",
        title: String = "Test Movie",
        type: ContentType = ContentType.Movie,
        positionMs: Long = 1_800_000L,
        durationMs: Long = 7_200_000L,
    ) = WatchHistory(
        id = id,
        providerId = providerId,
        url = url,
        title = title,
        posterUrl = null,
        type = type,
        watchPositionMs = positionMs,
        totalDurationMs = durationMs,
        lastWatchedAt = 1_000_000L,
        createdAt = 900_000L,
    )

    fun bookmark(
        id: String = "bookmark_1",
        providerId: String = PROVIDER_ID,
        url: String = "$URL/movie/1",
        title: String = "Test Movie",
        type: ContentType = ContentType.Movie,
        category: String = BookmarkCategory.DEFAULT,
    ) = Bookmark(
        id = id,
        providerId = providerId,
        url = url,
        title = title,
        posterUrl = null,
        type = type,
        category = category,
        createdAt = 1_000_000L,
    )
}
