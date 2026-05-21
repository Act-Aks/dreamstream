package com.dreamstream.convention

import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

/**
 * Hook to configure the Android KMP target.
 *
 * With AGP's `com.android.kotlin.multiplatform.library` plugin the Android
 * target is provisioned automatically through the `kotlin { androidLibrary { } }`
 * DSL declared in the module's own build script, and the Kotlin compilation's
 * jvmTarget is aligned with `compileOptions.targetCompatibility` by AGP.
 *
 * This function is intentionally a no-op for now and exists so consumers
 * (convention plugins) keep a stable entry point if per-target Kotlin options
 * become necessary later.
 */
internal fun Project.configureAndroidLibraryTarget(
    extension: KotlinMultiplatformExtension,
) {
    with(extension) {
        // Access the Android target, which also serves as the Android-specific DSL extension
        targets.withType<KotlinMultiplatformAndroidLibraryTarget> {
            compileSdk { version = release(compileSdkVersion) }
            minSdk { version = release(minSdkVersion) }
            compilerOptions {
                jvmTarget.set(project.jvmTarget)
            }
            withHostTest { }
        }
        configureCoreLibraryDesugaring()
    }
}