/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.serialization)
    alias(libs.plugins.kover)
    alias(libs.plugins.compose)
    alias(libs.plugins.gradle.ktlint)
    alias(libs.plugins.compose.compiler)
}

@OptIn(ExperimentalKotlinGradlePluginApi::class)
kotlin {
    jvmToolchain(17)
    jvm("desktop") {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
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

    val skikoVersion = "0.7.70" // or any more recent version
    val skikoTarget = "$targetOs-$targetArch"

    sourceSets {
        val desktopMain by getting
        val desktopTest by getting

        commonMain.dependencies {
            implementation(compose.components.resources)

            implementation(compose.desktop.currentOs)

            implementation(libs.timezonemap)
            implementation(libs.napier)
            implementation(libs.themedetector)

            implementation(libs.kotlinx.coroutines.core.jvm)
            implementation(libs.kotlinx.coroutines.swing)
            implementation(libs.kotlinx.serialization.json)

            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.koin.core)
            implementation(libs.koin.test)

            api(libs.moko.mvvm.core)
            api(libs.moko.mvvm.compose)
        }
        desktopMain.dependencies {
            implementation("org.jetbrains.skiko:skiko-awt-runtime-$skikoTarget:$skikoVersion")
            // implementation(compose.desktop.macos_arm64)
            implementation(compose.desktop.currentOs)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))

            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.mockk)
            implementation(libs.ktor.client.mock)

            implementation(libs.junit.jupiter)
            implementation(libs.junit.vintage.engine)

            implementation(libs.moko.mvvm.test)
        }
        desktopTest.dependencies {
            implementation(libs.compose.ui.test.junit4)
        }
    }
}

ktlint {
    enableExperimentalRules.set(true)
    filter {
        // exclude("**/generated/**")
        exclude { projectDir.toURI().relativize(it.file.toURI()).path.contains("/generated/") }
    }
}

compose.desktop {
    application {
        mainClass = "uk.ryanwong.gmap2ics.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "uk.ryanwong.gmap2ics"
            packageVersion = libs.versions.packageVersion.get()
        }
    }
}

kover {
    reports {
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
                        "gmap2ical.composeapp.generated.resources*",
                        "uk.ryanwong.gmap2ics.di*",
                        "uk.ryanwong.gmap2ics.app.configs",
                        "uk.ryanwong.gmap2ics.ui.screens",
                        "uk.ryanwong.gmap2ics.ui.theme",
                    ),
                )
            }
        }
    }
}

tasks.withType<Test>().configureEach {
    jvmArgs = mutableListOf("--enable-preview")
}
