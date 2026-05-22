/**
 * =============================================================================
 * :feature:home:presentation
 *
 * MVI presentation layer for the home feature. Contains ViewModel, screen
 * state/actions/events, composables, navigation route, and Koin module.
 *
 * Depends on :feature:home:domain for repository contracts and domain models.
 * Never depends on :feature:home:data — Koin provides the binding at runtime.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.feature)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.home.presentation"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.home.domain)
            }
        }
    }
}
