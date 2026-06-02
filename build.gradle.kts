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

    // ── Build analysis (applied here — each plugin autoconfigures all subprojects) ──
    alias(libs.plugins.dependency.analysis)
    alias(libs.plugins.module.graph)
    alias(libs.plugins.module.graph.assert)
}

// ── Lifecycle version alignment ────────────────────────────────────────────
// Koin 4.2.x declares a constraint that upgrades all androidx.lifecycle:*
// artifacts to 2.11.0-beta01.  Compose Multiplatform ships the equivalent
// JetBrains-repackaged fork (org.jetbrains.androidx.lifecycle) at an older
// version, causing duplicate-class warnings on the desktopMain classpath
// (e.g. LocalViewModelStoreOwner from both groups simultaneously).
// Forcing the entire androidx.lifecycle group to our pinned version keeps
// the two artefact trees in sync and eliminates the runtime ambiguity.
subprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "androidx.lifecycle") {
                useVersion(rootProject.libs.versions.compose.lifecycle.get())
                because(
                    "Pin androidx.lifecycle to our declared version to prevent " +
                    "duplicate-class conflicts with org.jetbrains.androidx.lifecycle " +
                    "artifacts provided by Compose Multiplatform."
                )
            }
        }
    }
}

// ── Dependency Analysis ────────────────────────────────────────────────────
// Detects unused deps, wrong api/implementation configurations, and transitive
// leaks across all modules. Run: ./gradlew buildHealth
dependencyAnalysis {
    issues {
        all {
            // Warnings first — review the initial report before promoting to "fail"
            onAny { severity("warn") }
        }
    }
    structure {
        // Bundle multi-artifact groups so DAGP treats them as one logical dependency
        // and does not flag partial usage of a group as unused.
        bundle("coil") {
            includeDependency("io.coil-kt.coil3:coil")
            includeDependency("io.coil-kt.coil3:coil-compose")
            includeDependency("io.coil-kt.coil3:coil-network-ktor3")
        }
        bundle("koin") {
            includeDependency("io.insert-koin:koin-core")
            includeDependency("io.insert-koin:koin-android")
            includeDependency("io.insert-koin:koin-compose")
            includeDependency("io.insert-koin:koin-compose-viewmodel")
        }
    }
}

// ── Module Graph (Visualization) ───────────────────────────────────────────
// Generates a Mermaid dependency diagram embedded in docs/module-graph.md.
// Run: ./gradlew createModuleGraph
moduleGraphConfig {
    readmePath.set("./docs/module-graph.md")
    heading.set("## Module Dependency Graph")
}

// ── Module Graph (Architectural Enforcement) ───────────────────────────────
// Fails the build if any architectural rule from AGENTS.md is violated.
// Run: ./gradlew assertModuleGraph
moduleGraphAssert {
    maxHeight = 4             // :app → :feature:*:presentation → :feature:*:domain → :core
    assertOnAnyBuild = false  // explicit only — does not run on every incremental build
    restricted = arrayOf(
        ":feature:.* -X> :feature:.*",                     // no cross-feature deps
        ":feature:.*:data -X> :feature:.*:presentation",   // data cannot see presentation
        ":feature:.*:domain -X> :feature:.*:data",         // domain cannot see data
        ":feature:.*:domain -X> :feature:.*:presentation", // domain cannot see presentation
        ":core:domain -X> :feature:.*",                    // core:domain stays below features
        ":core:model -X> :feature:.*",                     // core:model stays below features
    )
}
