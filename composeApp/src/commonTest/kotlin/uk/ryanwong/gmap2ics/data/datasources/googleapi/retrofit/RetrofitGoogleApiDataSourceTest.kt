/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.retrofit

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import retrofit2.Response
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.datasources.googleapi.retrofit.RetrofitGoogleApiDataSourceTestData.retrofitApiResponseFailure
import uk.ryanwong.gmap2ics.data.datasources.googleapi.retrofit.RetrofitGoogleApiDataSourceTestData.retrofitApiResponseSuccess
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails

@OptIn(ExperimentalCoroutinesApi::class)
class RetrofitGoogleApiDataSourceTest : FreeSpec() {

    private lateinit var retrofitGoogleApiDataSource: GoogleApiDataSource
    private lateinit var retrofitService: GoogleMapsApiService
    private lateinit var scope: TestScope

    private fun setupDataSource() {
        val dispatcher = StandardTestDispatcher()
        scope = TestScope(dispatcher)
        retrofitService = mockk()
        retrofitGoogleApiDataSource = RetrofitGoogleApiDataSource(retrofitService = retrofitService)
    }

    init {
        "getMapsApiPlaceDetails" - {
            "Should return correct PlaceDetail if API request was successful" {
                setupDataSource()
                scope.runTest {
                    // 🔴 Given
                    coEvery {
                        retrofitService.getMapsApiPlaceDetails(any(), any(), any())
                    } returns retrofitApiResponseSuccess

                    // 🟡 When
                    val placeDetails = retrofitGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )

                    // 🟢 Then
                    placeDetails.isSuccess shouldBe true
                    placeDetails.getOrNull() shouldBe
                        PlaceDetails(
                            placeId = "some-place-id",
                            name = "some-name",
                            formattedAddress = "some-formatted-address",
                            geo = LatLng(latitude = 50.44444444444444, longitude = -2.111111111111111),
                            types = listOf("route"),
                            url = "https://maps.google.com/?q=some+address,+Manchester+M21,+UK&ftid=0x1111111111111111:0x1111111111111111",
                        )
                }
            }

            "Should throw GetPlaceDetailsAPIErrorException if API request failed" {
                setupDataSource()
                scope.runTest {
                    // 🔴 Given
                    coEvery {
                        retrofitService.getMapsApiPlaceDetails(any(), any(), any())
                    } returns retrofitApiResponseFailure

                    // 🟡 When
                    val placeDetails = retrofitGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )

                    // 🟢 Then
                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() shouldBe GetPlaceDetailsAPIErrorException("Response.error()")
                }
            }

            "Should throw PlaceDetailsNotFoundException if API returns no result" {
                setupDataSource()
                scope.runTest {
                    // 🔴 Given
                    coEvery {
                        retrofitService.getMapsApiPlaceDetails(any(), any(), any())
                    } returns Response.success(200, uk.ryanwong.gmap2ics.data.models.places.PlaceDetails())

                    // 🟡 When
                    val placeDetails = retrofitGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )

                    // 🟢 Then
                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() shouldBe PlaceDetailsNotFoundException(placeId = "some-place-id")
                }
            }
        }
    }
}
