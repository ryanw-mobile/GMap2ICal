/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.screens.components

data class LogWindowUIState(
    val exportedCount: Int,
    val ignoredCount: Int,
    val selectedTab: LogWindowTab,
    val onTabSelected: (selectedTab: LogWindowTab) -> Unit,
)

enum class LogWindowTab {
    EXPORTED,
    IGNORED,
}
