package com.dreamstream.core.domain.model.catalog

import kotlinx.serialization.Serializable

/**
 * A cast or crew member associated with a piece of content.
 *
 * [Actor] represents a person involved in the creation or performance of
 * a movie, TV series, anime, or live channel. It is used in content detail models
 * to display the cast and crew list on the detail screen.
 *
 * This class supports live actors, voice actors (seiyū), and crew members
 * (directors, producers, screenwriters, etc.).
 *
 * ## Key Properties:
 * - [name]: The person's full name (***required***)
 * - [image]: URL to the profile photo (***optional***)
 * - [role]: Character name or crew role (***optional***)
 *
 * ## Usage:
 * ```kotlin
 * val actor = Actor(
 *     name = "Atsushi Tamura",
 *     role = "Gon Freecss",
 *     image = "https://example.com/actors/tamura.jpg"
 * )
 * ```
 *
 * ## In Content Detail:
 * Actors are included in the cast list returned by provider plugins:
 * ```kotlin
 * MovieDetail(
 *     name = "Dune: Part Two",
 *     actors = listOf(
 *         Actor("Timothée Chalamet", role = "Paul Atreides"),
 *         Actor("Zendaya", role = "Chani"),
 *         Actor("Denis Villeneuve", role = "Director")
 *     )
 * )
 * ```
 *
 * ## UI Representation:
 * - Displayed in a horizontal scrollable cast list
 * - [image] shown as round avatar (if available)
 * - [name] as primary text, [role] as subtitle
 *
 * ## Related:
 * - Used in:
 *      [com.dreamstream.core.model.detail.ContentDetail]
 *      [com.dreamstream.core.model.detail.SeriesDetail]
 *      [com.dreamstream.core.model.detail.AnimeDetail]
 *
 * @since 1.0.0
 * @author DreamStream Team
 */
@Serializable
data class Actor(
    /**
     * The person's full name.
     *
     * ***Required***. Displayed in the cast list.
     */
    val name: String,

    /**
     * URL to the actor's profile or portrait image.
     *
     * ***Optional***. Used to display an avatar next to the name in the cast list.
     * Commonly provided by providers with media databases.
     * Can be `null` if the provider doesn't supply actor images.
     */
    val image: String? = null,

    /**
     * The character name or crew role.
     *
     * ***Optional***. For actors, this is the character name.
     * For crew, this is the job title (e.g., Director, Producer).
     *
     * Displayed as a subtitle under the actor's name in the UI.
     */
    val role: String? = null,
)
