/**
 * =============================================================================
 * :feature:search:presentation
 *
 * Presentation layer for the search feature. Contains the ViewModel, Compose
 * UI, domain→UI mappers, navigation route, and Koin DI module. Depends on
 * :feature:search:domain for contracts. Never depends on :feature:search:data
 * or any other feature module.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.feature)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.search.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.search.domain)
            }
        }
    }
}
