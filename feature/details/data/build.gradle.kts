/**
 * =============================================================================
 * :feature:details:data
 *
 * Data layer for the details feature. Provides the DetailsRepository
 * implementation. Currently backed by in-memory hardcoded data; replaced
 * by a real network/database source once a content API is integrated.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.koin)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.details.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                // Exposed as api: InMemoryDetailsRepository implements DetailsRepository
                // and DetailsError, so app:shared needs these on its compile classpath.
                api(projects.core.domain)
                api(projects.feature.details.domain)
            }
        }
    }
}
