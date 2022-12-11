/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    id("org.jetbrains.compose")
    id("org.jlleitschuh.gradle.ktlint") version "11.0.0"
}

group = "uk.ryanwong"
version = "1.0-SNAPSHOT"

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

    val ktorVersion = "2.2.1"
    val kotestVersion = "5.5.4"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                implementation("us.dustinj.timezonemap:timezonemap:4.5")
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
                implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                implementation("io.github.aakira:napier:2.6.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
                implementation("io.mockk:mockk:1.12.7")
                // kotest
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation("io.kotest:kotest-property:$kotestVersion")
                // ktor
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                // Compose
                implementation(compose("org.jetbrains.compose.ui:ui-test-junit4"))
                // implementation("org.jetbrains.compose.ui:ui-test-junit4:1.2.0-alpha01-dev620")
                implementation("org.junit.jupiter:junit-jupiter:5.9.1")
                implementation("org.junit.vintage:junit-vintage-engine:5.9.1")
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
            packageVersion = "1.0.0"
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
