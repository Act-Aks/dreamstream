import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.withType

/**
 * Desktop entry-point convention plugin.
 *
 * Applied on a desktop application module (e.g. `:desktopApp` once introduced).
 * Sets up the Kotlin JVM toolchain, Compose Multiplatform compiler, and the
 * Compose Desktop application DSL. Distribution targets (Dmg/Msi/Deb) are
 * placeholders — fill them in when the desktop module is actually scaffolded.
 */
class DesktopApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "kotlin.jvm",
                "compose.multiplatform",
                "compose.compiler",
                "dreamstream.detekt",
                "dreamstream.testing",
            )

            configureKotlinJvm()

            // Ensure Jar tasks are deterministic for reproducible desktop builds.
            tasks.withType<Jar>().configureEach {
                isReproducibleFileOrder = true
                isPreserveFileTimestamps = false
            }
        }
    }
}
