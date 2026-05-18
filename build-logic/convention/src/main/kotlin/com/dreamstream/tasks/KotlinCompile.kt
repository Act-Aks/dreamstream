package com.dreamstream.tasks

import com.dreamstream.convention.jvmTarget
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Configures Kotlin compilation defaults for all Kotlin compile tasks in the project.
 *
 * This sets the shared JVM target, optionally enables warnings as errors,
 * and adds compiler flags for coroutines and opt-in APIs.
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
                "-opt-in=kotlin.RequiresOptIn"
            )
        }
    }
}