/**
 * =============================================================================
 * :core:model
 *
 * Shared domain model library. Holds all media types, content types, quality
 * enumerations, plugin manifests, stream/subtitle metadata, and the rich error
 * hierarchy (DreamError) used across the entire app and plugin system.
 *
 * MUST NOT depend on Android, Compose, Ktor, Room, or any UI framework.
 * All models that need serialization carry @Serializable.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.model"
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.domain)
            }
        }
    }
}
