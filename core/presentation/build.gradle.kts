/**
 * =============================================================================
 * :core:presentation
 *
 * Shared presentation primitives used by every feature presentation module:
 *  - UiText: resource-or-dynamic string wrapper that resolves under Compose.
 *  - ObserveAsEvents: lifecycle-aware one-shot event observer.
 *  - DataError.toUiText(): shared error -> user-facing string mapper.
 *
 * Depends on Compose Multiplatform (runtime + resources + lifecycle) and
 * :core:domain. Does NOT depend on any feature or on the data layer.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.compose)
}

val moduleName = "com.dreamstream.core.presentation"

kotlin {
    android {
        namespace = moduleName
    }

    sourceSets.commonMain.dependencies {
        implementation(projects.core.domain)
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "$moduleName.resources"
}
