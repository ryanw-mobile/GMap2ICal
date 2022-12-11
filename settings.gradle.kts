/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

enableFeaturePreview("VERSION_CATALOGS")
rootProject.name = "GMapTimelineToICS"
