package com.dreamstream.convention

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension

/**
 * Configures Kotlin JVM toolchain from version catalog.
 * Usage: configureKotlinJvmToolchain()
 */
internal fun Project.configureKotlinJvm() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = project.javaVersion
        targetCompatibility = project.javaVersion
    }
    extensions.configure<KotlinJvmProjectExtension> {
        compilerOptions { jvmTarget.set(project.jvmTarget) }
    }
    configureKotlinCompile()
}
