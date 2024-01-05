/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics

import androidx.compose.ui.window.application
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.engine.cio.CIO
import kotlinx.serialization.json.Json
import uk.ryanwong.gmap2ics.app.configs.ProvideConfig
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
import uk.ryanwong.gmap2ics.ui.screens.mainScreen
import uk.ryanwong.gmap2ics.ui.theme.gregorygreen.GregoryGreenTheme
import uk.ryanwong.gmap2ics.ui.viewmodels.MainScreenViewModel
import us.dustinj.timezonemap.TimeZoneMap
import java.util.Locale
import java.util.ResourceBundle

fun main() = application {
    Napier.base(DebugAntilog())
    val configFile = ProvideConfig.getConfig() // Specify your config there
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
        apiLanguageOverride = configFile.apiLanguageOverride,
    )
    val timeZoneMap: TimeZoneMapWrapper = TimeZoneMapImpl(timeZoneMap = TimeZoneMap.forEverywhere())
    val resourceBundle = ResourceBundle.getBundle("resources", Locale.ENGLISH)
    val getPlaceVisitVEventUseCase = GetPlaceVisitVEventUseCaseImpl(
        vEventFromChildVisitUseCase = VEventFromChildVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
        vEventFromPlaceVisitUseCase = VEventFromPlaceVisitUseCaseImpl(placeDetailsRepository = placeDetailsRepository),
    )
    val viewModel = getViewModel(
        key = "main-screen",
        factory = viewModelFactory {
            MainScreenViewModel(
                configFile = configFile,
                resourceBundle = resourceBundle,
                timelineRepository = TimelineRepositoryImpl(
                    timeZoneMap = timeZoneMap,
                    kotlinJson = Json { ignoreUnknownKeys = true },
                ),
                localFileRepository = LocalFileRepositoryImpl(),
                getActivitySegmentVEventUseCase = GetActivitySegmentVEventUseCaseImpl(
                    vEventFromActivitySegmentUseCase = VEventFromActivitySegmentUseCaseImpl(
                        placeDetailsRepository = placeDetailsRepository,
                    ),
                ),
                getOutputFilenameUseCase = GetOutputFilenameUseCaseImpl(),
                getPlaceVisitVEventUseCase = getPlaceVisitVEventUseCase,
            )
        },
    )

    GregoryGreenTheme {
        mainScreen(
            onCloseRequest = { exitApplication() },
            mainScreenViewModel = viewModel,
        )
    }
}
