/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

rootProject.name = "GMap2iCal"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "dev.icerock.moko.resources-generator") {
                useModule("dev.icerock.moko:resources-generator:0.23.0")
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}

include(":composeApp")
