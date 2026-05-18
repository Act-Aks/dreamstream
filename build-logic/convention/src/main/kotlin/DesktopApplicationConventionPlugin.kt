import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.configureKotlinJvm
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.desktop.application.dsl.JvmApplication

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
                "kotlin-jvm",
                "compose-compiler",
                "compose-multiplatform",
                "dreamstream-detekt",
                "dreamstream-testing",
            )

            configureKotlinJvm()

            val compose = extensions.getByType<ComposeExtension>()
            compose.extensions.findByType(JvmApplication::class.java)?.apply {
                // App DSL is supplied by org.jetbrains.compose.desktop {} closure in the
                // consuming module — kept intentionally minimal here.
            }

            dependencies {
                "implementation"(lib("kotlinx-coroutines-swing"))
                "implementation"(lib("kermit"))
            }

            // Ensure Jar tasks are deterministic for reproducible desktop builds.
            tasks.withType<Jar>().configureEach {
                isReproducibleFileOrder = true
                isPreserveFileTimestamps = false
            }
        }
    }
}
