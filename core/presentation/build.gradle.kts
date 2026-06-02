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
        // Exposed as api: UiText, ObserveAsEvents, and DataErrorUiText.toUiText() surface
        // Result/Error/DataError types from core:domain and model types from core:model
        // in their signatures. Consumers must see these on their compile classpath.
        api(projects.core.domain)
        api(projects.core.model)
        // AppRoute implements NavKey; expose navigation3 so callers can use it as a route type.
        api(libs.compose.navigation3.ui)
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "$moduleName.resources"
    nameOfResClass = "CoreRes"
}
