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
 * Ktor convention plugin.
 *
 * - Auto-applies the serialization plugin (Ktor JSON requires it).
 * - Wires ktor-client + content-negotiation + JSON + logging in commonMain.
 * - Adds the OkHttp engine on Android and the Java engine on desktop.
 * - Adds ktor-client-mock to test source sets so data-layer fakes can stub HTTP.
 *
 * Apply on data-layer modules that perform HTTP. Domain modules must NEVER apply this.
 */
class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins("dreamstream-serialization")

            val isKmp = extensions.findByType<KotlinMultiplatformExtension>() != null
            if (isKmp) {
                extensions.configure<KotlinMultiplatformExtension> {
                    sourceSets.commonMain.dependencies {
                        implementation(lib("ktor-client-core").get())
                        implementation(lib("ktor-client-content-negotiation").get())
                        implementation(lib("ktor-client-logging").get())
                        implementation(lib("ktor-serialization-kotlinx-json").get())
                    }
                    sourceSets.androidMain.dependencies {
                        implementation(lib("ktor-client-okhttp").get())
                    }
                    sourceSets.findByName("desktopMain")?.dependencies {
                        implementation(lib("ktor-client-okhttp").get())
                    }
                    sourceSets.commonTest.dependencies {
                        implementation(lib("ktor-client-mock").get())
                    }
                }
            } else {
                dependencies {
                    bundle("ktor-common").get().forEach { "implementation"(it) }
                    "implementation"(lib("ktor-client-okhttp").get())
                    "testImplementation"(lib("ktor-client-mock").get())
                }
            }
        }
    }
}
