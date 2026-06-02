plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.serialization)
    alias(libs.plugins.dreamstream.room)
}

kotlin {
    android {
        namespace = "com.dreamstream.core.database"
    }

    sourceSets {
        commonMain.dependencies {
            api(projects.core.data)
            api(projects.core.model)
        }
        desktopMain.dependencies {
            implementation(libs.androidx.datastore.preferences.core)
        }

        all {
            languageSettings {
                optIn("androidx.room3.ExperimentalRoomApi")
            }
        }
    }
}
