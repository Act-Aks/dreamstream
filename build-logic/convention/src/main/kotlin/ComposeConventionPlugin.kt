import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.LibraryExtension
import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.bundle
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Compose Multiplatform convention plugin.
 *
 * DreamStream uses **Compose Multiplatform** (org.jetbrains.compose), NOT
 * AndroidX Compose. All Compose runtime / material / ui / lifecycle /
 * navigation deps are sourced from the JetBrains forks
 * (`org.jetbrains.compose.*` and `org.jetbrains.androidx.*`) and wired into
 * `commonMain` so they work on Android, desktop, and any future KMP target.
 *
 * Responsibilities:
 *  - Applies the Compose compiler + Compose Multiplatform plugins.
 *  - Enables Android `buildFeatures.compose` when an Android extension is present.
 *  - Honours `dreamstream.enableComposeMetrics` (gradle.properties) and writes
 *    compiler reports + metrics into `build/compose-{reports,metrics}/`.
 *  - Wires the CMP runtime, foundation, material3, icons, resources, and
 *    UI-tooling preview into `commonMain`.
 *  - Wires the JetBrains lifecycle (runtime + viewmodel) bundle into
 *    `commonMain` so ViewModel + lifecycle-aware composables work cross-platform.
 *  - Adds `compose.desktop.currentOs` to `desktopMain`.
 *  - Adds `compose.uiTooling` as a `debugImplementation` for Android-only modules
 *    (e.g. the `:app` entry) so Android Studio previews render.
 *
 * Apply on every module that contains `@Composable` code.
 */
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "compose-compiler",
                "compose-multiplatform",
            )

            extensions.configure<ComposeCompilerGradlePluginExtension> {
                val metricsEnabled = providers
                    .gradleProperty("dreamstream.enableComposeMetrics")
                    .map { it.toBoolean() }
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

            enableComposeBuildFeature()
            wireComposeDependencies()
        }
    }

    /**
     * Enable Android `buildFeatures.compose` if this module is an Android app
     * or AGP-library module. For pure-JVM/desktop modules this is a no-op.
     */
    private fun Project.enableComposeBuildFeature() {
        plugins.withId("com.android.application") {
            extensions.getByType<ApplicationExtension>().buildFeatures.compose = true
        }
        plugins.withId("com.android.library") {
            extensions.getByType<LibraryExtension>().buildFeatures.compose = true
        }
    }

    private fun Project.wireComposeDependencies() {
        val compose = extensions.getByType<ComposeExtension>().dependencies

        val isKmp = extensions.findByType(KotlinMultiplatformExtension::class.java) != null
        if (isKmp) {
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                sourceSets.getByName("commonMain").dependencies {
                    implementation(compose.runtime)
                    implementation(compose.foundation)
                    implementation(compose.material3)
                    implementation(compose.materialIconsExtended)
                    implementation(compose.ui)
                    implementation(compose.components.resources)
                    implementation(compose.components.uiToolingPreview)
                    implementation(bundle("jb-lifecycle-compose").get())
                }
                sourceSets.findByName("desktopMain")?.dependencies {
                    implementation(compose.desktop.currentOs)
                }
            }
        } else {
            // Android-only Compose module (e.g. :app, :tvApp). The KMP libraries
            // already bring CMP runtime in transitively; this branch only adds
            // what's needed to host them at the Android entry point.
            dependencies {
                "implementation"(compose.runtime)
                "implementation"(compose.foundation)
                "implementation"(compose.material3)
                "implementation"(compose.ui)
                "implementation"(compose.components.resources)
                "debugImplementation"(compose.uiTooling)
                "debugImplementation"(compose.components.uiToolingPreview)
            }
        }
    }
}
