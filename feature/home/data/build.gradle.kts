/**
 * =============================================================================
 * :feature:home:data
 *
 * Data layer for the home feature. Provides repository implementations that
 * back the domain contracts. Currently uses an in-memory fake; real network
 * data sources will be added once a content API is integrated.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.koin)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.home.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.feature.home.domain)
            }
        }
    }
}
