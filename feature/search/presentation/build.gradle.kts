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

val moduleName = "com.dreamstream.feature.search.presentation"

kotlin {
    android {
        namespace = moduleName
    }

    sourceSets {
        commonMain {
            dependencies {
                // Exposed as api: SearchState and SearchRoute reference domain types
                // (SearchError) that callers such as app:shared need to see.
                api(projects.feature.search.domain)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "$moduleName.resources"
}
