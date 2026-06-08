plugins {
    alias(libs.plugins.dreamstream.kmp.library)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.testing"
    }
    sourceSets{
        commonMain.dependencies {
            implementation(projects.core.domain)
            implementation(projects.core.pluginApi)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.coroutines.test)
//            implementation(libs.kotest.assertions.core)
        }
    }
}
