package com.dreamstream.tasks

import com.dreamstream.convention.infoColor
import com.dreamstream.convention.lifecycleSuccess
import com.dreamstream.convention.warnColor
import org.gradle.api.DefaultTask
import org.gradle.api.GradleException
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.SkipWhenEmpty
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Validates Compose Multiplatform string resources for a single module.
 *
 * This task enforces that every required locale variant contains *all* string
 * keys present in the default [`values/strings.xml`] file located under
 * [composeResourcesDir].
 *
 * Behavior:
 * - If [composeResourcesDir] does not exist, the task is skipped.
 * - If the default strings file does not exist, the task is skipped.
 * - If [requiredLocales] is empty, the task is skipped.
 * - For each required locale:
 *   - Missing `values-{locale}/strings.xml` → build failure.
 *   - Missing keys in `values-{locale}/strings.xml` → build failure.
 *   - Extra keys in `values-{locale}/strings.xml` (not in default) → warning only.
 *
 * This task is intentionally marked as non-cacheable because it is a fast
 * verification step that does not produce any persistent outputs.
 */
@DisableCachingByDefault(because = "Verification task with no outputs; caching not needed")
abstract class ValidateLocalizationTask : DefaultTask() {

    /**
     * Root directory containing Compose Multiplatform resources for this module.
     *
     * Example layout:
     * - `src/commonMain/composeResources/values/strings.xml` (default)
     * - `src/commonMain/composeResources/values-de/strings.xml` (German)
     * - `src/commonMain/composeResources/values-hi/strings.xml` (Hindi)
     *
     * The directory is treated as an input for incremental execution. The task
     * is automatically skipped when the directory is empty or missing. [web:23]
     */
    @get:InputDirectory
    @get:SkipWhenEmpty
    @get:PathSensitive(PathSensitivity.RELATIVE)
    abstract val composeResourcesDir: DirectoryProperty

    /**
     * List of locale codes that must be present for this module.
     *
     * Example: `["hi", "de", "ja"]` corresponding to `values-hi`, `values-de`, `values-ja`.
     *
     * This list is typically provided from a single source of truth in the root
     * project, such as a `dreamstream.localization.requiredLocales` property.
     */
    @get:Input
    abstract val requiredLocales: ListProperty<String>

    /**
     * Gradle project path for this module (for example, `:feature:search:presentation`).
     *
     * Used exclusively for log and error messages. This value is provided at
     * configuration time to keep the task compatible with the configuration
     * cache (the task action does not access `project` directly). [web:22]
     */
    @get:Input
    abstract val projectPath: Property<String>

    /**
     * Human‑readable display name of the module used in log and error messages.
     *
     * This is a cosmetic property and does not participate in up‑to‑date checks.
     * If not set, [projectPath] is used as a fallback.
     */
    @get:Internal
    abstract val moduleDisplayName: Property<String>

    /**
     * Relative path (from the project directory) to the default strings file.
     *
     * Example: `src/commonMain/composeResources/values/strings.xml`.
     *
     * This value is precomputed during task configuration to avoid accessing
     * `project.projectDir` during task execution, which keeps the task
     * configuration‑cache friendly. [web:22]
     */
    @get:Input
    abstract val defaultStringsRelativePath: Property<String>

    /**
     * Effective module name used in log and error messages.
     *
     * Prefers [moduleDisplayName] when set, otherwise falls back to [projectPath].
     */
    private val displayName: String
        get() = moduleDisplayName.orNull ?: projectPath.get()

