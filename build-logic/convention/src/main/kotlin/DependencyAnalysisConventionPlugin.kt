import com.dreamstream.convention.applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin that wires the Dependency Analysis Gradle Plugin (DAGP)
 * into every library and application module.
 *
 * Applied automatically through [KmpLibraryConventionPlugin] (which transitively
 * covers every KMP library module — domain, data, presentation, core libs) and
 * [AndroidApplicationConventionPlugin] (which covers the :app:android entry point).
 *
 * The root project retains its own top-level `com.autonomousapps.dependency-analysis`
 * application for two exclusive responsibilities:
 *   1. The [buildHealth] aggregation task (rolls up advice from every subproject).
 *   2. The top-level [dependencyAnalysis {}] configuration block (bundles, issues).
 *
 * This plugin replaces the old `subprojects { apply("...") }` pattern in the root
 * build file, making dependency-analysis an explicit part of each module's contract
 * rather than an invisible side effect of the root configuration phase.
 */
class DependencyAnalysisConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins("dependency-analysis")
        }
    }
}
