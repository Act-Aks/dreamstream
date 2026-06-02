import androidx.room3.gradle.RoomExtension
import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

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

            dependencies {
                "commonMainImplementation"(lib("androidx-room3-runtime").get())
                "commonMainImplementation"(lib("androidx-sqlite-bundled").get())
                "kspAndroid"(lib("androidx-room3-compiler").get())
                "kspDesktop"(lib("androidx-room3-compiler").get())
            }
        }
    }
}
