package com.dreamstream.convention

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures shared Kotlin Multiplatform compiler opts. Targets (android, jvm)
 * are set up separately via [configureAndroidTarget] / [configureDesktopTarget]
 * so individual convention plugins can pick the subset they need.
 *
 * iOS/native targets are intentionally NOT configured here for DreamStream's
 * initial scope (Android + desktop only). Add them later when needed.
 */
internal fun Project.configureKotlinMultiplatformCompilerOpts(
    extension: KotlinMultiplatformExtension
) {
    with(extension) {
        compilerOptions {
            freeCompilerArgs.add("-Xexpect-actual-classes")
            optIn.add("kotlin.RequiresOptIn")
            optIn.add("kotlin.time.ExperimentalTime")
        }
    }
    configureKotlinCompile()
}
