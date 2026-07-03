import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.configureAndroidLibraryTarget
import com.dreamstream.convention.configureDesktopTarget
import com.dreamstream.convention.configureKotlinMultiplatformCompilerOpts
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Convention plugin for KMP library modules that need both an Android target and
 * a desktop (JVM) target — typically `:core:data` style modules whose Android
 * portion uses `com.android.kotlin.multiplatform.library`.
 */
class KmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "kotlin.multiplatform",
                "android.kotlin.multiplatform.library",
                "dreamstream.detekt",
                "dreamstream.testing",
            ).also {
                val enableDependencyAnalysisForSubProjects =
                    providers.gradleProperty("dreamstream.enableDependencyAnalysisForSubProjects")
                        .map { it.toBoolean() }.orElse(false)
                if (enableDependencyAnalysisForSubProjects.get()) {
                    applyPlugins("dreamstream.dependency.analysis")
                }
            }

            extensions.configure<KotlinMultiplatformExtension> {
                configureAndroidLibraryTarget(this)
                configureDesktopTarget(this)
                configureKotlinMultiplatformCompilerOpts(this)
            }
        }
    }
}
