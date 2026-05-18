/**
 * =============================================================================
 * The build-logic directory is an included build. It has its own settings file
 * so Gradle treats it as an independent project whose outputs (convention plugins)
 * are available to the root project at configuration time.
 * =============================================================================
 */

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("libs"){
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "build-logic"

include(":convention")