import com.android.build.api.dsl.ApplicationExtension
import com.dreamstream.convention.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

/**
 * Convention plugin for the Android TV application entry point.
 *
 * This is an APPLICATION plugin (uses `com.android.application`), not a feature
 * plugin. Use it for a separate `:tvApp` module that ships the leanback / TV
 * variant of DreamStream alongside the regular mobile `:app`.
 *
 * Adds the TV launcher applicationIdSuffix so both can coexist on a single
 * device during development, and wires the androidx.tv UI deps.
 */
class AndroidTvConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "android-application",
                "dreamstream-detekt",
                "dreamstream-testing",
            )

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
                configureAndroidBuildTypes(this)

                namespace = "${this@with.applicationId}.tv"

                defaultConfig {
                    applicationId = "${this@with.applicationId}.tv"
                    versionCode = appVersionCode
                    versionName = appVersion
                    targetSdk = targetSdkVersion
                }
            }

            dependencies {
                add("implementation", bundle("androidx-tv"))
            }
        }
    }
}
