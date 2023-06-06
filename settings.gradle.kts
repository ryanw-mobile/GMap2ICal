/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }
}

rootProject.name = "GMapTimelineToICS"
