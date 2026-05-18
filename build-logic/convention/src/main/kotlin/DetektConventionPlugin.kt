import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.javaVersion
import com.dreamstream.convention.lib
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import io.gitlab.arturbosch.detekt.Detekt

/**
 * Detekt convention plugin.
 *
 * Applied transitively by every module convention plugin so static-analysis runs
 * consistently across the whole project. Reports land under build/reports/detekt/.
 *
 * Config: config/detekt/detekt.yml
 * Baseline: config/detekt/baseline.xml (empty by default — strict from day one)
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins("detekt")

            extensions.configure<DetektExtension> {
                parallel = true
                buildUponDefaultConfig = true
                autoCorrect = false
                ignoreFailures = false
                config.setFrom(rootProject.file("config/detekt/detekt.yml"))
                baseline = rootProject.file("config/detekt/baseline.xml")
            }

            dependencies {
                "detektPlugins"(lib("detekt-formatting"))
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget = javaVersion.toString()
                reports {
                    xml.required.set(true)
                    html.required.set(true)
                    sarif.required.set(true)
                    md.required.set(false)
                    txt.required.set(false)
                }
            }
        }
    }
}
