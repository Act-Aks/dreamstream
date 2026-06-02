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

val moduleName = "com.dreamstream.feature.details.presentation"

kotlin {
    android {
        namespace = moduleName
    }

    sourceSets {
        commonMain {
            dependencies {
                // Exposed as api: DetailsState and DetailsRoute reference domain types
                // (DetailContent, DetailsError) that callers such as app:shared need to see.
                api(projects.feature.details.domain)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "$moduleName.resources"
}
