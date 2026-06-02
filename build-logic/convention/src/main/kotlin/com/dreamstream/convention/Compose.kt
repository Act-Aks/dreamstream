package com.dreamstream.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal fun Project.configureComposeCompiler() {
    extensions.configure<ComposeCompilerGradlePluginExtension> {
        val metricsEnabled =
            providers.gradleProperty("dreamstream.enableComposeMetrics").map { it.toBoolean() }
                .orElse(false)

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
                // Exposed as api so feature/presentation modules can access string resources
                // from the compose-components-resources artifact without re-declaring it.
                api(lib("compose-components-resources").get())
                // Lifecycle deps (runtime, viewmodel, etc.) are provided transitively by
                // Compose Multiplatform. Adding them explicitly caused duplicate-class
                // conflicts between androidx.lifecycle and org.jetbrains.androidx.lifecycle
                // artifacts on the desktopMain classpath (LocalViewModelStoreOwner clash).
            }
            findByName("desktopMain")?.dependencies {
                implementation(lib("compose-desktop-jvm").get())
            }
        }
    }
}
