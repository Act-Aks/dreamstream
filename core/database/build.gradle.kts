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
            api(projects.core.domain)
        }

        all {
            languageSettings {
                optIn("androidx.room3.ExperimentalRoomApi")
            }
        }
    }
}
