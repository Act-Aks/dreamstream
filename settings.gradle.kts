@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "DreamStream"

/*********************** All The Core Modules ***************************/
//include(":core:data")
//include(":core:database")
include(":core:design-system")
include(":core:domain")
//include(":core:player")
//include(":core:plugin-api")
include(":core:presentation")

/********************* All The Feature Modules **************************/
//listOf("home", "search", "browse", "detail", "player", "plugins", "settings", "watchlist", "history").forEach { feature ->
//    include(":feature:$feature:data")
//    include(":feature:$feature:domain")
//    include(":feature:$feature:presentation")
//}

/********************* All The Apps **************************/
//include(":app:android")
//include(":app:desktop")
//include(":app:tv")
