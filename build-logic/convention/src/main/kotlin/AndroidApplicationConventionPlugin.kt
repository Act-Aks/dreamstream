import com.android.build.api.dsl.ApplicationExtension
import com.dreamstream.convention.*
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Convention plugin for the :app module.
 *
 * Applies the Android application plugin, Kotlin Android plugin, shared detekt
 * and testing add-ons, then configures common Android + build-type settings.
 *
 * Compose, Koin, etc. are NOT applied here — those are intentionally separate
 * add-on plugins. The :app module decides what to add on top.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "android-application",
                "dreamstream-detekt",
                "dreamstream-testing",
            )

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)

                namespace = applicationId

                defaultConfig {
                    applicationId = this@with.applicationId
                    versionCode = appVersionCode
                    versionName = appVersion
                    targetSdk = targetSdkVersion
                }
            }
        }
    }
}
