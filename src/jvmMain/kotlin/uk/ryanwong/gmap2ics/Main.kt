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
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromActivitySegmentUseCaseImpl
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromChildVisitUseCaseImpl
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromPlaceVisitUseCaseImpl
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapImpl
import uk.ryanwong.gmap2ics.utils.timezonemap.TimeZoneMapWrapper
import us.dustinj.timezonemap.TimeZoneMap

fun main() = application {

    val configFile = RyanConfig() // Specify your config here

    // TODO: dependency injection
    val placeDetailsRepository = PlaceDetailsRepositoryImpl(
        placesApiKey = configFile.placesApiKey,
        apiLanguageOverride = configFile.apiLanguageOverride
    )
    val timeZoneMap: TimeZoneMapWrapper = TimeZoneMapImpl(timeZoneMap = TimeZoneMap.forEverywhere())
    mainScreen(
        onCloseRequest = { exitApplication() },
        mainScreenViewModel = MainScreenViewModel(
            configFile = configFile,
            timelineRepository = TimelineRepositoryImpl(timeZoneMap = timeZoneMap),
            localFileRepository = LocalFileRepositoryImpl(),
            vEventFromActivitySegmentUseCase = VEventFromActivitySegmentUseCaseImpl(
                placeDetailsRepository = placeDetailsRepository,
                timeZoneMap = timeZoneMap
            ),
            vEventFromChildVisitUseCase = VEventFromChildVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
            vEventFromPlaceVisitUseCase = VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository)
        )
    )
}
