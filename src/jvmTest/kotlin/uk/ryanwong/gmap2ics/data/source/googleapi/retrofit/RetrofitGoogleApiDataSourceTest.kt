/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.retrofit

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.repository.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.source.googleapi.GoogleApiDataSource
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSourceTestData.retrofitApiResponseFailure
import uk.ryanwong.gmap2ics.data.source.googleapi.retrofit.RetrofitGoogleApiDataSourceTestData.retrofitApiResponseSuccess

class RetrofitGoogleApiDataSourceTest : FreeSpec() {

    private lateinit var retrofitGoogleApiDataSource: GoogleApiDataSource
    private lateinit var retrofitService: GoogleMapsApiService

    private fun setupDataSource() {
        retrofitService = mockk()
        retrofitGoogleApiDataSource = RetrofitGoogleApiDataSource(retrofitService = retrofitService)
    }

    init {
        "getMapsApiPlaceDetails" - {
            "Should return correct PlaceDetail if API request was successful" {
                runBlocking {
                    // 🔴 Given
                    setupDataSource()
                    coEvery {
                        retrofitService.getMapsApiPlaceDetails(any(), any(), any())
                    } returns retrofitApiResponseSuccess

                    // 🟡 When
                    val placeDetails = retrofitGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language"
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
                                url = "https://maps.google.com/?q=some+address,+Manchester+M21,+UK&ftid=0x1111111111111111:0x1111111111111111"
                            )
                }
            }

            "Should throw GetPlaceDetailsAPIErrorException if API request failed" {
                runBlocking {
                    // 🔴 Given
                    setupDataSource()
                    coEvery {
                        retrofitService.getMapsApiPlaceDetails(any(), any(), any())
                    } returns retrofitApiResponseFailure

                    // 🟡 When
                    val placeDetails = retrofitGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language"
                    )

                    // 🟢 Then
                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() shouldBe GetPlaceDetailsAPIErrorException("Response.error()")
                }
            }

            "Should throw PlaceDetailsNotFoundException if API returns no result" {
                runBlocking {
                    // 🔴 Given
                    setupDataSource()
                    coEvery {
                        retrofitService.getMapsApiPlaceDetails(any(), any(), any())
                    } returns Response.success(200, uk.ryanwong.gmap2ics.data.models.places.PlaceDetails())

                    // 🟡 When
                    val placeDetails = retrofitGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language"
                    )

                    // 🟢 Then
                    placeDetails.isFailure shouldBe true
                    placeDetails.exceptionOrNull() shouldBe PlaceDetailsNotFoundException(placeId = "some-place-id")
                }
            }
        }
    }
}