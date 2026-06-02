package com.dreamstream.core.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.Index
import androidx.room3.PrimaryKey
import com.dreamstream.core.model.catalog.ContentType

@Entity(
    tableName = "Bookmarks",
    indices = [
        Index(value = ["provider_id"]),
        Index(value = ["category"]),
        Index(value = ["url", "provider_id"], unique = true)
    ]
)
data class BookmarkEntity(
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

    @ColumnInfo(name = "category")
    val category: String = "Watchlist",

    @ColumnInfo(name = "created_at")
    val createdAt: Long
)
