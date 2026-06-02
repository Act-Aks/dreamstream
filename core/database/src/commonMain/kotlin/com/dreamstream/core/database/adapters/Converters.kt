package com.dreamstream.core.database.adapters

import androidx.room3.TypeConverter
import com.dreamstream.core.model.catalog.ContentType
import kotlinx.serialization.json.Json

class ContentTypeConverter {
    @TypeConverter
    fun fromContentType(value: ContentType): String = value.name

    @TypeConverter
    fun toContentType(value: String): ContentType =
        runCatching { ContentType.valueOf(value) }.getOrDefault(ContentType.Movie)
}

class StringListConverter {
    @TypeConverter
    fun fromStringList(value: List<String>): String = Json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String): List<String> =
        runCatching { Json.decodeFromString<List<String>>(value) }.getOrDefault(emptyList())
}
