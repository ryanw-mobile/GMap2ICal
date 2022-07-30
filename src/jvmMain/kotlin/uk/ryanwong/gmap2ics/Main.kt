package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import uk.ryanwong.gmap2ics.configs.RyanConfig
import uk.ryanwong.gmap2ics.data.repository.LocalFileRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.TimelineRepositoryImpl
import uk.ryanwong.gmap2ics.ui.mainScreen
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel

fun main() = application {

    val configFile = RyanConfig() // Specify your config here

    // TODO: dependency injection
    mainScreen(
        onCloseRequest = { exitApplication() },
        mainScreenViewModel = MainScreenViewModel(
            configFile = configFile,
            timelineRepository = TimelineRepositoryImpl(
                configFile = configFile,
                placeDetailsRepository = PlaceDetailsRepositoryImpl(configFile = configFile)
            ),
            localFileRepository = LocalFileRepositoryImpl()
        )
    )
}
