plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.koin)
    alias(libs.plugins.dreamstream.ktor)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.data"
    }
    sourceSets {
        commonMain.dependencies {
            // core:domain provides Result, DataError, DreamLogger, and coroutines (api).
            api(projects.core.domain)
            // Kermit — implements DreamLogger for structured logging.
            implementation(libs.kermit)
            implementation(libs.ktor.client.encoding)
            // DataStore (KMP-compatible artifact) — preferences persistence.
            implementation(libs.androidx.datastore.preferences.core)
        }
    }
}
