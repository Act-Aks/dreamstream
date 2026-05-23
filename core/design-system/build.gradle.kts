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
}

kotlin {
    android {
        namespace = "com.dreamstream.core.designsystem"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.presentation)

                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor3)

                // Glassmorphism — blur/frosted-glass visual effects.
                // haze provides hazeSource / hazeChild modifiers and HazeStyle.
                // haze-materials provides HazeMaterials presets (adaptive light/dark).
                // Exposed as `api` so feature modules that use HazeState with glass
                // components don't need to declare haze as a direct dependency.
                api(libs.haze)
                implementation(libs.haze.materials)
            }
        }
    }
}

compose.resources {
    publicResClass = true
    packageOfResClass = "com.dreamstream.core.designsystem"
}
