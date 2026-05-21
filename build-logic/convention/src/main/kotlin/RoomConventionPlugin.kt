import androidx.room3.gradle.RoomExtension
import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.bundle
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.findByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Room convention plugin.
 *
 * Apply only on the `:core:database` module (single source of truth for
 * entities, DAOs, and migrations). Feature data modules depend on
 * `:core:database`; they should not apply this plugin themselves.
 *
 * Wires Room runtime + ktx + paging, the KSP compiler, and exports schemas
 * to `<module>/schemas` for version-controlled migration verification.
 *
 * Supports both KMP (`:core:database` as a KMP library) and Android-only
 * usage. For KMP, Room is wired into `commonMain` and KSP runs per target.
 */
class RoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins("room", "ksp")

            extensions.configure<RoomExtension> {
                schemaDirectory("$projectDir/schemas")
            }

            val isKmp = extensions.findByType<KotlinMultiplatformExtension>() != null
            if (isKmp) {
                extensions.configure<KotlinMultiplatformExtension> {
                    sourceSets.commonMain.dependencies {
                        implementation(lib("room-runtime").get())
                        implementation(lib("room-ktx").get())
                        implementation(lib("room-paging").get())
                    }
                    sourceSets.commonTest.dependencies {
                        implementation(lib("room-testing").get())
                    }
                }
                // KSP must be configured per Android target source set.
                dependencies {
                    add("kspAndroid", lib("room-compiler"))
                    findConfigurationName("kspDesktop")?.let { add(it, lib("room-compiler")) }
                }
            } else {
                dependencies {
                    bundle("room-runtime").get().forEach { "implementation"(it) }
                    "ksp"(lib("room-compiler").get())
                    "testImplementation"(lib("room-testing").get())
                    "androidTestImplementation"(lib("room-testing").get())
                }
            }
        }
    }

    private fun Project.findConfigurationName(name: String): String? =
        configurations.findByName(name)?.name
}
