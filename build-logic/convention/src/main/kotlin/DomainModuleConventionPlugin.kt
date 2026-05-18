import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.configureAndroidTarget
import com.dreamstream.convention.configureDesktopTarget
import com.dreamstream.convention.configureKotlinMultiplatformCompilerOpts
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for pure-Kotlin domain modules.
 *
 * Use for every module under :feature:<name>:domain and for :core:domain.
 * These modules MUST NOT depend on Android, Compose, Ktor, Room, or any
 * framework: they hold pure business models, repository interfaces, errors,
 * Result types, and (when justified) use cases.
 *
 * Targets: Android + desktop JVM only. iOS/native intentionally omitted for
 * DreamStream's initial scope.
 */
class DomainModuleConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "kotlin-multiplatform",
                "android-kotlin-multiplatform-library",
                "dreamstream-detekt",
                "dreamstream-testing",
            )

            extensions.configure<KotlinMultiplatformExtension> {
                configureAndroidTarget(this)
                configureDesktopTarget(this)

                sourceSets.getByName("commonMain").dependencies {
                    implementation(lib("kotlinx-coroutines-core").get())
                    implementation(lib("kotlinx-datetime").get())
                    implementation(lib("kermit").get())
                }
            }

            configureKotlinMultiplatformCompilerOpts()
        }
    }
}
