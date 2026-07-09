/**
 * =============================================================================
 * :plugin:movieboxonline
 *
 * Bundled DreamStream plugin for MovieBox Online (https://movieboxonline.org).
 * Provides movies and TV series via HTML scraping (Ksoup) with Nuxt SSR payload
 * parsing for poster images.
 *
 * Depends on :core:plugin-api (which provides Ktor, Ksoup, serialization,
 * coroutines, and :core:domain). No Android, Compose, or DI framework imports.
 *
 * DSP packaging
 * ─────────────
 * Run `./gradlew :plugin:movieboxonline:buildDsp` to produce both artefacts:
 *   build/dsp/movieboxonline-desktop.dsp   ← URLClassLoader-loadable ZIP (JVM)
 *   build/dsp/movieboxonline-android.dsp   ← DexClassLoader-loadable ZIP (DEX)
 * Each .dsp is accompanied by a .sha256 checksum file.
 * =============================================================================
 */
plugins {
    alias(libs.plugins.dreamstream.kmp.library)
}

kotlin {
    android {
        namespace = "com.dreamstream.plugin.movieboxonline"
    }
    sourceSets {
        commonMain.dependencies {
            // core:plugin-api exposes Ktor, Ksoup, kotlinx-serialization, kotlinx-coroutines,
            // and core:domain — all as api dependencies. No further deps needed here.
            implementation(projects.core.pluginApi)
        }
        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}
