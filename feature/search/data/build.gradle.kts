/**
 * =============================================================================
 * :feature:search:data
 *
 * Data layer for the search feature. Provides the SearchRepository
 * implementation. Uses PluginSearchRepository backed by PluginRegistry,
 * which fans out search queries to all loaded content provider plugins.
 *
 * InMemorySearchRepository is kept as a standalone class for unit tests.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.koin)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.search.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.domain)
                api(projects.feature.search.domain)
                implementation(projects.core.pluginRuntime)
            }
        }
    }
}
