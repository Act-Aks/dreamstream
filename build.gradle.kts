/**
 * =============================================================================
 * Root build file. Should be nearly empty — all real config lives in
 * build-logic convention plugins or in module-level build files.
 * Only cross-project tasks (clean, dependency updates, detekt aggregation)
 * belong here.
 * =============================================================================
 */
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.room) apply false
}
