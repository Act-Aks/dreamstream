import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.lib
import com.dreamstream.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

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
                "dreamstream-kmp-library",
                "dreamstream-serialization",
            )

            dependencies {
                "commonMainImplementation"(lib("kotlin-stdlib").get())
                "commonMainImplementation"(lib("kotlinx-coroutines-core").get())
                "commonMainImplementation"(lib("kotlinx-datetime").get())
                "commonMainImplementation"(lib("kermit").get())
            }
        }
    }
}