    /**
     * Performs localization validation for this module.
     *
     * Steps:
     * 1. Resolve the default `values/strings.xml` file and collect all string keys.
     * 2. For each required locale:
     *    - Fail if the locale file is missing.
     *    - Fail if any keys from the default file are missing in the locale file.
     *    - Warn if the locale file defines keys that do not exist in the default file.
     *
     * If any missing file or key is detected, a [GradleException] is thrown with
     * a formatted multi‑line error message. On success, a concise summary is
     * logged with [logger.lifecycle].
     */
    @TaskAction
    fun validate() {
        val dir = composeResourcesDir.asFile.get()
        val defaultFile = dir.resolve("values/strings.xml")
        if (!defaultFile.exists()) {
            logger.infoColor("[localization] $displayName: no default values/strings.xml, skipping localization validation")
            return
        }

        val defaultKeys = parseStringKeys(defaultFile)
        if (defaultKeys.isEmpty()) {
            logger.infoColor("[localization] $displayName: default strings.xml has no <string> keys, skipping validation")
            return
        }

        val locales = requiredLocales.getOrElse(emptyList())
        if (locales.isEmpty()) {
            logger.infoColor("[localization] $displayName: no required locales configured, skipping validation")
            return
        }

        var hasError = false
        val errors = mutableListOf<String>()

        locales.forEach { locale ->
            val localeDir = dir.resolve("values-$locale")
            val localeFile = localeDir.resolve("strings.xml")

            if (!localeFile.exists()) {
                errors.add("  values-$locale/ → MISSING strings.xml")
                hasError = true
                return@forEach
            }

            val localeKeys = parseStringKeys(localeFile)
            val missing = (defaultKeys - localeKeys).toSortedSet()
            if (missing.isNotEmpty()) {
                missing.forEach { key ->
                    errors.add("  values-$locale/strings.xml  →  missing key: \"$key\"")
                }
                hasError = true
            }

            val extra = (localeKeys - defaultKeys).toSortedSet()
            if (extra.isNotEmpty()) {
                logger.warnColor(buildString {
                    appendLine("[localization] Warning in $displayName")
                    appendLine("  values-$locale/strings.xml has extra keys not present in default:")
                    extra.forEach { key ->
                        appendLine("    - $key")
                    }
                })
            }
        }

        val defaultPath = defaultStringsRelativePath.get()

        if (hasError) {
            throw GradleException(
                buildErrorMessage(
                    moduleName = displayName,
                    errors = errors,
                    locales = locales,
                    defaultPath = defaultPath,
                )
            )
        }

        logger.lifecycleSuccess(buildSuccessMessage(displayName, defaultPath, locales))
    }
}

/**
 * Parses all `<string>` element `name` attributes from the given XML file.
 *
 * The file is expected to follow the standard Android/Compose XML string resource format,
 * for example:
 *
 * ```xml
 * <resources>
 *     <string name="search_no_results_hint">No results found</string>
 * </resources>
 * ```
 *
 * @param file XML file to parse.
 * @return a [Set] of string keys defined in the file.
 */
private fun parseStringKeys(file: File): Set<String> {
    val factory = DocumentBuilderFactory.newInstance()
    val builder = factory.newDocumentBuilder()
    val document = builder.parse(file)

    val keys = mutableSetOf<String>()
    val nodes = document.getElementsByTagName("string")

    for (index in 0 until nodes.length) {
        val element = nodes.item(index)
        val key = element.attributes?.getNamedItem("name")?.nodeValue
        if (!key.isNullOrBlank()) {
            keys.add(key)
        }
    }

    return keys
}

/**
 * Builds a human‑readable multi‑line error message for localization failures.
 *
 * The message includes:
 * - A header indicating validation failure.
 * - One line per missing file/key.
 * - The path to the default strings file.
 * - The list of required locales.
 */
private fun buildErrorMessage(
    moduleName: String,
    errors: List<String>,
    locales: List<String>,
    defaultPath: String,
): String = buildString {
    appendLine()
    appendLine("  ╔═══════════════════════════════════════════════════════════════")
    appendLine("  ║  Localization validation FAILED in $moduleName")
    appendLine("  ╚═══════════════════════════════════════════════════════════════")
    appendLine()
    errors.forEach { appendLine(it) }
    appendLine()
    appendLine("  Default strings: $defaultPath")
    appendLine("  Required locales: ${locales.joinToString(", ")}")
    appendLine("  Add the missing translations to fix this error.")
    appendLine()
}

/**
 * Builds a human‑readable multi‑line success message for successful validation.
 *
 * The message includes:
 * - A one-line success header.
 * - The path to the default strings file.
 * - The list of locales that were validated.
 */
private fun buildSuccessMessage(
    moduleName: String,
    defaultPath: String,
    locales: List<String>,
): String = buildString {
    appendLine()
    appendLine("  [localization] Validation PASSED for $moduleName")
    appendLine("  [localization] Default strings: $defaultPath")
    appendLine("  [localization] Locales checked: ${locales.joinToString()}")
    appendLine()
}
