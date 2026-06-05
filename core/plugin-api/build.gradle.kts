/**
 * =============================================================================
 * :core:pluginapi
 *
 * Shared domain-layer plugin API module. Provides core primitives and
 * framework-agnostic utilities for feature modules: Result wrapper, Error
 * contract, EmptyResult alias, shared error types (DataError), and result/
 * extension helpers. Exposes essential multiplatform dependencies as `api`
 * so dependent feature modules inherit them without transitive declarations.
 *
 * Includes:
 * - kotlinx.coroutines.core      → Async/await and coroutine support
 * - kotlinx.serialization.json   → JSON serialization/deserialization
 * - kotlinx.datetime             → Cross-platform date/time handling
 * - ktor.client.core             → Ktor HTTP client core (v3.5.0)
 * - ktor.client.content.negotiation → Content negotiation for Ktor
 * - ktor.serialization.kotlinx.json → Ktor JSON serialization integration
 * - ksoup                        → Kotlin Multiplatform HTML/XML parser (jsoup port)
 *
 * MUST NOT depend on Android, Compose, Room, or any UI-specific framework.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.pluginapi"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.kotlinx.coroutines.core)
            api(libs.kotlinx.datetime)
            api(libs.kotlinx.serialization.json)
            api(libs.ksoup)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.client.core)
            api(libs.ktor.serialization.kotlinx.json)
            api(projects.core.domain)
        }
    }
}
