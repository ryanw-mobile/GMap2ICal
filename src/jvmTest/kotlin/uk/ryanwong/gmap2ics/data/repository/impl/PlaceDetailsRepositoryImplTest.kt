/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.repository.impl

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsRepository
import uk.ryanwong.gmap2ics.data.source.local.mocks.MockGoogleApiDataSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class PlaceDetailsRepositoryImplTest : FreeSpec() {

    private lateinit var placeDetailsRepository: PlaceDetailsRepository
    private lateinit var mockGoogleApiDataSource: MockGoogleApiDataSource

    private fun setupRepository(apiLanguageOverride: Map<String, String> = mapOf()) {
        mockGoogleApiDataSource = MockGoogleApiDataSource()
        placeDetailsRepository = PlaceDetailsRepositoryImpl(
            networkDataSource = mockGoogleApiDataSource,
            placesApiKey = "some-api-key",
            apiLanguageOverride = apiLanguageOverride,
            dispatcher = UnconfinedTestDispatcher()
        )
    }

    init {
        "getPlaceDetails" - {
            "enablePlacesApiLookup is false" - {
                "Should return PlaceDetailsNotFoundException if placeId is not cached" {
                    // 🔴 Given
                    setupRepository()
                    val placeId = "some-place-id"
                    val enablePlacesApiLookup = false

                    // 🟡 When
                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = enablePlacesApiLookup
                    )

                    // 🟢 Then
                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() should beInstanceOf<PlaceDetailsNotFoundException>()
                }

                "Should return cached PlaceDetails if it is in the cache" {
                    // 🔴 Given
                    setupRepository()
                    val placeId = "some-place-id"
                    val enablePlacesApiLookup = false

                    // cache the PlaceDetails first
                    val apiResponse = Result.success(
                        PlaceDetails(
                            placeId = "some-place-id",
                            name = "some-name",
                            formattedAddress = "some-formatted-address",
                            geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                            types = listOf(
                                "some-unknown-type"
                            ),
                            url = "https://maps.google.com/?cid=1021876599690425051"
                        )
                    )
                    mockGoogleApiDataSource.getMapsApiPlaceDetailsResponse = apiResponse
                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = true
                    )
                    mockGoogleApiDataSource.getMapsApiPlaceDetailsResponse = null

                    // 🟡 When
                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = enablePlacesApiLookup
                    )

                    // 🟢 Then
                    placeDetails.isSuccess shouldBe true
                    placeDetails.getOrNull() shouldBe PlaceDetails(
                        placeId = "some-place-id",
                        name = "some-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                        types = listOf("some-unknown-type"),
                        url = "https://maps.google.com/?cid=1021876599690425051"
                    )
                }
            }

            "enablePlacesApiLookup is true" - {
                "Should return correct PlaceDetails if data source returns something" {
                    // 🔴 Given
                    setupRepository()
                    val apiResponse = Result.success(
                        PlaceDetails(
                            placeId = "some-place-id",
                            name = "some-name",
                            formattedAddress = "some-formatted-address",
                            geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                            types = listOf(
                                "some-unknown-type"
                            ),
                            url = "https://maps.google.com/?cid=1021876599690425051"
                        )
                    )
                    mockGoogleApiDataSource.getMapsApiPlaceDetailsResponse = apiResponse

                    // 🟡 When
                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = "some-place-id",
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = true
                    )

                    // 🟢 Then
                    placeDetails.isSuccess shouldBe true
                    placeDetails.getOrNull() shouldBe PlaceDetails(
                        placeId = "some-place-id",
                        name = "some-name",
                        formattedAddress = "some-formatted-address",
                        geo = LatLng(latitude = 53.6152405, longitude = -1.5639315),
                        types = listOf("some-unknown-type"),
                        url = "https://maps.google.com/?cid=1021876599690425051"
                    )
                }

                "Should return PlaceDetailsNotFoundException if data source returns not found and placeId is not cached" {
                    // 🔴 Given
                    setupRepository()
                    val placeId = "some-place-id"
                    val enablePlacesApiLookup = true
                    mockGoogleApiDataSource.getMapsApiPlaceDetailsResponse =
                        Result.failure(exception = PlaceDetailsNotFoundException(placeId = placeId))

                    // 🟡 When
                    val placeDetails = placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = "Asia/Tokyo",
                        enablePlacesApiLookup = enablePlacesApiLookup
                    )

                    // 🟢 Then
                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() should beInstanceOf<PlaceDetailsNotFoundException>()
                    placeDetails.exceptionOrNull()!!.message shouldBe "⛔️ placeId some-place-id not found"
                }

                "Should query data source by overriding language code if it is defined in apiLanguageOverride" {
                    // 🔴 Given
                    setupRepository(
                        apiLanguageOverride = mapOf(
                            Pair("Asia/Tokyo", "ja"),
                            Pair("default", "some-language")
                        )
                    )
                    val placeId = "some-place-id"
                    val placeTimeZoneId = "Asia/Tokyo"
                    val enablePlacesApiLookup = true

                    // 🟡 When
                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = placeTimeZoneId,
                        enablePlacesApiLookup = enablePlacesApiLookup
                    )

                    // 🟢 Then
                    mockGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested shouldBe "ja"
                }

                "Should query data source using default language code if it is defined in apiLanguageOverride" {
                    // 🔴 Given
                    setupRepository(
                        apiLanguageOverride = mapOf(
                            Pair("Asia/Tokyo", "ja"),
                            Pair("default", "some-language")
                        )
                    )
                    val placeId = "some-place-id"
                    val placeTimeZoneId = "some-place-timezone-id"
                    val enablePlacesApiLookup = true

                    // 🟡 When
                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = placeTimeZoneId,
                        enablePlacesApiLookup = enablePlacesApiLookup
                    )

                    // 🟢 Then
                    mockGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested shouldBe "some-language"
                }

                "Should query data source without specifying language if apiLanguageOverride has no default language" {
                    // 🔴 Given
                    setupRepository(
                        apiLanguageOverride = mapOf()
                    )
                    val placeId = "some-place-id"
                    val placeTimeZoneId = "some-place-timezone-id"
                    val enablePlacesApiLookup = true

                    // 🟡 When
                    placeDetailsRepository.getPlaceDetails(
                        placeId = placeId,
                        placeTimeZoneId = placeTimeZoneId,
                        enablePlacesApiLookup = enablePlacesApiLookup
                    )

                    // 🟢 Then
                    mockGoogleApiDataSource.getMapsApiPlaceDetailsLanguageRequested shouldBe null
                }
            }
        }
    }
}