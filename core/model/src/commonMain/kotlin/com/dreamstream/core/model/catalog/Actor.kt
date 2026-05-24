package com.dreamstream.core.model.catalog

import kotlinx.serialization.Serializable

/** A cast or crew member associated with a piece of content. */
@Serializable
data class Actor(
    val name: String,
    val image: String? = null,
    val role: String? = null,
)
