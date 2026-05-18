package com.dreamstream.convention

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Project

/**
 * Configures Android build types for an application module.
 */
internal fun Project.configureAndroidBuildTypes(
    extension: ApplicationExtension,
) {
    with(extension) {
        buildTypes {
            debug {
                applicationIdSuffix = ".debug"
                isDebuggable = true
                isMinifyEnabled = false
            }
            release {
                isMinifyEnabled = true
                isShrinkResources = true
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro",
                )
            }
        }

        configureSigningConfigs(this)
    }
}

/**
 * Creates the release signing config when keystore environment variables are present.
 * Local development stays signing-free; CI injects credentials via env vars.
 */
private fun Project.configureSigningConfigs(
    extension: ApplicationExtension,
) {
    with(extension) {
        val keystoreFile = System.getenv("KEYSTORE_FILE")
        if (!keystoreFile.isNullOrBlank()) {
            signingConfigs {
                create("release") {
                    storeFile = file(keystoreFile)
                    storePassword = System.getenv("STORE_PASSWORD")
                    keyAlias = System.getenv("KEY_ALIAS")
                    keyPassword = System.getenv("KEY_PASSWORD")
                }
            }
            buildTypes.getByName("release") {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }
}
