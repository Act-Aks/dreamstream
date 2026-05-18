import org.jetbrains.kotlin.gradle.dsl.JvmTarget

/**
 * =============================================================================
 * Declares the dependencies that convention plugin *source code* needs at compile
 * time — i.e. the Gradle API surface of AGP, KGP, KSP, Compose, Room, Detekt.
 * These are NOT transitive to app modules; they exist only so we can write
 * "android { ... }", "kotlin { ... }", "compose { ... }", etc. inside convention
 * plugin Kotlin files.
 * =============================================================================
 */

plugins {
    `kotlin-dsl`
}

group = "com.dreamstream.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    /** Access libs.* version catalog accessors inside convention plugin code */
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
    compileOnly(libs.compose.gradle.plugin)
    compileOnly(libs.room.gradle.plugin)
    compileOnly(libs.detekt.gradle.plugin)
}

/**
 * Register every convention plugin so Gradle knows which class implements which ID.
 * The id must match exactly what modules reference in their plugins { } block, and
 * also exactly what is declared in gradle/libs.versions.toml under [plugins].
 */
gradlePlugin {
    plugins {
        /******************* Application entry points ********************/
        register("androidApplication") {
            id = "com.dreamstream.androidApplication"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidTv") {
            id = "com.dreamstream.android.tv"
            implementationClass = "AndroidTvConventionPlugin"
        }
        register("desktopApplication") {
            id = "com.dreamstream.desktop.app"
            implementationClass = "DesktopApplicationConventionPlugin"
        }

        /********************  Compose  **********************/
        register("compose") {
            id = "com.dreamstream.compose"
            implementationClass = "ComposeConventionPlugin"
        }

        /**************  KMP libraries (the only kind we have)  ******************/
        register("domain") {
            id = "com.dreamstream.domain"
            implementationClass = "DomainModuleConventionPlugin"
        }
        register("kmpLibrary") {
            id = "com.dreamstream.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("feature") {
            id = "com.dreamstream.feature"
            implementationClass = "FeatureConventionPlugin"
        }

        /** Add-on plugins (apply on top of the above) **/
        register("koin") {
            id = "com.dreamstream.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("ktor") {
            id = "com.dreamstream.ktor"
            implementationClass = "KtorConventionPlugin"
        }
        register("room") {
            id = "com.dreamstream.room"
            implementationClass = "RoomConventionPlugin"
        }
        register("serialization") {
            id = "com.dreamstream.serialization"
            implementationClass = "SerializationConventionPlugin"
        }
        register("testing") {
            id = "com.dreamstream.testing"
            implementationClass = "TestingConventionPlugin"
        }
        register("detekt") {
            id = "com.dreamstream.detekt"
            implementationClass = "DetektConventionPlugin"
        }
    }
}
