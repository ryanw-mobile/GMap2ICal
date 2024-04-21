/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.GoogleMapsApiClientImplTestData.mockPlaceDetailsGregAve
import uk.ryanwong.gmap2ics.data.datasources.googleapi.ktor.impl.GoogleMapsApiClientImpl
import uk.ryanwong.gmap2ics.data.models.places.Geometry
import uk.ryanwong.gmap2ics.data.models.places.Location
import uk.ryanwong.gmap2ics.data.models.places.Result

@OptIn(ExperimentalCoroutinesApi::class)
internal class GoogleMapsApiClientImplTest : FreeSpec() {

    private lateinit var apiClient: GoogleMapsApiClientImpl
    private lateinit var scope: TestScope

    private fun setupEngine(status: HttpStatusCode, payload: String) {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(payload),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json"),
            )
        }
        apiClient = GoogleMapsApiClientImpl(engine = mockEngine)
    }

    init {
        beforeTest {
            val dispatcher = StandardTestDispatcher()
            scope = TestScope(dispatcher)
        }

        "getPlaceDetails" - {
            "should return PlaceDetails correctly if API request is successful" {
                scope.runTest {
                    setupEngine(
                        status = HttpStatusCode.OK,
                        payload = mockPlaceDetailsGregAve,
                    )
                    val expectedPlaceDetails = uk.ryanwong.gmap2ics.data.models.places.PlaceDetails(
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
                            website = null,
                        ),
                    )

                    val placeDetails = apiClient.getPlaceDetails(
                        placeId = "some-placeId",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )

                    placeDetails shouldBe expectedPlaceDetails
                }
            }

            "should return null if API request returns error message" {
                scope.runTest {
                    setupEngine(
                        status = HttpStatusCode.OK,
                        payload = """{
                                   "error_message" : "Missing the placeid or reference parameter.",
                                   "html_attributions" : [],
                                   "status" : "INVALID_REQUEST"
                                }""",
                    )

                    val placeDetails = apiClient.getPlaceDetails(
                        placeId = "some-placeId",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )

                    placeDetails?.result shouldBe null
                }
            }
        }
    }
}
