/*
 * Copyright (c) 2022-2024. Ryan Wong (hello@ryanwebmail.com)
 */

package uk.ryanwong.gmap2ics.data.datasources.googleapi

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import uk.ryanwong.gmap2ics.data.datasources.googleapi.KtorGoogleApiDataSourceTestData.PLACE_DETAILS_GREG_AVE_DTO
import uk.ryanwong.gmap2ics.data.datasources.googleapi.KtorGoogleApiDataSourceTestData.PLACE_DETAILS_GREG_AVE_JSON

@OptIn(ExperimentalCoroutinesApi::class)
internal class KtorGoogleApiDataSourceTest : FreeSpec() {

    private lateinit var ktorGoogleApiDataSource: KtorGoogleApiDataSource

    private fun setupDataSource(status: HttpStatusCode, contentType: String, payload: String) {
        val mockEngine = MockEngine { _ ->
            respond(
                content = ByteReadChannel(payload),
                status = status,
                headers = headersOf(HttpHeaders.ContentType, contentType),
            )
        }
        ktorGoogleApiDataSource = KtorGoogleApiDataSource(
            engine = mockEngine,
            dispatcher = UnconfinedTestDispatcher(),
        )
    }

    init {
        "getMapsApiPlaceDetails" - {
            "should return PlaceDetails correctly if API request is successful" {
                runTest {
                    setupDataSource(
                        status = HttpStatusCode.OK,
                        contentType = "application/json",
                        payload = PLACE_DETAILS_GREG_AVE_JSON,
                    )
                    val expectedPlaceDetails = PLACE_DETAILS_GREG_AVE_DTO

                    val placeDetails = ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                        placeId = "some-place-id",
                        apiKey = "some-api-key",
                        language = "some-language",
                    )

                    placeDetails shouldBe expectedPlaceDetails
                }
            }

            "should return Failure.GetPlaceDetailsAPIErrorException if API request returns error message" {
                runTest {
                    setupDataSource(
                        status = HttpStatusCode.OK,
                        contentType = "application/json",
                        payload = """{
                                   "error_message" : "Missing the placeid or reference parameter.",
                                   "html_attributions" : [],
                                   "status" : "INVALID_REQUEST"
                                }""",
                    )

                    val exception = shouldThrow<GetPlaceDetailsAPIErrorException> {
                        ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                            placeId = "some-place-id",
                            apiKey = "some-api-key",
                            language = "some-language",
                        )
                    }
                    exception.message shouldBe "some-exception-message"
                }
            }

            "Should return Failure.GetPlaceDetailsAPIErrorException if API request throws an exception" {
                runTest {
                    setupDataSource(
                        status = HttpStatusCode.InternalServerError,
                        contentType = "text/plain",
                        payload = """Internal Server Error""",
                    )

                    val exception = shouldThrow<GetPlaceDetailsAPIErrorException> {
                        ktorGoogleApiDataSource.getMapsApiPlaceDetails(
                            placeId = "some-place-id",
                            apiKey = "some-api-key",
                            language = "some-language",
                        )
                    }
                    exception.message shouldBe "some-exception-message"
                }
            }
        }
    }
}
