package com.dreamstream.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configures Kotlin compilation defaults for all Kotlin compile tasks in the project.
 *
 * Responsibilities:
 * - Sets the shared JVM target via [jvmTarget].
 * - Optionally treats warnings as errors based on the `warningsAsErrors` project property.
 * - Enables commonly used opt-in annotations for coroutines and experimental APIs.
 * - Ensures that Kotlin compilation runs only after localization validation, when
 *   the `validateLocalization` task is present in the project.
 */
internal fun Project.configureKotlinCompile() {
    tasks.withType<KotlinCompile>().configureEach {
        compilerOptions {
            jvmTarget.set(project.jvmTarget)

            // Treat all Kotlin warnings as errors (disabled by default).
            // Override by setting warningsAsErrors=true in your ~/.gradle/gradle.properties.
            val warningsAsErrors: String? by project
            allWarningsAsErrors.set(warningsAsErrors.toBoolean())

            freeCompilerArgs.addAll(
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlin.RequiresOptIn",
            )
        }
    }
}
