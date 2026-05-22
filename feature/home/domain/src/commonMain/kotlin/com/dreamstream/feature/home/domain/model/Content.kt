package com.dreamstream.feature.home.domain.model

/**
 * Unique identifier for a piece of content. Wrapping in a value class prevents
 * accidental mixing with other string IDs (e.g. provider IDs, episode IDs).
 */
@JvmInline
value class ContentId(val value: String)

/**
 * A single piece of streamable content discovered through the home feed.
 *
 * IDs carry both a content-level identifier and a source identifier so that
 * content from different providers cannot collide accidentally.
 */
data class Content(
    val id: ContentId,
    val title: String,
    val description: String,
    val thumbnailUrl: String?,
    val type: ContentType,
    val year: Int?,
    val rating: Float?,
)
