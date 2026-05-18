package com.dreamstream.convention

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Configures the JVM target for the Kotlin Multiplatform JVM source set.
 *
 * Applies the shared [jvmTarget] value to all Kotlin JVM compilation tasks
 * in the multiplatform project.
 */
internal fun Project.configureDesktopTarget(
    extension: KotlinMultiplatformExtension,
) {
    with(extension) {
        jvm("desktop") {
            compilations.all {
                compileTaskProvider.configure {
                    compilerOptions {
                        jvmTarget.set(project.jvmTarget)
                    }
                }
            }
        }
    }
}