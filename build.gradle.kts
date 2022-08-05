/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

@file:Suppress("HardCodedStringLiteral")

import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization") version "1.7.10"
    id("org.jetbrains.compose")
}

group = "uk.ryanwong"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

val ktorVersion = "2.0.3"
kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.3")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.3")
                implementation("us.dustinj.timezonemap:timezonemap:4.5")
                implementation("com.squareup.moshi:moshi-kotlin:1.13.0")
                implementation("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.4")
                implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.mockk:mockk:1.12.5")
                // kotest
                implementation("io.kotest:kotest-runner-junit5:5.3.1")
                implementation("io.kotest:kotest-assertions-core:5.3.1")
                implementation("io.kotest:kotest-property:5.3.1")
            }
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
}
