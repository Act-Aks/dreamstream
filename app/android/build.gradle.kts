/**
 * =============================================================================
 * :app:android
 *
 * Android application entry point for DreamStream. Bootstraps the Koin DI
 * graph, sets up the Compose UI host, and wires all feature nav graphs into
 * the single NavDisplay.
 *
 * Intentionally Android-only. Shared cross-platform code lives in KMP modules
 * under :core and :feature. This module's job is to be the Android shell.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.android.app)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.dreamstream.koin)
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation)
    implementation(projects.core.designSystem)
    implementation(projects.feature.home.domain)
    implementation(projects.feature.home.data)
    implementation(projects.feature.home.presentation)
    implementation(projects.feature.details.domain)
    implementation(projects.feature.details.data)
    implementation(projects.feature.details.presentation)
}
