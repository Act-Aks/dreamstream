/**
 * =============================================================================
 * :core:domain
 *
 * Pure-Kotlin shared domain module. Holds primitives reused across every
 * feature: Result wrapper, Error contract, EmptyResult alias, shared error
 * types (DataError), and result/extension helpers.
 *
 * MUST NOT depend on Android, Compose, Ktor, Room, or any framework.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.domain"
    }
}
