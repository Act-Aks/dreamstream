plugins {
    alias(libs.plugins.dreamstream.feature)
}

val moduleName = "com.dreamstream.feature.settings.presentation"

kotlin {
    android {
        namespace = moduleName
    }

    sourceSets {
        commonMain {
            dependencies {
                // Exposed as api: SettingsState and SettingsRoute reference domain types
                // (AppLanguage, SettingsRepository) that callers such as app:shared need to see.
                api(projects.feature.settings.domain)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "$moduleName.resources"
}
