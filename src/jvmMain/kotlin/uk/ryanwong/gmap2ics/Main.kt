/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import uk.ryanwong.gmap2ics.configs.RyanConfig
import uk.ryanwong.gmap2ics.data.repository.LocalFileRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.TimelineRepositoryImpl
import uk.ryanwong.gmap2ics.ui.mainScreen
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import uk.ryanwong.gmap2ics.ui.usecases.ExportActivitySegmentUseCaseImpl
import uk.ryanwong.gmap2ics.ui.usecases.ExportChildVisitUseCaseImpl
import uk.ryanwong.gmap2ics.ui.usecases.ExportPlaceVisitUseCaseImpl

fun main() = application {

    val configFile = RyanConfig() // Specify your config here

    // TODO: dependency injection
    val placeDetailsRepository = PlaceDetailsRepositoryImpl(configFile = configFile)
    mainScreen(
        onCloseRequest = { exitApplication() },
        mainScreenViewModel = MainScreenViewModel(
            configFile = configFile,
            timelineRepository = TimelineRepositoryImpl(),
            localFileRepository = LocalFileRepositoryImpl(),
            exportActivitySegmentUseCase = ExportActivitySegmentUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
            exportChildVisitUseCase = ExportChildVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
            exportPlaceVisitUseCase = ExportPlaceVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository)
        )
    )
}
