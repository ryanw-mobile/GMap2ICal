/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.ui.screens

sealed class MainScreenUIState {
    object Ready : MainScreenUIState()
    data class Processing(val progress: Float) : MainScreenUIState()
    object ChangeJsonPath : MainScreenUIState()
    object ChangeICalPath : MainScreenUIState()
    data class Error(val errMsg: String) : MainScreenUIState()
}
