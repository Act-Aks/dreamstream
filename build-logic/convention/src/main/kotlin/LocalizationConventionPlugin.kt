import com.dreamstream.tasks.ValidateLocalizationTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

/**
 * Convention plugin that configures per‑module localization validation for
 * Compose Multiplatform resources.
 *
 * Responsibilities:
 * - Reads required locales from the root project property
 *   `dreamstream.localization.requiredLocales` (comma‑separated list).
 * - Registers a [ValidateLocalizationTask] named `validateLocalization` in each
 *   project that applies this plugin, configured to scan `src/commonMain/composeResources`.
 * - Wires all Kotlin compile tasks in the project to depend on `validateLocalization`,
 *   so validation runs before compilation.
 *
 * This plugin is intended to be applied to modules that provide Compose resources
 * via `src/commonMain/composeResources`.
 */
class LocalizationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val requiredLocalesProp =
            rootProject.findProperty("dreamstream.localization.requiredLocales")
                ?.toString()
                ?.split(',')
                ?.map { it.trim() }
                ?.filter { it.isNotEmpty() }
                ?: emptyList()

        if (requiredLocalesProp.isEmpty()) {
            logger.info("dreamstream-localization: no dreamstream.localization.requiredLocales configured, skipping for $path")
            return@with
        }

        // Precompute directories and paths at configuration time so the task action
        // does not need to access the Project API, which keeps it compatible with
        // Gradle's configuration cache. [web:22]
        val composeResourcesDir = layout.projectDirectory.dir("src/commonMain/composeResources")
        val defaultStringsPath = composeResourcesDir
            .file("values/strings.xml")
            .asFile
            .relativeTo(projectDir)
            .path

        val validateLocalizationProvider =
            tasks.register<ValidateLocalizationTask>("validateLocalization") {
                group = "verification"
                description =
                    "Validates Compose Multiplatform string resources for missing keys in locale variants."

                this.composeResourcesDir.set(composeResourcesDir)
                requiredLocales.set(requiredLocalesProp)
                projectPath.set(project.path)
                moduleDisplayName.set(project.path)
                defaultStringsRelativePath.set(defaultStringsPath)
            }

        tasks.withType<KotlinCompile>().configureEach {
            dependsOn(validateLocalizationProvider)
        }
    }
}
