/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import uk.ryanwong.gmap2ics.data.datasources.googleapi.GetPlaceDetailsAPIErrorException
import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.KtorGoogleApiDataSourceTestData.mockPlaceDetailsDataModel
import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.mocks.MockGoogleMapsApiClient
import uk.ryanwong.gmap2ics.data.repositories.PlaceDetailsNotFoundException
import uk.ryanwong.gmap2ics.domain.models.timeline.LatLng
import uk.ryanwong.gmap2ics.domain.models.timeline.PlaceDetails
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
internal class KtorGoogleApiDataSourceTest : FreeSpec() {

    private lateinit var ktorGoogleApiDataSource: KtorGoogleApiDataSource
    private lateinit var mockGoogleMapsApiClient: MockGoogleMapsApiClient

    init {
        beforeTest {
            mockGoogleMapsApiClient = MockGoogleMapsApiClient()
            ktorGoogleApiDataSource = KtorGoogleApiDataSource(
                googleMapsApiClient = mockGoogleMapsApiClient,
                dispatcher = UnconfinedTestDispatcher(),
            )
        }

        "getMapsApiPlaceDetails" - {
            "Should return correct PlaceDetails App Model if data source return Data Model properly" {
                val placeDetailsDataModel = mockPlaceDetailsDataModel
                mockGoogleMapsApiClient.getPlaceDetailsResponse = placeDetailsDataModel

                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = "some-language",
                )

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
                val placeDetailsDataModel = mockPlaceDetailsDataModel
                mockGoogleMapsApiClient.getPlaceDetailsResponse = placeDetailsDataModel

                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = null,
                )

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
                mockGoogleMapsApiClient.getPlaceDetailsException = CancellationException()

                val exception = shouldThrow<CancellationException> {
                    ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )
                }

                exception.message shouldBe null
            }

            "Should return Failure.PlaceDetailsNotFoundException if data source returns null" {
                mockGoogleMapsApiClient.getPlaceDetailsException = null
                mockGoogleMapsApiClient.getPlaceDetailsResponse = null

                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = "some-language",
                )

                result.isFailure shouldBe true
                result.exceptionOrNull() shouldBe PlaceDetailsNotFoundException(placeId = "some-place-id")
            }

            "Should return Failure.GetPlaceDetailsAPIErrorException if data source throws other exceptions" {
                mockGoogleMapsApiClient.getPlaceDetailsException = IOException("some-exception-message")

                val result = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                    placeId = "some-place-id",
                    apiKey = "some-api-key",
                    language = "some-language",
                )

                result.isFailure shouldBe true
                result.exceptionOrNull() shouldBe GetPlaceDetailsAPIErrorException("some-exception-message")
            }
        }
    }
}
