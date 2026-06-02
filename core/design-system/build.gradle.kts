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

val moduleName = "com.dreamstream.core.designsystem"

kotlin {
    android {
        namespace = moduleName
    }

    sourceSets {
        commonMain {
            dependencies {
                // Exposed as api: design-system components use UiText and NavigationItem
                // from core:presentation in their public composable signatures.
                api(projects.core.presentation)

                // Glassmorphism — blur/frosted-glass visual effects.
                // haze provides hazeSource / hazeChild modifiers and HazeStyle.
                // Exposed as `api` so feature modules that use HazeState with glass
                // components don't need to declare haze as a direct dependency.
                api(libs.haze)
                // haze-materials (HazeMaterials presets) and coil are intentionally
                // NOT added here — neither is imported in the design-system source.
                // Feature modules that need coil get it through the feature plugin.
            }
        }
    }
}

compose.resources {
    publicResClass = false
    packageOfResClass = "$moduleName.resources"
}
