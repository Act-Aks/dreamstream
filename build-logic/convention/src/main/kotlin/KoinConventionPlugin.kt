import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Koin convention plugin.
 *
 * - KMP modules: wires koin-core + koin-compose + koin-compose-viewmodel into
 *   `commonMain` (CMP-friendly), and koin-android into `androidMain` for
 *   Android-specific scoping. koin-test into `commonTest`.
 * - Android-only modules (`:app`, `:tvApp`): wires the same set as
 *   `implementation`. No koin-androidx-compose — koin-compose-viewmodel covers
 *   ViewModel integration across CMP.
 *
 * Apply on every module that declares or uses Koin definitions.
 */
class KoinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val isKmp = extensions.findByType(KotlinMultiplatformExtension::class.java) != null
            if (isKmp) {
                wireKmpKoin()
            } else {
                wireAndroidKoin()
            }
        }
    }

    private fun Project.wireKmpKoin() {
        extensions.configure(KotlinMultiplatformExtension::class.java) {
            val bom: Provider<MinimalExternalModuleDependency> = lib("koin-bom")

            sourceSets.getByName("commonMain").dependencies {
                implementation(project.dependencies.platform(bom))
                implementation(lib("koin-core").get())
                implementation(lib("koin-compose").get())
                implementation(lib("koin-compose-viewmodel").get())
            }
            sourceSets.findByName("androidMain")?.dependencies {
                implementation(project.dependencies.platform(bom))
                implementation(lib("koin-android").get())
            }
            sourceSets.findByName("commonTest")?.dependencies {
                implementation(project.dependencies.platform(bom))
                implementation(lib("koin-test").get())
            }
            sourceSets.findByName("androidUnitTest")?.dependencies {
                implementation(lib("koin-test-junit5").get())
            }
            sourceSets.findByName("desktopTest")?.dependencies {
                implementation(lib("koin-test-junit5").get())
            }
        }
    }

    private fun Project.wireAndroidKoin() {
        dependencies {
            val bom = platform(lib("koin-bom"))
            "implementation"(bom)
            "implementation"(lib("koin-core"))
            "implementation"(lib("koin-android"))
            "implementation"(lib("koin-compose"))
            "implementation"(lib("koin-compose-viewmodel"))

            "testImplementation"(bom)
            "testImplementation"(lib("koin-test"))
            "testImplementation"(lib("koin-test-junit5"))
        }
    }
}
