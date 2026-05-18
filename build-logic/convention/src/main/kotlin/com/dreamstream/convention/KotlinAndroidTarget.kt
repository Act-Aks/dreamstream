package com.dreamstream.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures Android target for APPLICATION modules using com.android.application plugin.
 * Uses the traditional androidTarget {} DSL.
 */
internal fun Project.configureAndroidTarget(
    extension: KotlinMultiplatformExtension,
) {
    with(extension) {
        androidTarget {
            compilerOptions {
                jvmTarget.set(project.jvmTarget)
            }
        }
    }
}

