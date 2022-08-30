/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.cio.CIO
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.app.configs.RyanConfig
import uk.ryanwong.gmap2ics.app.usecases.impl.GetActivitySegmentVEventUseCaseImpl
import uk.ryanwong.gmap2ics.app.usecases.impl.GetOutputFilenameUseCaseImpl
import uk.ryanwong.gmap2ics.app.usecases.impl.GetPlaceVisitVEventUseCaseImpl
import uk.ryanwong.gmap2ics.app.usecases.impl.VEventFromActivitySegmentUseCaseImpl
import uk.ryanwong.gmap2ics.app.usecases.impl.VEventFromChildVisitUseCaseImpl
import uk.ryanwong.gmap2ics.app.usecases.impl.VEventFromPlaceVisitUseCaseImpl
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapImpl
import uk.ryanwong.gmap2ics.app.utils.timezonemap.TimeZoneMapWrapper
import uk.ryanwong.gmap2ics.data.repository.impl.LocalFileRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.impl.PlaceDetailsRepositoryImpl
import uk.ryanwong.gmap2ics.data.repository.impl.TimelineRepositoryImpl
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.KtorGoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.impl.GoogleMapsApiClientImpl
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSource
import uk.ryanwong.gmap2ics.ui.GregoryGreenTheme
import uk.ryanwong.gmap2ics.ui.screens.mainScreen
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import us.dustinj.timezonemap.TimeZoneMap
import java.util.Locale
import java.util.ResourceBundle

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
    val resourceBundle = ResourceBundle.getBundle("resources", Locale.ENGLISH)
    val getPlaceVisitVEventUseCase = GetPlaceVisitVEventUseCaseImpl(
        vEventFromChildVisitUseCase = VEventFromChildVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
        vEventFromPlaceVisitUseCase = VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
    )

    GregoryGreenTheme {
        mainScreen(
            onCloseRequest = { exitApplication() },
            mainScreenViewModel = MainScreenViewModel(
                configFile = configFile,
                resourceBundle = resourceBundle,
                timelineRepository = TimelineRepositoryImpl(
                    timeZoneMap = timeZoneMap,
                    kotlinJson = Json { ignoreUnknownKeys = true }
                ),
                localFileRepository = LocalFileRepositoryImpl(),
                getActivitySegmentVEventUseCase = GetActivitySegmentVEventUseCaseImpl(
                    vEventFromActivitySegmentUseCase = VEventFromActivitySegmentUseCaseImpl(
                        placeDetailsRepository = placeDetailsRepository
                    )
                ),
                getOutputFilenameUseCase = GetOutputFilenameUseCaseImpl(),
                getPlaceVisitVEventUseCase = getPlaceVisitVEventUseCase,
            )
        )
    }
}
