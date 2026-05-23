/**
 * =============================================================================
 * :feature:details:domain
 *
 * Pure-Kotlin domain layer for the content details feature. Holds models,
 * repository contract, and typed errors. No Android, Compose, or framework
 * imports are allowed here.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.details.domain"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
            }
        }
    }
}
