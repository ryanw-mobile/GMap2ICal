/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.repositories

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.gmap2ics.data.datasources.local.fakes.FakeGoogleApiDataSource
import uk.ryanwong.gmap2ics.data.models.places.GeometryDto
import uk.ryanwong.gmap2ics.data.models.places.LocationDto
import uk.ryanwong.gmap2ics.data.models.places.PlaceDetailsDto
import uk.ryanwong.gmap2ics.data.models.places.ResultDto
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.domain.repositories.PlaceDetailsRepository

@OptIn(ExperimentalCoroutinesApi::class)
internal class PlaceDetailsRepositoryImplTest : FreeSpec() {

    private lateinit var placeDetailsRepository: PlaceDetailsRepository
    private lateinit var fakeGoogleApiDataSource: FakeGoogleApiDataSource

    private fun setupRepository(apiLanguageOverride: Map<String, String> = mapOf()) {
        fakeGoogleApiDataSource = FakeGoogleApiDataSource()
        placeDetailsRepository = PlaceDetailsRepositoryImpl(
            networkDataSource = fakeGoogleApiDataSource,
            placesApiKey = "some-api-key",
            apiLanguageOverride = apiLanguageOverride,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    init {
        "getPlaceDetails" - {
            "enablePlacesApiLookup is false" - {
                "Should return PlaceDetailsNotFoundException if placeId is not cached" {
                    setupRepository()
                    val placeId = "some-place-id"
                    val enablePlacesApiLookup = false

                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = enablePlacesApiLookup,
                    )

                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() should beInstanceOf<PlaceDetailsNotFoundException>()
                }

                "Should return cached PlaceDetails if it is in the cache" {
                    setupRepository()
                    val placeId = "some-place-id"
                    val enablePlacesApiLookup = false

                    // cache the PlaceDetails first
                    val apiResponse = PlaceDetailsDto(
                        ResultDto(
                            placeId = "some-place-id",
                            name = "some-name",
                            formattedAddress = "some-formatted-address",
                            geometry = GeometryDto(LocationDto(lat = 53.6152405, lng = -1.5639315)),
                            types = listOf(
                                "some-unknown-type",
                            ),
                            url = "https://maps.google.com/?cid=1021876599690425051",
                        ),
                    )
                    fakeGoogleApiDataSource.getMapsApiPlaceDetailsResponse = apiResponse
                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = true,
                    )
                    fakeGoogleApiDataSource.getMapsApiPlaceDetailsResponse = null

                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = enablePlacesApiLookup,
                    )

                    placeDetails.isSuccess shouldBe true
                    placeDetails.getOrNull() shouldBe PlaceDetails(
                        placeId = "some-place-id",
                        name = "some-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                        types = listOf("some-unknown-type"),
                        url = "https://maps.google.com/?cid=1021876599690425051",
                    )
                }
            }

            "enablePlacesApiLookup is true" - {
                "Should return correct PlaceDetails if data source returns something" {
                    setupRepository()
                    val apiResponse = PlaceDetailsDto(
                        ResultDto(
                            placeId = "some-place-id",
                            name = "some-name",
                            formattedAddress = "some-formatted-address",
                            geometry = GeometryDto(LocationDto(lat = 53.6152405, lng = -1.5639315)),
                            types = listOf(
                                "some-unknown-type",
                            ),
                            url = "https://maps.google.com/?cid=1021876599690425051",
                        ),
                    )
                    fakeGoogleApiDataSource.getMapsApiPlaceDetailsResponse = apiResponse

                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = "some-place-id",
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = true,
                    )

                    placeDetails.isSuccess shouldBe true
                    placeDetails.getOrNull() shouldBe PlaceDetails(
                        placeId = "some-place-id",
                        name = "some-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                        types = listOf("some-unknown-type"),
                        url = "https://maps.google.com/?cid=1021876599690425051",
                    )
                }

                "Should return PlaceDetailsNotFoundException if data source returns not found and placeId is not cached" {
                    setupRepository()
                    val placeId = "some-place-id"
                    val enablePlacesApiLookup = true
                    fakeGoogleApiDataSource.fakeException = PlaceDetailsNotFoundException(placeId = placeId)

                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = enablePlacesApiLookup,
                    )

                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() should beInstanceOf<PlaceDetailsNotFoundException>()
                    placeDetails.exceptionOrNull()!!.message shouldBe "⛔️ placeId some-place-id not found"
                }

                "Should query data source by overriding language code if it is defined in apiLanguageOverride" {
                    setupRepository(
                        apiLanguageOverride = mapOf(
                            Pair("Asia/Tokyo", "ja"),
                            Pair("default", "some-language"),
                        ),
                    )
                    val placeId = "some-place-id"
                    val placeTimeZoneId = "Asia/Tokyo"
                    val enablePlacesApiLookup = true

                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = placeTimeZoneId,
                        enablePlacesApiLookup = enablePlacesApiLookup,
                    )

                    fakeGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested shouldBe "ja"
                }

                "Should query data source using default language code if it is defined in apiLanguageOverride" {
                    setupRepository(
                        apiLanguageOverride = mapOf(
                            Pair("Asia/Tokyo", "ja"),
                            Pair("default", "some-language"),
                        ),
                    )
                    val placeId = "some-place-id"
                    val placeTimeZoneId = "some-place-timezone-id"
                    val enablePlacesApiLookup = true

                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = placeTimeZoneId,
                        enablePlacesApiLookup = enablePlacesApiLookup,
                    )

                    fakeGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested shouldBe "some-language"
                }

                "Should query data source without specifying language if apiLanguageOverride has no default language" {
                    setupRepository(
                        apiLanguageOverride = mapOf(),
                    )
                    val placeId = "some-place-id"
                    val placeTimeZoneId = "some-place-timezone-id"
                    val enablePlacesApiLookup = true

                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = placeTimeZoneId,
                        enablePlacesApiLookup = enablePlacesApiLookup,
                    )

                    fakeGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested shouldBe null
                }
            }
        }
    }
}
