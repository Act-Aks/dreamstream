plugins {
    alias(libs.plugins.dreamstream.kmp.library)
}

kotlin {
    android {
        namespace = "com.dreamstream.feature.settings.domain"
    }
    sourceSets{
        commonMain.dependencies {
            api(projects.core.domain)
        }
    }
}
