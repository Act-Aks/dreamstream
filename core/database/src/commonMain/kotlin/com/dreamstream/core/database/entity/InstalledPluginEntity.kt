package com.dreamstream.core.database.entity

import androidx.room3.ColumnInfo
import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "InstalledPlugins")
data class InstalledPluginEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "version")
    val version: Int,

    @ColumnInfo(name = "version_name")
    val versionName: String,

    @ColumnInfo(name = "description")
    val description: String = "",

    @ColumnInfo(name = "authors")
    val authors: String = "[]",

    @ColumnInfo(name = "icon_url")
    val iconUrl: String?,

    @ColumnInfo(name = "language")
    val language: String = "en",

    @ColumnInfo(name = "content_types")
    val contentTypes: String = "[]",

    @ColumnInfo(name = "file_path")
    val filePath: String,

    @ColumnInfo(name = "repository_url")
    val repositoryUrl: String,

    @ColumnInfo(name = "requires_app_version")
    val requiresAppVersion: Int = 1,

    @ColumnInfo(name = "is_enabled")
    val isEnabled: Boolean = true,

    @ColumnInfo(name = "is_adult")
    val isAdult: Boolean = false,

    @ColumnInfo(name = "installed_at")
    val installedAt: Long,

    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
)
