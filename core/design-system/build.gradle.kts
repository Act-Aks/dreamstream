/**
 * =============================================================================
 * :core:design-system
 *
 * DreamStream's shared visual language: color tokens, typography, shapes,
 * the root [DreamStreamTheme] composable, and reusable design-system
 * components (added incrementally as features need them).
 *
 * Depends on Compose Multiplatform only. Does not depend on :core:domain or
 * :core:presentation — design-system code should be visually self-contained.
 * =============================================================================
 */

plugins {
    alias(libs.plugins.dreamstream.compose)
    alias(libs.plugins.dreamstream.kmp.library)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.designsystem"
    }
}
