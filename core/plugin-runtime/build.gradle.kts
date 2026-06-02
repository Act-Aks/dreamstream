/**
 * =============================================================================
 * :core:plugin-runtime
 *
 * Plugin host infrastructure: loads DreamPlugin instances from various sources
 * (bundled Gradle modules, APK files on Android), initialises them with a shared
 * PluginContext, and exposes a PluginRegistry for feature repositories to query.
 *
 * Depends on :core:plugin-api (which transitively provides Ktor, Ksoup,
 * kotlinx-coroutines, and :core:model). No Android, Compose, or Room.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.koin)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.plugin.runtime"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.pluginApi)
        }
        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}
