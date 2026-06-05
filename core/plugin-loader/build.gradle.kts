plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.serialization)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.pluginloader"
    }
    sourceSets {
        commonMain.dependencies {
            api(libs.kermit)
            api(projects.core.domain)
            api(projects.core.pluginApi)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.okio)
        }
    }
}
