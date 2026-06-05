import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.bundle
import com.dreamstream.convention.lib
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

/**
 * Convention plugin for KMP feature presentation modules.
 *
 * DreamStream's libraries are all KMP and use **Compose Multiplatform**. This
 * plugin builds on [KmpLibraryConventionPlugin] and layers on the typical
 * feature-presentation stack: Compose Multiplatform, Koin, Serialization, and
 * the shared cross-feature foundation (core domain/presentation/design-system,
 * CMP navigation, coil, kermit). CMP lifecycle (runtime + viewmodel) comes in
 * transitively via [ComposeConventionPlugin].
 *
 * Apply on every `:feature:<name>:presentation` module. Features must never
 * depend on other features directly — cross-feature glue lives in `:app`.
 */
class FeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "dreamstream-kmp-library",
                "dreamstream-compose",
                "dreamstream-koin",
                "dreamstream-serialization",
            )

            val outer: Project = this

            extensions.configure<KotlinMultiplatformExtension> {
                sourceSets.commonMain.dependencies {
                    addProjectIfPresent(outer, ":core:domain")
                    // Exposed as api: feature presentation modules leak UiText, ObserveAsEvents
                    // and other core:presentation types through their public composable/ViewModel
                    // signatures, so consumers need it on their compile classpath.
                    addProjectIfPresent(outer, ":core:presentation", asApi = true)
                    addProjectIfPresent(outer, ":core:design-system")

                    implementation(lib("compose-navigation3-ui").get())
                    bundle("coil").get().forEach { implementation(it) }
                }
            }
        }
    }

    private fun KotlinDependencyHandler.addProjectIfPresent(
        target: Project,
        path: String,
        asApi: Boolean = false,
    ) {
        if (target.rootProject.findProject(path) != null) {
            if (asApi) api(target.project(path))
            else implementation(target.project(path))
        }
    }
}
