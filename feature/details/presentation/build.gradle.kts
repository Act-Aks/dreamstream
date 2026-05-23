/**
 * =============================================================================
 * :feature:details:presentation
 *
 * Presentation layer for the content details feature. Contains the ViewModel,
 * Compose UI, domain→UI mappers, navigation route, and Koin DI module.
 * Depends on :feature:details:domain for contracts. Never depends on
 * :feature:details:data or any other feature module.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.feature)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.details.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.details.domain)
            }
        }
    }
}
