plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.koin)
    alias(libs.plugins.dreamstream.ktor)
    alias(libs.plugins.dreamstream.room)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.data"
    }
    sourceSets {
        commonMain.dependencies {
            api(projects.core.domain)
            api(projects.core.database)
            api(projects.core.pluginApi)
            api(projects.core.pluginLoader)
            // Kermit — implements DreamLogger for structured logging.
            implementation(libs.kermit)
            implementation(libs.ktor.client.encoding)
            // DataStore (KMP-compatible artifact) — preferences persistence.
            implementation(libs.androidx.datastore.core)
            implementation(libs.androidx.datastore.preferences)
        }
    }
}
