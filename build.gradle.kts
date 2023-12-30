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
version = libs.versions.packageVersion.get()

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    maven { url = uri("https://jitpack.io") }
}

kotlin {
    jvmToolchain(17)
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }

    // https://github.com/JetBrains/compose-multiplatform/issues/3123
    val osName = System.getProperty("os.name")
    val targetOs = when {
        osName == "Mac OS X" -> "macos"
        osName.startsWith("Win") -> "windows"
        osName.startsWith("Linux") -> "linux"
        else -> error("Unsupported OS: $osName")
    }

    val targetArch = when (val osArch = System.getProperty("os.arch")) {
        "x86_64", "amd64" -> "x64"
        "aarch64" -> "arm64"
        else -> error("Unsupported arch: $osArch")
    }

    val version = "0.7.70" // or any more recent version
    val target = "$targetOs-$targetArch"

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("org.jetbrains.skiko:skiko-awt-runtime-$target:$version")
                // implementation(compose.desktop.macos_arm64)
                implementation(compose.desktop.currentOs)
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)

                implementation(libs.timezonemap)
                implementation(libs.napier)
                implementation(libs.themedetector)

                implementation(libs.retrofit)
                implementation(libs.retrofit2.kotlin.coroutines.adapter)
                implementation(libs.retrofit2.kotlinx.serialization.converter)

                implementation(libs.kotlinx.coroutines.core.jvm)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.kotlinx.serialization.json)

                implementation(libs.ktor.client.cio)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(libs.kotlinx.coroutines.test)
                implementation(libs.mockk)
                implementation(libs.ktor.client.mock)
                implementation(libs.compose.ui.test.junit4)

                implementation(libs.junit.jupiter)
                implementation(libs.junit.vintage.engine)

                implementation(libs.kotest.framework.engine)
                implementation(libs.kotest.runner.junit5.jvm)
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotest.property)
            }
        }
    }
}

koverReport {
    // common filters for all reports of all variants
    filters {
        // exclusions for reports
        excludes {
            // excludes class by fully-qualified JVM class name, wildcards '*' and '?' are available
            classes(
                listOf(
                    "uk.ryanwong.gmap2ics.ComposableSingletons*",
                    "uk.ryanwong.gmap2ics.MainKt",
                ),
            )
            // excludes all classes located in specified package and it subpackages, wildcards '*' and '?' are available
            packages(
                listOf(
                    "uk.ryanwong.gmap2ics.app.configs.*",
                    "uk.ryanwong.gmap2ics.ui.screens.*",
                    "uk.ryanwong.gmap2ics.ui.theme.*",
                ),
            )
        }
    }

    // configure default reports - for Kotlin/JVM or Kotlin/MPP projects or merged android variants
    defaults {
        //  generate an XML report when running the `check` task
        xml {
            onCheck = true
        }

        //  generate a HTML report when running the `check` task
        html {
            onCheck = true
        }

        //  verify coverage when running the `check` task
        verify {
            onCheck = true
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GMapTimelineToICS"
            packageVersion = libs.versions.packageVersion.get()
        }
    }
}

tasks.withType<Test>().configureEach {
    // This is for kotest
    useJUnitPlatform()
    jvmArgs = mutableListOf("--enable-preview")
}
