/**
 * =============================================================================
 * :feature:search:domain
 *
 * Pure-Kotlin domain layer for the search feature. Holds the repository
 * contract and typed errors. No Android, Compose, Ktor, Room, or framework
 * imports are allowed here.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.search.domain"
    }

    sourceSets {
        commonMain {
            dependencies {
                // api: SearchResult is part of SearchRepository's public API so
                // data and presentation layers see core:model transitively.
                api(projects.core.model)
            }
        }
    }
}
