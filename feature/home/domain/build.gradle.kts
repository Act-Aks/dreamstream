/**
 * =============================================================================
 * :feature:home:domain
 *
 * Pure-Kotlin domain module for the home feature. Holds domain models
 * (Content, ContentType, HomeSection), repository contracts, and
 * home-specific error types.
 *
 * MUST NOT depend on Android, Compose, Ktor, Room, or any framework.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.home.domain"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
            }
        }
    }
}
