import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Applies kotlinx.serialization plugin and wires the JSON runtime into the
 * appropriate source set (commonMain for KMP modules, main for JVM/Android).
 */
class SerializationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins("kotlin-serialization")

            dependencies {
                // Promoted to api: modules that apply dreamstream-serialization expose
                // @Serializable types in their public API, so consumers need the
                // kotlinx-serialization-core annotations on their compile classpath.
                "commonMainApi"(lib("kotlinx-serialization-core").get())
                "commonMainImplementation"(lib("kotlinx-serialization-json").get())
            }
        }
    }
}
