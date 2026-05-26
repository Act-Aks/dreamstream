plugins {
    alias(libs.plugins.dreamstream.kmp.library)
    alias(libs.plugins.dreamstream.koin)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.settings.data"
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.feature.settings.domain)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.appcompat)
            }
        }
    }
}
