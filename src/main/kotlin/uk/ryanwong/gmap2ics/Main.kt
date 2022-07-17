package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import uk.ryanwong.gmap2ics.configs.RyanConfig
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.repository.TimelineRepository
import uk.ryanwong.gmap2ics.ui.mainScreen
import uk.ryanwong.gmap2ics.ui.models.MainScreenViewModel

fun main() = application {

    val configFile = RyanConfig() // Specify your config here

    // TODO: dependency injection
    mainScreen(
        onCloseRequest = { exitApplication() },
        mainScreenViewModel = MainScreenViewModel(
            configFile = configFile,
            timelineRepository = TimelineRepository(
                configFile = configFile,
                placeDetailsRepository = PlaceDetailsRepository(configFile = configFile)
            )
        )
    )
}
