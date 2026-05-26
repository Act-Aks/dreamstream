import com.dreamstream.convention.applyPlugins
import com.dreamstream.convention.configureComposeCompiler
import com.dreamstream.convention.configureComposeDependencies
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Compose Multiplatform convention plugin.
 *
 * DreamStream uses **Compose Multiplatform** (org.jetbrains.compose), NOT
 * AndroidX Compose. All Compose runtime / material / ui / lifecycle /
 * navigation deps are sourced from the JetBrains forks
 * (`org.jetbrains.compose.*` and `org.jetbrains.androidx.*`) and wired into
 * `commonMain` so they work on Android, desktop, and any future KMP target.
 *
 * Responsibilities:
 *  - Applies the Compose compiler + Compose Multiplatform plugins.
 *  - Enables Android `buildFeatures.compose` when an Android extension is present.
 *  - Honours `dreamstream.enableComposeMetrics` (gradle.properties) and writes
 *    compiler reports + metrics into `build/compose-{reports,metrics}/`.
 *  - Wires the CMP runtime, foundation, material3, icons, resources, and
 *    UI-tooling preview into `commonMain`.
 *  - Wires the JetBrains lifecycle (runtime + viewmodel) bundle into
 *    `commonMain` so ViewModel + lifecycle-aware composables work cross-platform.
 *  - Adds `compose.desktop.currentOs` to `desktopMain`.
 *  - Adds `compose.uiTooling` as a `debugImplementation` for Android-only modules
 *    (e.g. the `:app` entry) so Android Studio previews render.
 *
 * Apply on every module that contains `@Composable` code.
 */
class ComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins(
                "dreamstream-kmp-library",
                "compose-compiler",
                "compose-multiplatform",
                "dreamstream-localization",
            )

            configureComposeCompiler()
            configureComposeDependencies()
        }
    }
}
