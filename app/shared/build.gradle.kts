/**
 * =============================================================================
 * :app:shared
 *
 * Wires all feature nav graphs into the single NavDisplay.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.compose)
    alias(libs.plugins.dreamstream.koin)
}

kotlin {
    android {
        namespace = "com.dreamstream.shared"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.navigation3.ui)

            implementation(projects.core.domain)
            implementation(projects.core.presentation)
            implementation(projects.core.designSystem)

            // Plugin infrastructure: runtime + first bundled plugin
            implementation(projects.core.data)
            implementation(projects.core.pluginRuntime)
            implementation(projects.plugin.flixhq)

            implementation(projects.feature.home.domain)
            implementation(projects.feature.home.data)
            implementation(projects.feature.home.presentation)
            implementation(projects.feature.details.domain)
            implementation(projects.feature.details.data)
            implementation(projects.feature.details.presentation)
            implementation(projects.feature.search.domain)
            implementation(projects.feature.search.data)
            implementation(projects.feature.search.presentation)
            implementation(projects.feature.settings.domain)
            implementation(projects.feature.settings.data)
            implementation(projects.feature.settings.presentation)
        }
    }
}
