import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.bundle
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Ktor convention plugin.
 *
 * - Auto-applies the serialization plugin (Ktor JSON requires it).
 * - Wires ktor-client + content-negotiation + JSON + logging + auth in commonMain.
 * - Adds the OkHttp engine on Android and the CIO engine on desktop.
 * - Adds ktor-client-mock to test source sets so data-layer fakes can stub HTTP.
 *
 * Apply on data-layer modules that perform HTTP. Domain modules must NEVER apply this.
 */
class KtorConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins("dreamstream-serialization")

            val isKmp = extensions.findByType(KotlinMultiplatformExtension::class.java) != null
            if (isKmp) {
                extensions.configure(KotlinMultiplatformExtension::class.java) {
                    sourceSets.getByName("commonMain").dependencies {
                        implementation(lib("ktor-client-core").get())
                        implementation(lib("ktor-client-content-negotiation").get())
                        implementation(lib("ktor-client-logging").get())
                        implementation(lib("ktor-client-auth").get())
                        implementation(lib("ktor-serialization-kotlinx-json").get())
                    }
                    sourceSets.findByName("androidMain")?.dependencies {
                        implementation(lib("ktor-client-okhttp").get())
                    }
                    sourceSets.findByName("desktopMain")?.dependencies {
                        implementation(lib("ktor-client-cio").get())
                    }
                    sourceSets.findByName("commonTest")?.dependencies {
                        implementation(lib("ktor-client-mock").get())
                    }
                }
            } else {
                dependencies {
                    "implementation"(bundle("ktor-common"))
                    "implementation"(lib("ktor-client-okhttp"))
                    "testImplementation"(lib("ktor-client-mock"))
                }
            }
        }
    }
}
