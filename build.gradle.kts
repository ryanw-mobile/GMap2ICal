/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.compose)
    alias(libs.plugins.gradle.ktlint)
    alias(libs.plugins.kotest)
}

group = "uk.ryanwong"
version = libs.versions.application.version.get()

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.core.jvm)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.timezonemap)
                implementation(libs.retrofit)
                implementation(libs.retrofit2.kotlin.coroutines.adapter)
                implementation(libs.retrofit2.kotlinx.serialization.converter)
                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.napier)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.mockk)
                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.runner.junit5.jvm)
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.property)
                implementation(libs.ktor.client.mock)
                implementation(libs.compose.ui.test.junit4)
                implementation(libs.junit.jupiter)
                implementation(libs.junit.vintage.engine)
            }
        }
    }
}

koverMerged {
    enable()

    htmlReport {
        overrideClassFilter {
            excludes += listOf(
                "uk.ryanwong.gmap2ics.app.configs.*",
                "uk.ryanwong.gmap2ics.ui.screens.*",
                "uk.ryanwong.gmap2ics.ComposableSingletons*",
                "uk.ryanwong.gmap2ics.ui.theme.*",
                "uk.ryanwong.gmap2ics.MainKt"
            )
        }
    }

    xmlReport {
        overrideClassFilter {
            excludes += listOf(
                "uk.ryanwong.gmap2ics.app.configs.*",
                "uk.ryanwong.gmap2ics.ui.screens.*",
                "uk.ryanwong.gmap2ics.ComposableSingletons*",
                "uk.ryanwong.gmap2ics.ui.theme.*",
                "uk.ryanwong.gmap2ics.MainKt"
            )
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GMapTimelineToICS"
            packageVersion = libs.versions.application.version.get()
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    jvmArgs = mutableListOf("--enable-preview")

    extensions.configure(kotlinx.kover.api.KoverTaskExtension::class) {
        // set to true to disable instrumentation of this task,
        // Kover reports will not depend on the results of its execution
        isDisabled.set(false)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-P", "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
}
