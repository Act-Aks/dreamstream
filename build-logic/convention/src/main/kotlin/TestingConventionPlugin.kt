import com.dreamstream.convention.bundle
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinBasePlugin

/**
 * Adds JUnit5 + AssertK + Turbine + coroutines-test to every module's test sources.
 *
 * Detects whether the module is KMP, Android, or JVM and wires deps into the right
 * source set. Also makes every Test task run on JUnit Platform.
 */
class TestingConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.withType<Test>().configureEach {
                useJUnitPlatform()
            }

            // JUnit Platform engine must be on the runtime classpath wherever the
            // platform plugin runs (configuration name varies by module type, so we
            // wait for the relevant plugin to apply and then add deps).
            plugins.withType<KotlinBasePlugin> {
                wireTestDependencies()
            }
        }
    }

    private fun Project.wireTestDependencies() {
        val isKmp = extensions.findByType(KotlinMultiplatformExtension::class.java) != null

        if (isKmp) {
            extensions.configure(KotlinMultiplatformExtension::class.java) {
                sourceSets.commonTest.dependencies {
                    implementation(kotlin("test"))
                    implementation(lib("assertk").get())
                    implementation(lib("turbine").get())
                    implementation(lib("kotlinx-coroutines-test").get())
                }
                sourceSets.findByName("desktopTest")?.dependencies {
                    implementation(lib("junit-jupiter-api").get())
                    implementation(lib("junit-jupiter-params").get())
                    runtimeOnly(lib("junit-jupiter-engine").get())
                }
            }
        } else {
            dependencies {
                bundle("testing-unit-common").get().forEach { "testImplementation"(it) }
                "testRuntimeOnly"(lib("junit-jupiter-engine").get())

                // Android-only instrumented test deps when the Android plugin is present.
                if (plugins.hasPlugin("com.android.application") ||
                    plugins.hasPlugin("com.android.library")
                ) {
                    bundle("testing-android").get().forEach { "androidTestImplementation"(it) }
                }
            }
        }
    }
}
