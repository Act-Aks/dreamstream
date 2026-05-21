package com.dreamstream.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Enable Android `buildFeatures.compose` if this module is an Android app
 * or AGP-library module. For pure-JVM/desktop modules this is a no-op.
 */
internal fun Project.configureComposeBuildFeature() {
    plugins.withId("com.android.application") {
        extensions.getByType<ApplicationExtension>().buildFeatures.compose = true
    }
    plugins.withId("com.android.library") {
        extensions.getByType<LibraryExtension>().buildFeatures.compose = true
    }
}

internal fun Project.configureComposeCompiler() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        val metricsEnabled =
            providers.gradleProperty("dreamstream.enableComposeMetrics").map { it.toBoolean() }.orElse(false)

        if (metricsEnabled.get()) {
            metricsDestination.set(layout.buildDirectory.dir("compose-metrics"))
            reportsDestination.set(layout.buildDirectory.dir("compose-reports"))
        }

        val stabilityConfig = rootProject.file("config/compose/stability-config.conf")
        if (stabilityConfig.exists()) {
            stabilityConfigurationFiles.add(
                layout.projectDirectory.file(stabilityConfig.absolutePath)
            )
        }
    }
}

internal fun Project.configureComposeDependencies() {
    extensions.configure<KotlinMultiplatformExtension> {

        sourceSets.apply {
            commonMain.dependencies {
                implementation(lib("compose-runtime").get())
                implementation(lib("compose-foundation").get())
                implementation(lib("compose-material3").get())
                implementation(lib("compose-material-icons-extended").get())
                implementation(lib("compose-material-icons-core").get())
                implementation(lib("compose-ui").get())
                implementation(lib("compose-ui-graphics").get())
                implementation(lib("compose-ui-tooling").get())
                implementation(lib("compose-ui-tooling-preview").get())
                implementation(lib("compose-components-resources").get())
                bundle("lifecycle-compose").get().forEach { implementation(it) }
            }
            findByName("desktopMain")?.dependencies {
                implementation(lib("compose-desktop-jvm").get())
            }
        }
    }
}
