package com.dreamstream.core.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.dreamstream.core.domain.model.catalog.ContentType

@Entity(
    tableName = "WatchHistory",
    indices = [
        Index(value = ["provider_id"]),
        Index(value = ["url"]),
        Index(value = ["last_watched_at"], orders = arrayOf(Index.Order.DESC))
    ]
)
data class WatchHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "provider_id")
    val providerId: String,

    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "poster_url")
    val posterUrl: String?,

    @ColumnInfo(name = "content_type")
    val contentType: ContentType,

    @ColumnInfo(name = "episode_name")
    val episodeName: String?,

    @ColumnInfo(name = "season")
    val season: Int?,

    @ColumnInfo(name = "episode")
    val episode: Int?,

    @ColumnInfo(name = "episode_data")
    val episodeData: String?,

    @ColumnInfo(name = "watch_position_ms")
    val watchPositionMs: Long = 0L,

    @ColumnInfo(name = "total_duration_ms")
    val totalDurationMs: Long = 0L,

    @ColumnInfo(name = "last_watched_at")
    val lastWatchedAt: Long,

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
