package com.dreamstream.core.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "Repositories")
data class RepositoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "url")
    val url: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "description")
    val description: String?,

    @ColumnInfo(name = "manifest_version")
    val manifestVersion: Int = 1,

    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = true,

    @ColumnInfo(name = "last_fetched")
    val lastFetched: Long?,

    @ColumnInfo(name = "added_at")
    val addedAt: Long
)
