/*
 * Copyright (c) 2022. Ryan Wong (hello@ryanwong.co.uk)
 */

package uk.ryanwong.gmap2ics.data.source.googleapi.ktor

import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import uk.ryanwong.gmap2ics.data.models.places.Geometry
import uk.ryanwong.gmap2ics.data.models.places.Location
import uk.ryanwong.gmap2ics.data.models.places.Result
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.GoogleMapsApiClientImplTestData.mockPlaceDetailsGregAve
import uk.ryanwong.gmap2ics.data.source.googleapi.ktor.impl.GoogleMapsApiClientImpl

internal class GoogleMapsApiClientImplTest : FreeSpec() {

    private lateinit var apiClient: GoogleMapsApiClientImpl

    private fun setupEngine(status: HttpStatusCode, payload: String) {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(payload),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        apiClient = GoogleMapsApiClientImpl(engine = mockEngine)
    }

    init {
        "getPlaceDetails" - {
            "should return PlaceDetails correctly if API request is successful" {
                runBlocking {
                    // 🔴 Given
                    setupEngine(
                        status = HttpStatusCode.OK,
                        payload = mockPlaceDetailsGregAve
                    )

                    // 🟡 When
                    val placeDetails = apiClient.getPlaceDetails(
                        placeId = "some-placeId",
                        apiKey = "some-api-key",
                        language = "some-language"
                    )

                    // 🟢 Then
                    placeDetails shouldBe uk.ryanwong.gmap2ics.data.models.places.PlaceDetails(
                        result = Result(
                            formattedAddress = "8 Greg Ave, Bollington, Macclesfield SK10 5HR, UK",
                            formattedPhoneNumber = null,
                            geometry = Geometry(location = Location(lat = 53.2945761, lng = -2.114387)),
                            icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
                            name = "8 Greg Ave",
                            placeId = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
                            reference = "ChIJ43NG7NxLekgR7NFDJzb-WXw",
                            types = listOf("premise"),
                            url = "https://maps.google.com/?q=8+Greg+Ave,+Bollington,+Macclesfield+SK10+5HR,+UK&ftid=0x487a4bdcec4673e3:0x7c59fe362743d1ec",
                            userRatingsTotal = null,
                            utcOffset = 60,
                            vicinity = "Bollington",
                            website = null
                        )
                    )
                }
            }

            "should return null if API request returns error message" {
                runBlocking {
                    // 🔴 Given
                    setupEngine(
                        status = HttpStatusCode.OK,
                        payload = """{
                                   "error_message" : "Missing the placeid or reference parameter.",
                                   "html_attributions" : [],
                                   "status" : "INVALID_REQUEST"
                                }"""
                    )

                    // 🟡 When
                    val placeDetails = apiClient.getPlaceDetails(
                        placeId = "some-placeId",
                        apiKey = "some-api-key",
                        language = "some-language"
                    )

                    // 🟢 Then
                    placeDetails?.result shouldBe null
                }
            }
        }
    }
}