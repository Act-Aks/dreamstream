/**
 * =============================================================================
 * Root build file. Should be nearly empty — all real config lives in
 * build-logic convention plugins or in module-level build files.
 * Only cross-project tasks (clean, dependency updates, detekt aggregation)
 * belong here.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.ksp) apply false
}