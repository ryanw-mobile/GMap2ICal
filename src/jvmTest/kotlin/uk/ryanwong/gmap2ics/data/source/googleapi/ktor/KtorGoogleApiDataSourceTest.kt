/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.gmap2ics.app.models.timeline.LatLng
import uk.ryanwong.gmap2ics.app.models.timeline.PlaceDetails
import uk.ryanwong.gmap2ics.data.repository.impl.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.data.source.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.KtorGoogleApiDataSourceTestData.mockPlaceDetailsDataModel
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.mocks.MockGoogleMapsApiClient
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class KtorGoogleApiDataSourceTest : FreeSpec() {

    private lateinit var ktorGoogleApiDataSource: KtorGoogleApiDataSource
    private lateinit var mockGoogleMapsApiClient: MockGoogleMapsApiClient

    private fun setupDataSource() {
        mockGoogleMapsApiClient = MockGoogleMapsApiClient()
        ktorGoogleApiDataSource = KtorGoogleApiDataSource(
            googleMapsApiClient = mockGoogleMapsApiClient,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    init {
        "getMapsApiPlaceDetails" - {
            "Should return correct PlaceDetails App Model if data source return Data Model properly" {
                // 🔴 Given
                setupDataSource()
                val placeDetailsDataModel = mockPlaceDetailsDataModel
                mockGoogleMapsApiClient.getPlaceDetailsResponse = placeDetailsDataModel

                // 🟡 When
                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = "some-language",
                )

                // 🟢 Then
                result.isSuccess shouldBe true
                result.getOrNull() shouldBe PlaceDetails(
                    placeId = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
                    name = "8 Greg Ave",
                    formattedAddress = "8 Greg Ave, Bollington, Macclesfield SK10 5HR, UK",
                    geo = LatLng(latitude = 53.2945761, longitude = -2.114387),
                    types = listOf("premise"),
                    url = "https://maps.google.com/?q=8+Greg+Ave,+Bollington,+Macclesfield+SK10+5HR,+UK&ftid=0x487a4bdcec4673e3:0x7c59fe362743d1ec",
                )
            }

            "Should return correct PlaceDetails App Model when language is null" {
                // 🔴 Given
                setupDataSource()
                val placeDetailsDataModel = mockPlaceDetailsDataModel
                mockGoogleMapsApiClient.getPlaceDetailsResponse = placeDetailsDataModel

                // 🟡 When
                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = null,
                )

                // 🟢 Then
                result.isSuccess shouldBe true
                result.getOrNull() shouldBe PlaceDetails(
                    placeId = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
                    name = "8 Greg Ave",
                    formattedAddress = "8 Greg Ave, Bollington, Macclesfield SK10 5HR, UK",
                    geo = LatLng(latitude = 53.2945761, longitude = -2.114387),
                    types = listOf("premise"),
                    url = "https://maps.google.com/?q=8+Greg+Ave,+Bollington,+Macclesfield+SK10+5HR,+UK&ftid=0x487a4bdcec4673e3:0x7c59fe362743d1ec",
                )
            }

            "Should rethrow the exception if data source throws CancellationException" {
                // 🔴 Given
                setupDataSource()
                mockGoogleMapsApiClient.getPlaceDetailsException = CancellationException()

                // 🟡 When
                val exception = shouldThrow<CancellationException> {
                    ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )
                }

                // 🟢 Then
                exception.message shouldBe null
            }

            "Should return Failure.PlaceDetailsNotFoundException if data source returns null" {
                // 🔴 Given
                setupDataSource()
                mockGoogleMapsApiClient.getPlaceDetailsException = null
                mockGoogleMapsApiClient.getPlaceDetailsResponse = null

                // 🟡 When
                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = "some-language",
                )

                // 🟢 Then
                result.isFailure shouldBe true
                result.exceptionOrNull() shouldBe PlaceDetailsNotFoundException(placeId = "some-place-id")
            }

            "Should return Failure.GetPlaceDetailsAPIErrorException if data source throws other exceptions" {
                // 🔴 Given
                setupDataSource()
                mockGoogleMapsApiClient.getPlaceDetailsException = IOException("some-exception-message")

                // 🟡 When
                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = "some-language",
                )

                // 🟢 Then
                result.isFailure shouldBe true
                result.exceptionOrNull() shouldBe GetPlaceDetailsAPIErrorException("some-exception-message")
            }
        }
    }
}
