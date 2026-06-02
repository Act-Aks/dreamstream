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
                // Exposed as api: AndroidSettingsRepository implements SettingsRepository,
                // so app:shared needs the domain on its compile classpath.
                api(projects.feature.settings.domain)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidx.appcompat)
                // DataStore type used in AndroidSettingsRepository constructor.
                implementation(libs.androidx.datastore.preferences.core)
            }
        }
    }
}
