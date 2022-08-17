/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.cio.CIO
import uk.ryanwong.gmap2ics.app.configs.RyanConfig
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapImpl
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import uk.ryanwong.gmap2ics.data.repository.LocalFileRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.TimelineRepositoryImpl
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.GoogleMapsApiClientImpl
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.KtorGoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSource
import uk.ryanwong.gmap2ics.ui.screens.mainScreen
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromActivitySegmentUseCaseImpl
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromChildVisitUseCaseImpl
import uk.ryanwong.gmap2ics.ui.usecases.VEventFromPlaceVisitUseCaseImpl
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import us.dustinj.timezonemap.TimeZoneMap

fun main() = application {
    Napier.base(DebugAntilog())
    val configFile = RyanConfig() // Specify your config here
    val useKtor = true

    // TODO: dependency injection
    val googleMapsApiClient = GoogleMapsApiClientImpl(engine = CIO.create())
    val networkDataSource = if (useKtor) {
        KtorGoogleApiDataSource(googleMapsApiClient = googleMapsApiClient)
    } else {
        RetrofitGoogleApiDataSource()
    }

    val placeDetailsRepository = PlaceDetailsRepositoryImpl(
        networkDataSource = networkDataSource,
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
                placeDetailsRepository = placeDetailsRepository
            ),
            vEventFromChildVisitUseCase = VEventFromChildVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
            vEventFromPlaceVisitUseCase = VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository)
        )
    )
}
