import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.javaVersion
import com.dreamstream.convention.lib
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

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

            configure<DetektExtension> {
                parallel.set(true)
                buildUponDefaultConfig.set( true)
                autoCorrect.set( false)
                ignoreFailures.set(false)
                config.from(rootProject.file("config/detekt/detekt.yml"))
                baseline.set(rootProject.file("config/detekt/baseline.xml"))
            }

            dependencies {
                "detektPlugins"(lib("detekt-rules-ktlint-wrapper").get())
            }

            tasks.withType<Detekt>().configureEach {
                jvmTarget.set(javaVersion.toString())

                reports {
                    checkstyle.required.set(true)
                    html.required.set(true)
                    sarif.required.set(true)
                    markdown.required.set(true)
                }
            }
        }
    }
}
