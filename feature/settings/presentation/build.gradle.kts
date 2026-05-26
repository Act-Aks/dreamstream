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
                implementation(projects.feature.settings.domain)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "$moduleName.resources"
}
