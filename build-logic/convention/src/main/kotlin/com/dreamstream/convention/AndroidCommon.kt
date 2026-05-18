package com.dreamstream.convention

import com.android.build.api.dsl.*
import com.dreamstream.tasks.configureKotlinCompile
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

private val defaultPackagingExcludes = setOf(
    "META-INF/LICENSE.md",
    "META-INF/LICENSE-notice.md",
    "META-INF/{AL2.0,LGPL2.1}",
)

/**
 * Configures the shared Android DSL for Android modules.
 *
 * Applies the common Android settings that are shared by application and
 * library modules, including compile SDK, default configuration, Java
 * compatibility, build features, packaging exclusions, unit test defaults,
 * core library desugaring, and Kotlin compilation settings.
 *
 * @receiver the Gradle [Project] being configured.
 * @param extension the Android DSL extension to configure.
 */
internal fun Project.configureAndroid(extension: CommonExtension) {
    with(extension) {
        compileSdk = compileSdkVersion
        configureDefaultConfig(defaultConfig)
        configureCompileOptions(compileOptions)
        configureBuildFeatures(buildFeatures)
        configurePackaging(packaging)
        configureTestOptions(testOptions)
    }

    configureCoreLibraryDesugaring()
    configureKotlinCompile()
}

/**
 * Configures the Android `defaultConfig` block.
 *
 * Sets the minimum SDK, test instrumentation runner, and vector drawable
 * support for Android modules.
 *
 * @param defaultConfig the DSL block to configure.
 */
internal fun Project.configureDefaultConfig(defaultConfig: DefaultConfig) {
    defaultConfig.apply {
        minSdk {
            version = release(minSdkVersion)
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
}

/**
 * Configures Java compatibility and enables core library desugaring for
 * Android modules.
 *
 * @param compileOptions the DSL block to configure.
 */
internal fun Project.configureCompileOptions(compileOptions: CompileOptions) {
    compileOptions.apply {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        isCoreLibraryDesugaringEnabled = true
    }
}

/**
 * Enables shared Android build features.
 *
 * @param buildFeatures the DSL block to configure.
 */
internal fun Project.configureBuildFeatures(buildFeatures: BuildFeatures) {
    buildFeatures.apply {
        buildConfig = true
    }
}

/**
 * Configures Android packaging exclusions.
 *
 * @param packaging the DSL block to configure.
 */
internal fun Project.configurePackaging(packaging: Packaging) {
    packaging.apply {
        resources {
            excludes += defaultPackagingExcludes
        }
    }
}

/**
 * Configures Android unit test defaults.
 *
 * Enables return-default-values and Android resources for unit tests.
 *
 * @param testOptions the DSL block to configure.
 */
internal fun Project.configureTestOptions(testOptions: TestOptions) {
    testOptions.apply {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

/**
 * Adds the core library desugaring dependency required by
 * [configureCompileOptions] when core library desugaring is enabled.
 */
internal fun Project.configureCoreLibraryDesugaring() {
    dependencies {
        "coreLibraryDesugaring"(lib("android-desugarJdkLibs"))
    }
}