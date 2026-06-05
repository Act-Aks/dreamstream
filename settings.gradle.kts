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

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "DreamStream"

/*********************** All The Core Modules ***************************/
//include(":core:player")
include(":core:data")
include(":core:database")
include(":core:design-system")
include(":core:domain")
include(":core:plugin-api")
include(":core:plugin-loader")
include(":core:presentation")

/************************ All The Plugins *******************************/
include(":plugin:flixhq")

/********************* All The Feature Modules **************************/
include(":feature:home:data")
include(":feature:home:domain")
include(":feature:home:presentation")
include(":feature:details:data")
include(":feature:details:domain")
include(":feature:details:presentation")
include(":feature:search:data")
include(":feature:search:domain")
include(":feature:search:presentation")
include(":feature:settings:data")
include(":feature:settings:domain")
include(":feature:settings:presentation")
//listOf("browse", "player", "plugins", "watchlist", "history").forEach { feature ->
//    include(":feature:$feature:data")
//    include(":feature:$feature:domain")
//    include(":feature:$feature:presentation")
//}

/********************* All The Apps **************************/
include(":app:android")
include(":app:shared")
//include(":app:desktop")
//include(":app:tv")
