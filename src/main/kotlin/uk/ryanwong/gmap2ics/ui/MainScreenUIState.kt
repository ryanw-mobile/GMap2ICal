package uk.ryanwong.gmap2ics.ui

sealed class MainScreenUIState {
    object Ready : MainScreenUIState()
    object Processing : MainScreenUIState()
    object ShowChangeJsonPathDialog : MainScreenUIState()
    object ShowChangeICalPathDialog : MainScreenUIState()
    data class Error(val errMsg: String) : MainScreenUIState()
}