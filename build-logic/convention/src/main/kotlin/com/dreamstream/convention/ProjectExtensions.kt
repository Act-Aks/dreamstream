package com.dreamstream.convention

import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

// ============================================================================
// Version Catalog Access
// ============================================================================

/**
 * Provides type-safe access to libs.versions.toml entries.
 */
internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.lib(alias: String): Provider<MinimalExternalModuleDependency> =
    libs.findLibrary(alias)
        .orElseThrow { IllegalStateException("Library alias '$alias' not found in libs.versions.toml") }

internal fun Project.bundle(alias: String): Provider<ExternalModuleDependencyBundle> =
    libs.findBundle(alias)
        .orElseThrow { IllegalStateException("Bundle alias '$alias' not found in libs.versions.toml") }

internal fun Project.pluginId(alias: String): String =
    libs.findPlugin(alias)
        .orElseThrow { IllegalStateException("Plugin alias '$alias' not found in libs.versions.toml") }
        .get()
        .pluginId

// ============================================================================
// Project Metadata Extensions
// ============================================================================

internal val Project.applicationId: String
    get() = libs.findVersion("projectApplicationId").get().toString()

internal val Project.applicationName: String
    get() = libs.findVersion("projectApplicationName").get().toString()

internal val Project.appVersion: String
    get() = libs.findVersion("projectVersion").get().toString()

internal val Project.appVersionCode: Int
    get() = libs.findVersion("projectVersionCode").get().toString().toInt()

// ============================================================================
// SDK Version Extensions
// ============================================================================

internal val Project.compileSdkVersion: Int
    get() = libs.findVersion("projectCompileSdkVersion").get().toString().toInt()

internal val Project.minSdkVersion: Int
    get() = libs.findVersion("projectMinSdkVersion").get().toString().toInt()

internal val Project.targetSdkVersion: Int
    get() = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

// ============================================================================
// Java / JVM Version Extensions
// ============================================================================

internal val Project.projectJavaVersion: Int
    get() = libs.findVersion("projectJavaVersion").get().toString().toInt()

internal val Project.javaVersion: JavaVersion
    get() = when (projectJavaVersion) {
        17 -> JavaVersion.VERSION_17
        21 -> JavaVersion.VERSION_21
        24 -> JavaVersion.VERSION_24
        else -> JavaVersion.VERSION_17
    }

internal val Project.jvmTarget: JvmTarget
    get() = when (projectJavaVersion) {
        17 -> JvmTarget.JVM_17
        21 -> JvmTarget.JVM_21
        24 -> JvmTarget.JVM_24
        else -> JvmTarget.JVM_17
    }

// ============================================================================
// Plugin Application Extensions
// ============================================================================

/**
 * Applies multiple plugins by their version-catalog alias (under [plugins] in
 * libs.versions.toml). The alias does NOT need a version (DreamStream convention
 * plugins are versionless).
 */
internal fun Project.applyPlugins(vararg aliases: String): Project {
    with(pluginManager) {
        aliases.forEach { apply(pluginId(it)) }
    }
    return this
}
