/*
 * Copyright (c) 2024. Ryan Wong (hello@ryanwebmail.com)
 */

plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
}
