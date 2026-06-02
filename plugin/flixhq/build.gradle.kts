/**
 * =============================================================================
 * :plugin:flixhq
 *
 * Bundled DreamStream plugin for FlixHQ (https://flixhq.to).
 * Provides movies and TV shows via HTML scraping using Ksoup.
 *
 * Depends on :core:plugin-api (which provides Ktor, Ksoup, serialization,
 * coroutines, and :core:model). No Android, Compose, or DI framework imports.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.domain)
}

kotlin {
    android {
        namespace = "com.dreamstream.plugin.flixhq"
    }
    sourceSets {
        commonMain.dependencies {
            // core:plugin-api exposes Ktor, Ksoup, kotlinx-serialization, kotlinx-coroutines,
            // and core:model — all as api dependencies. No further deps needed here.
            implementation(projects.core.pluginApi)
        }
        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}
