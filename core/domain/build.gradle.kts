/**
 * =============================================================================
 * :core:domain
 *
 * Pure Kotlin shared domain module.
 *
 * Responsibilities:
 * - Defines the core domain model for DreamStream:
 *   - Catalog and content types (movies, shows, episodes, seasons, qualities).
 *   - Detail models (content detail hierarchies, show status, metadata).
 *   - Media models (stream links, subtitles, tracks).
 *   - Plugin contracts and manifests used by the plugin system.
 *   - Shared error hierarchy for domain and data layers.
 * - Provides the typed Result wrapper, Error contract, EmptyResult alias, and
 *   result/extension helpers used across repositories and features.
 * - Exposes repository interfaces and other domain contracts that the data and
 *   presentation layers depend on.
 *
 * Rules:
 * - MUST NOT depend on Android, Compose, Ktor, Room, or any UI/IO framework.
 * - Contains only pure Kotlin types and logic that can be reused on every
 *   platform.
 * - Domain models that need serialization are annotated with @Serializable.
 *
 * Dependency direction:
 * - Feature modules and data modules depend on :core:domain.
 * - :core:domain never depends on feature, data, or platform-specific modules.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.domain"
    }
    sourceSets{
        commonMain.dependencies {
            api(libs.okio)
        }
    }
}
