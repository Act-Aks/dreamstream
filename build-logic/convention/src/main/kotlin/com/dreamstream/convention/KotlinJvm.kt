package com.dreamstream.convention

import com.dreamstream.tasks.configureKotlinCompile
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmExtension

/**
 * Configures Kotlin JVM toolchain from version catalog.
 * Usage: configureKotlinJvmToolchain()
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<KotlinJvmExtension> {
        jvmToolchain(projectJavaVersion)
    }
    configureKotlinCompile()
}