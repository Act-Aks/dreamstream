/**
 * =============================================================================
 * :feature:home:data
 *
 * Data layer for the home feature. Provides repository implementations that
 * back the domain contracts. Uses PluginHomeRepository backed by PluginRegistry,
 * which fans out home page requests to all loaded content provider plugins.
 *
 * InMemoryHomeRepository is kept as a standalone class for unit tests.
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
                api(projects.core.domain)
                api(projects.feature.home.domain)
                implementation(projects.core.pluginRuntime)
            }
        }
    }
}
