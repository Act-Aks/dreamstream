package com.dreamstream.core.domain.util

/**
 * Marker contract for every typed error in the app.
 *
 * Implement this on enums or sealed hierarchies that describe a finite set of
 * failure cases the caller is expected to react to. Domain, data, presentation
 * and validation code all funnel their typed failures through [Error] so they
 * can flow uniformly through [Result].
 *
 * `Error` is intentionally empty: there is no shared state across error types.
 * Each implementer owns its own discriminator (enum constant, sealed subclass).
 */
interface Error
